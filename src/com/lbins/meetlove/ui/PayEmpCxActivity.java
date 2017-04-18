package com.lbins.meetlove.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.alipay.sdk.app.PayTask;
import com.alipay.share.sdk.openapi.algorithm.MD5;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.meetlove.R;
import com.lbins.meetlove.base.BaseActivity;
import com.lbins.meetlove.base.InternetURL;
import com.lbins.meetlove.data.OrderInfoAndSignDATA;
import com.lbins.meetlove.data.WxPayObjData;
import com.lbins.meetlove.module.Order;
import com.lbins.meetlove.module.WxPayObj;
import com.lbins.meetlove.pay.OrderInfoAndSign;
import com.lbins.meetlove.pay.PayResult;
import com.lbins.meetlove.util.StringUtil;
import com.lbins.meetlove.wxpay.Util;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 */
public class PayEmpCxActivity extends BaseActivity implements View.OnClickListener,Runnable {
    private TextView title;

    private Button btn_1;
    private Button btn_2;
    //---------------------------------支付开始----------------------------------------
    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
//                    PayResult payResult = new PayResult((String) msg.obj);
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
//                        Toast.makeText(OrderMakeActivity.this, "支付成功",
//                                Toast.LENGTH_SHORT).show();
                        //更新订单状态
                        updateMineOrder();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(PayEmpCxActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(PayEmpCxActivity.this, "支付失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(PayEmpCxActivity.this, "检查结果为：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        };
    };
    //------------------------------------------------------------------------------------
    //微信支付
    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
    private String out_trade_no;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_emp_cx_activity);  registerBoradcastReceiver();
        //微信支付
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, InternetURL.WEIXIN_APPID, false);
        
        initView();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("诚信认证支付");
        btn_1 = (Button) this.findViewById(R.id.btn_1);
        btn_2 = (Button) this.findViewById(R.id.btn_2);

        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
    }

    private int selectPayWay = 0;//0微信 1支付宝
    
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_1:
            {
                //点击微信支付
                showMsg(PayEmpCxActivity.this, "微信支付");
                selectPayWay = 0;
                Order order = new Order();
                order.setEmpid(getGson().fromJson(getSp().getString("empid", ""), String.class));
                order.setPayable_amount("120");
                order.setTrade_type("1");//0支付宝  1微信
                order.setOrder_cont("幸福牵手吧会员认证服务费，微信支付");
                order.setIs_dxk_order("1");//0认证服务费  1诚信保证金
                order.setPay_status("0");
                order.setStatus("0");
                goToPayWeixin(order);
            }
                break;
            case R.id.btn_2:
            {
                //支付宝支付
                //支付宝支付
                showMsg(PayEmpCxActivity.this, "支付宝支付");
                selectPayWay = 1;
                Order order = new Order();
                order.setEmpid(getGson().fromJson(getSp().getString("empid", ""), String.class));
                order.setPayable_amount("120");
                order.setTrade_type("0");//0支付宝  1微信
                order.setOrder_cont("幸福牵手吧会员认证服务费，支付宝支付");
                order.setIs_dxk_order("1");//0认证服务费  1诚信保证金
                order.setPay_status("0");
                order.setStatus("0");
                sendOrderToServer(order);
            }
                break;
        }
    }

    String xmlStr = "";
    WxPayObj wxPayObj;

    //传order给服务器
    private void sendOrderToServer(final Order order) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.SEND_ORDER_TOSERVER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo =  new JSONObject(s);
                                String code = jo.getString("code");
                                if (code.equals("200")) {
                                    OrderInfoAndSignDATA data = getGson().fromJson(s, OrderInfoAndSignDATA.class);
                                    //已经生成订单，等待支付，下面去支付
                                    out_trade_no= data.getData().getOut_trade_no();
                                    pay(data.getData());//调用支付接口
                                }else{
                                    showMsg(PayEmpCxActivity.this, jo.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(PayEmpCxActivity.this, "支付失败！", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(PayEmpCxActivity.this, "支付失败！", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empid", order.getEmpid());
                params.put("payable_amount", order.getPayable_amount());
                params.put("status", order.getStatus());
                params.put("pay_status", order.getPay_status());
                params.put("trade_type", order.getTrade_type());
                params.put("is_dxk_order", order.getIs_dxk_order());
                params.put("order_cont", order.getOrder_cont());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        getRequestQueue().add(request);
    }
    //---------------------------------------------------------支付宝------------------------------------------

    /**
     * call alipay sdk pay. 调用SDK支付
     *
     */
    public void pay(final OrderInfoAndSign orderInfoAndSign) {

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfoAndSign.getOrderInfo() + "&sign=\"" + orderInfoAndSign.getSign() + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
//                // 构造PayTask 对象
                PayTask alipay = new PayTask(PayEmpCxActivity.this);
                Map<String, String> result = alipay.payV2(payInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    /**
     * get the sign type we use. 获取签名方式
     *
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }


    //更新订单状态
    void updateMineOrder(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.UPDATE_ORDER_TOSERVER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo =  new JSONObject(s);
                                String code = jo.getString("code");
                                if (code.equals("200")) {
                                    Toast.makeText(PayEmpCxActivity.this, "支付成功！", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{
                                    showMsg(PayEmpCxActivity.this, jo.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(PayEmpCxActivity.this, "支付失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(PayEmpCxActivity.this, "支付失败！", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("out_trade_no",  out_trade_no);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        getRequestQueue().add(request);
    }
    //----------------微信---------------
    public void goToPayWeixin(final Order order){
        // 将该app注册到微信
        api.registerApp(InternetURL.WEIXIN_APPID);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.SEND_ORDER_TOSERVER_WX,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {

                            try {
                                JSONObject jo =  new JSONObject(s);
                                String code = jo.getString("code");
                                if (code.equals("200")) {
                                    WxPayObjData data = getGson().fromJson(s, WxPayObjData.class);
                                    //我们服务端已经生成订单，微信支付统一下单
                                    wxPayObj = data.getData();
                                    if(wxPayObj != null){
                                        xmlStr =wxPayObj.getXmlStr();
                                        out_trade_no = wxPayObj.getOut_trade_no();
                                    }
                                    // 启动一个线程
                                    new Thread(PayEmpCxActivity.this).start();
                                }else{
                                    showMsg(PayEmpCxActivity.this, jo.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(PayEmpCxActivity.this, "支付失败！", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(PayEmpCxActivity.this,  "支付失败！", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empid", order.getEmpid());
                params.put("payable_amount", order.getPayable_amount());
                params.put("status", order.getStatus());
                params.put("pay_status", order.getPay_status());
                params.put("trade_type", order.getTrade_type());
                params.put("is_dxk_order", order.getIs_dxk_order());
                params.put("order_cont", order.getOrder_cont());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        getRequestQueue().add(request);
    }
    public Map<String,String> decodeXml(String content) {
        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName=parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if("xml".equals(nodeName)==false){
                            xml.put(nodeName,parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            Log.e("orion", e.toString());
        }
        return null;
    }

    @Override
    public void run() {
        //微信服务端返回之后，调用这个方法
        String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
        //根据返回的字符串和url 调用post方法处理
        byte[] buf = Util.httpPost(url, xmlStr);
        //处理完成的值转换成字符串
        String content = new String(buf);
        //字符串处理
        Map<String,String> xmlMap=decodeXml(content);
        //构造微信的req对象
        PayReq req = new PayReq();
        //解析xml就可以获得我们需要的值
        req.appId			= xmlMap.get("appid");
        req.partnerId		=  xmlMap.get("mch_id");
        req.prepayId		= xmlMap.get("prepay_id");
        req.nonceStr		= xmlMap.get("nonce_str");
        req.packageValue			= " Sign=WXPay";
        req.timeStamp = String.valueOf(genTimeStamp());

        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
        //生成sign
        req.sign = genAppSign(signParams).toUpperCase();
        //吊起微信支付窗口
        api.sendReq(req);
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }
    StringBuffer sb=new StringBuffer();;

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(InternetURL.WX_API_KEY);//微信支付key

        this.sb.append("sign str\n"+sb.toString()+"\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes());
        return appSign;
    }


    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("pay_wx_success")) {
//                updateMineOrder();
            }
        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("pay_wx_success");
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }
}
