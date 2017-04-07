package com.lbins.meetlove.ui;

import android.content.IntentFilter;
import android.content.res.Resources;
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
import com.lbins.meetlove.R;
import com.lbins.meetlove.base.BaseActivity;
import com.lbins.meetlove.base.InternetURL;
import com.lbins.meetlove.receiver.SMSBroadcastReceiver;
import com.lbins.meetlove.util.StringUtil;
import com.lbins.meetlove.widget.CustomProgressDialog;
import org.json.JSONObject;

/**
 * Created by zhl on 2016/8/30.
 */
public class UpdateMobileActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private EditText mobile;
    private EditText card;
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
        setContentView(R.layout.update_mobile_activity);
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
        title.setText("手机号码修改");

        mobile = (EditText) this.findViewById(R.id.mobile);
        card = (EditText) this.findViewById(R.id.card);
        btn_card = (Button) this.findViewById(R.id.btn_card);
        btn_save = (Button) this.findViewById(R.id.btn_save);

        mobile.addTextChangedListener(watcher);
        card.addTextChangedListener(watcher);
        btn_card.setOnClickListener(this);
        btn_save.setOnClickListener(this);
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
            if(!StringUtil.isNullOrEmpty(mobile.getText().toString()) && !StringUtil.isNullOrEmpty(card.getText().toString())){
                btn_save.setBackground(getDrawable(R.drawable.btn_big_active));
            }else {
                btn_save.setBackground(getDrawable(R.drawable.btn_big_unactive));
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_card:
            {
                if(StringUtil.isNullOrEmpty(mobile.getText().toString())){
                    showMsg(UpdateMobileActivity.this, "请输入手机号码！");
                    return;
                }
                SMSSDK.getVerificationCode("86", mobile.getText().toString());//发送请求验证码，手机10s之内会获得短信验证码
                phString = mobile.getText().toString();
                btn_card.setClickable(false);//不可点击
                btn_card.setBackground(res.getDrawable(R.drawable.btn_short_unactive));
                MyTimer myTimer = new MyTimer(60000, 1000);
                myTimer.start();
            }
                break;
            case R.id.btn_save:
            {
                if(StringUtil.isNullOrEmpty(mobile.getText().toString())){
                    showMsg(UpdateMobileActivity.this, "请输入手机号码！");
                    return;
                }
                if(StringUtil.isNullOrEmpty(card.getText().toString())){
                    showMsg(UpdateMobileActivity.this, "请输入验证码！");
                    return;
                }
                progressDialog = new CustomProgressDialog(UpdateMobileActivity.this, "",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                SMSSDK.submitVerificationCode("86", phString, card.getText().toString());
            }
                break;
        }
    }
    class MyTimer extends CountDownTimer {

        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            btn_card.setText(res.getString(R.string.daojishi_three));
            btn_card.setClickable(true);//可点击
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
//                    Toast.makeText(getApplicationContext(), "提交验证码成功", Toast.LENGTH_SHORT).show();
//                    reg();

                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //已经验证
                    Toast.makeText(getApplicationContext(), R.string.code_msg_one, Toast.LENGTH_SHORT).show();
                }

            } else {
//				((Throwable) data).printStackTrace();
                Toast.makeText(UpdateMobileActivity.this, R.string.code_msg_two, Toast.LENGTH_SHORT).show();
//					Toast.makeText(MainActivity.this, "123", Toast.LENGTH_SHORT).show();
                int status = 0;
                try {
                    ((Throwable) data).printStackTrace();
                    Throwable throwable = (Throwable) data;

                    JSONObject object = new JSONObject(throwable.getMessage());
                    String des = object.optString("detail");
                    status = object.optInt("status");
                    if (!TextUtils.isEmpty(des)) {
                        Toast.makeText(UpdateMobileActivity.this, des, Toast.LENGTH_SHORT).show();
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

}
