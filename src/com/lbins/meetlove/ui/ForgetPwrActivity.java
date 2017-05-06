package com.lbins.meetlove.ui;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.meetlove.R;
import com.lbins.meetlove.base.BaseActivity;
import com.lbins.meetlove.base.InternetURL;
import com.lbins.meetlove.data.KefuTelData;
import com.lbins.meetlove.module.KefuTel;
import com.lbins.meetlove.receiver.SMSBroadcastReceiver;
import com.lbins.meetlove.util.StringUtil;
import com.lbins.meetlove.widget.CustomProgressDialog;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 */
public class ForgetPwrActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private EditText mobile;
    private EditText card;
    private EditText pwr2;
    private EditText pwr3;
    private Button btn_card;
    private Button btn_save;
    private Resources res;

    //mob短信
    // 填写从短信SDK应用后台注册得到的APPKEY
    private static String APPKEY = InternetURL.APP_MOB_KEY;//"69d6705af33d";0d786a4efe92bfab3d5717b9bc30a10d
    // 填写从短信SDK应用后台注册得到的APPSECRET
    private static String APPSECRET = InternetURL.APP_MOB_SCRECT;
    public String phString;//手机号码
    //短信读取
    private SMSBroadcastReceiver mSMSBroadcastReceiver;
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_pwr_activity);
        res = getResources();
        //mob短信无GUI
        SMSSDK.initSDK(this, APPKEY, APPSECRET, true);
        EventHandler eh = new EventHandler() {

            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                mHandler.sendMessage(msg);
            }

        };
        SMSSDK.registerEventHandler(eh);
        
        initView();

        //生成广播处理
        mSMSBroadcastReceiver = new SMSBroadcastReceiver();
        //实例化过滤器并设置要过滤的广播
        IntentFilter intentFilter = new IntentFilter(ACTION);
        intentFilter.setPriority(Integer.MAX_VALUE);
        //注册广播
        this.registerReceiver(mSMSBroadcastReceiver, intentFilter);
        mSMSBroadcastReceiver.setOnReceivedMessageListener(new SMSBroadcastReceiver.MessageListener() {
            @Override
            public void onReceived(String message) {
                //花木通的验证码：8469【掌淘科技】
                if (!StringUtil.isNullOrEmpty(message)) {
                    String codestr = StringUtil.valuteNumber(message);
                    if (!StringUtil.isNullOrEmpty(codestr)) {
                        card.setText(codestr);
                    }
                }
            }
        });
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("忘记密码");
        mobile = (EditText) this.findViewById(R.id.mobile);
        card = (EditText) this.findViewById(R.id.card);
        pwr2 = (EditText) this.findViewById(R.id.pwr2);
        pwr3 = (EditText) this.findViewById(R.id.pwr3);
        btn_card = (Button) this.findViewById(R.id.btn_card);
        btn_save = (Button) this.findViewById(R.id.btn_save);


        this.findViewById(R.id.btn_tel).setOnClickListener(this);
        pwr2.addTextChangedListener(watcher);
        pwr3.addTextChangedListener(watcher);
        mobile.addTextChangedListener(watcher);
        card.addTextChangedListener(watcher);
        btn_save.setOnClickListener(this);
        btn_card.setOnClickListener(this);
    }

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(!StringUtil.isNullOrEmpty(mobile.getText().toString())
                    && !StringUtil.isNullOrEmpty(card.getText().toString())
                    && !StringUtil.isNullOrEmpty(pwr2.getText().toString())
                    && !StringUtil.isNullOrEmpty(pwr3.getText().toString())
                    && pwr2.getText().toString().equals(pwr3.getText().toString())){
                btn_save.setBackgroundResource(R.drawable.btn_big_active);
                btn_save.setTextColor(getResources().getColor(R.color.white));
            }else {
                btn_save.setBackgroundResource(R.drawable.btn_big_unactive);
                btn_save.setTextColor(getResources().getColor(R.color.textColortwo));
            }
        }
    };


    class MyTimer extends CountDownTimer {

        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            btn_card.setText(res.getString(R.string.daojishi_three));
            btn_card.setClickable(true);//可点击
            btn_card.setBackgroundResource(R.drawable.btn_short_active);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btn_card.setText(res.getString(R.string.daojishi_one) + millisUntilFinished / 1000 + res.getString(R.string.daojishi_two));
        }
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            Log.e("event", "event=" + event);
            if (result == SMSSDK.RESULT_COMPLETE) {
                System.out.println("--------result" + event);
                //短信注册成功后，返回MainActivity,然后提示新好友
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                    saveData();
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //已经验证
//                    Toast.makeText(getApplicationContext(), R.string.code_msg_one, Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(ForgetPwrActivity.this, R.string.code_msg_two, Toast.LENGTH_SHORT).show();
                int status = 0;
                try {
                    ((Throwable) data).printStackTrace();
                    Throwable throwable = (Throwable) data;

                    JSONObject object = new JSONObject(throwable.getMessage());
                    String des = object.optString("detail");
                    status = object.optInt("status");
                    if (!TextUtils.isEmpty(des)) {
                        Toast.makeText(ForgetPwrActivity.this, des, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    SMSLog.getInstance().w(e);
                }
            }
            if(progressDialog != null){
                progressDialog.dismiss();
            }
        }

        ;
    };

    public void onDestroy() {
        super.onPause();
        SMSSDK.unregisterAllEventHandler();
        //注销短信监听广播
        this.unregisterReceiver(mSMSBroadcastReceiver);
    }
    
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_tel:
            {
                //联系我们
                getTel();
            }
                break;
            case R.id.btn_card:
            {
                //验证码 
                if(StringUtil.isNullOrEmpty(mobile.getText().toString())){
                     showMsg(ForgetPwrActivity.this, "请输入手机号码！");
                return;
            }
            SMSSDK.getVerificationCode("86", mobile.getText().toString());//发送请求验证码，手机10s之内会获得短信验证码
            phString = mobile.getText().toString();
            btn_card.setClickable(false);//不可点击
            btn_card.setBackgroundResource(R.drawable.btn_short_unactive);
            MyTimer myTimer = new MyTimer(60000, 1000);
            myTimer.start();
                
            }
                break;
            case R.id.btn_save:
            {
                //保存
                if(StringUtil.isNullOrEmpty(mobile.getText().toString())){
                    showMsg(ForgetPwrActivity.this, "请输入手机号码！");
                return;
                }
                if(StringUtil.isNullOrEmpty(card.getText().toString())){
                    showMsg(ForgetPwrActivity.this, "请输入验证码！");
                    return;
                } if(StringUtil.isNullOrEmpty(pwr2.getText().toString())){
                    showMsg(ForgetPwrActivity.this, "请输入新密码！");
                    return;
                }
                if(pwr2.getText().toString().length() <6 || pwr2.getText().toString().length() > 18){
                    showMsg(ForgetPwrActivity.this, "新密码长度在6到18位之间！");
                    return;
                }
                if(StringUtil.isNullOrEmpty(pwr3.getText().toString())){
                    showMsg(ForgetPwrActivity.this, "请再次输入确认密码！");
                    return;
                }
                if(!pwr2.getText().toString().equals(pwr3.getText().toString())){
                    showMsg(ForgetPwrActivity.this, "两次输入密码不一致！");
                    return;
                }
                progressDialog = new CustomProgressDialog(ForgetPwrActivity.this, "",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                SMSSDK.submitVerificationCode("86", phString, card.getText().toString());
            }
                break;
        }
    }

    private void saveData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appUpdatePwrByMobile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    showMsg(ForgetPwrActivity.this, "修改密码成功！");
                                    save("password", pwr2.getText().toString());
                                    finish();
                                } else {
                                    Toast.makeText(ForgetPwrActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(ForgetPwrActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                        Toast.makeText(ForgetPwrActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("password", pwr2.getText().toString());
                params.put("mobile", mobile.getText().toString());
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

    private void getTel() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appTel,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    KefuTelData data = getGson().fromJson(s, KefuTelData.class);
                                    if(data != null){
                                        List<KefuTel> tels = data.getData();
                                        if(tels != null && tels.size()>0){
                                            KefuTel kefuTel = tels.get(0);
                                            if(kefuTel != null){
                                                String mm_tel = kefuTel.getMm_tel();
                                                if(!StringUtil.isNullOrEmpty(mm_tel)){
                                                    showMsgDialog(mm_tel);
                                                }
                                            }
                                        }
                                    }else {
                                        showMsg(ForgetPwrActivity.this, "暂无客服电话！");
                                    }
                                } else {
                                    Toast.makeText(ForgetPwrActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(ForgetPwrActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                        Toast.makeText(ForgetPwrActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
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


    private void showMsgDialog(final String mm_tel) {
        final Dialog picAddDialog = new Dialog(ForgetPwrActivity.this, R.style.dialog);
        View picAddInflate = View.inflate(this, R.layout.msg_tel_dialog, null);
        TextView btn_sure = (TextView) picAddInflate.findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mm_tel));
                ForgetPwrActivity.this.startActivity(intent);
                picAddDialog.dismiss();
            }
        });

        TextView btn_cancel = (TextView) picAddInflate.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picAddDialog.dismiss();
            }
        });
        picAddDialog.setContentView(picAddInflate);
        picAddDialog.show();
    }

}
