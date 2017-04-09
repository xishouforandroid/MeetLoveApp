package com.lbins.meetlove.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.meetlove.MainActivity;
import com.lbins.meetlove.R;
import com.lbins.meetlove.adapter.AnimateFirstDisplayListener;
import com.lbins.meetlove.base.BaseActivity;
import com.lbins.meetlove.base.InternetURL;
import com.lbins.meetlove.data.EmpData;
import com.lbins.meetlove.module.Emp;
import com.lbins.meetlove.util.GuirenHttpUtils;
import com.lbins.meetlove.util.StringUtil;
import com.lbins.meetlove.widget.CustomProgressDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText mobile;
    private EditText pwr;

    private Button btn_login;
    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        res = getResources();
//        DemoHelper.getInstance().logout(false, new EMCallBack() {
//            @Override
//            public void onSuccess() {
//            }
//            @Override
//            public void onError(int i, String s) {
//            }
//            @Override
//            public void onProgress(int i, String s) {
//            }
//        });
        registerBoradcastReceiver();
        initView();
        //查看是否有用户  是否使用用户----0否 1是 2尚未维护资料
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("empid",""), String.class)) && !StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("is_use",""), String.class)) ){
            if("2".equals(getGson().fromJson(getSp().getString("is_use",""), String.class))){
                //尚未完善资料
                Intent intent =  new Intent(LoginActivity.this, RegUpdateActivity.class);
                intent.putExtra("empid", getGson().fromJson(getSp().getString("empid",""), String.class));
                startActivity(intent);
            }
            if("0".equals(getGson().fromJson(getSp().getString("is_use",""), String.class))){
                //用户被禁用
                showMsg(LoginActivity.this, "该用户已被禁用，请联系客服！");
            }
        }

        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("mobile", ""), String.class)) ){
            mobile.setText(getGson().fromJson(getSp().getString("mobile", ""), String.class));
        }if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("password", ""), String.class))){
            pwr.setText(getGson().fromJson(getSp().getString("password", ""), String.class));
        }

    }

    private void initView() {
        mobile = (EditText) this.findViewById(R.id.mobile);
        pwr = (EditText) this.findViewById(R.id.pwr);
        btn_login = (Button) this.findViewById(R.id.btn_login);

        this.findViewById(R.id.btn_reg).setOnClickListener(this);
        this.findViewById(R.id.btn_find).setOnClickListener(this);
        btn_login.setOnClickListener(this);
        this.findViewById(R.id.back).setVisibility(View.GONE);
        this.findViewById(R.id.btn_fwtk).setOnClickListener(this);
        this.findViewById(R.id.btn_ysbh).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.GONE);

        //设置监听  随时更改注册按钮的状态
        mobile.addTextChangedListener(watcher);
        pwr.addTextChangedListener(watcher);
        btn_login.setBackground(res.getDrawable(R.drawable.btn_big_unactive));

    }

    boolean isMobileNet, isWifiNet;

    @Override
    public void onClick(View v) {
        try {
            isMobileNet = GuirenHttpUtils.isMobileDataEnable(getApplicationContext());
            isWifiNet = GuirenHttpUtils.isWifiDataEnable(getApplicationContext());
            if (!isMobileNet && !isWifiNet) {
                Toast.makeText(this, R.string.net_work_error, Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (v.getId()){
            case R.id.btn_reg:
                //注册
            {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.btn_find:
                //找回密码
            {
                Intent intent = new Intent(LoginActivity.this, ForgetPwrActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.btn_login:
                //登录
            {
                if (StringUtil.isNullOrEmpty(mobile.getText().toString())) {
                    showMsg(LoginActivity.this, res.getString(R.string.error_login_one));
                    return;
                }
                if(mobile.getText().toString().length() != 11 ){
                    showMsg(LoginActivity.this, "请检查手机号是否正确");
                    return;
                }
                if (StringUtil.isNullOrEmpty(pwr.getText().toString())) {
                    showMsg(LoginActivity.this, res.getString(R.string.error_login_two));
                    return;
                }
                progressDialog = new CustomProgressDialog(LoginActivity.this, "正在加载中",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                loginData();
            }
                break;
            case R.id.btn_fwtk:
            {
                //服务条款
                Intent intent = new Intent(LoginActivity.this, FutkActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.btn_ysbh:
            {
                //隐私保护
                Intent intent = new Intent(LoginActivity.this, YsbhActivity.class);
                startActivity(intent);
            }
                break;
        }
    }


    private void loginData(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appLogin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    EmpData data = getGson().fromJson(s, EmpData.class);
                                    saveAccount(data.getData());
                                }  else {
                                    showMsg(LoginActivity.this,  jo.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(LoginActivity.this, R.string.login_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", mobile.getText().toString());
                params.put("password", pwr.getText().toString());
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

    public void saveAccount(final Emp emp) {
        save("empid", emp.getEmpid());
        save("password", pwr.getText().toString());
        save("mobile", emp.getPassword());
        save("nickname", emp.getNickname());
        save("cover", emp.getCover());
        save("sign", emp.getSign());
        save("age", emp.getAge());
        save("sex", emp.getSex());
        save("heightl", emp.getHeightl());
        save("education", emp.getEducation());
        save("provinceid", emp.getProvinceid());
        save("cityid", emp.getCityid());
        save("areaid", emp.getAreaid());
        save("marriage", emp.getMarriage());
        save("company", emp.getCompany());
        save("likeids", emp.getLikeids());
        save("state", emp.getState());
        save("cardpic", emp.getCardpic());
        save("rzstate1", emp.getRzstate1());
        save("rzstate2", emp.getRzstate2());
        save("rzstate3", emp.getRzstate3());
        save("is_use", emp.getIs_use());
        Intent intent = new Intent(LoginActivity.this,
                                MainActivity.class);
                        startActivity(intent);
        finish();
        //登录成功，绑定百度云推送
//        if (StringUtil.isNullOrEmpty(emp.getUserId())) {
            //进行绑定
//            PushManager.startWork(getApplicationContext(),
//                    PushConstants.LOGIN_TYPE_API_KEY,
//                    Utils.getMetaValue(LoginActivity.this, "api_key"));
//        } else {
////            如果已经绑定，就保存
//            save("userId", emp.getUserId());
//        }

        // 登陆成功，保存用户名密码
//        save("mm_emp_id", emp.getMm_emp_id());

//        MeetLoveApplication.currentCover = emp.getMm_emp_cover();
//        MeetLoveApplication.currentName = emp.getMm_emp_nickname();
//
//        Emp emp1 = DBHelper.getInstance(LoginActivity.this).getEmpByEmpId(emp.getMm_emp_id());
//        if(emp1 != null){
//            //说明存在这个用户了
//        }else{
//            //不存在该用户 可以保存到数据库
//            DBHelper.getInstance(LoginActivity.this).saveEmp(emp);
//        }
//
//        // close it before login to make sure DemoDB not overlap
//        DemoDBManager.getInstance().closeDB();
//
//        // reset current user name before login
//        DemoHelper.getInstance().setCurrentUserName(emp.getHxusername());
//        final long start = System.currentTimeMillis();
//        // call login method
//        EMClient.getInstance().login(emp.getHxusername(), "111111", new EMCallBack() {
//
//            @Override
//            public void onSuccess() {
//
//                // ** manually load all local groups and conversation
//                EMClient.getInstance().groupManager().loadAllGroups();
//                EMClient.getInstance().chatManager().loadAllConversations();
//
//                // update current user's display name for APNs
//                boolean updatenick = EMClient.getInstance().updateCurrentUserNick(emp.getMm_emp_nickname());
//                if (!updatenick) {
////                    Log.e("LoginActivity", "update current user nick fail");
//                }
//
//                // get user's info (this should be get from App's server or 3rd party service)
//                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
//
//                Intent intent = new Intent(LoginActivity.this,
//                        MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//
//            @Override
//            public void onProgress(int progress, String status) {
//            }
//
//            @Override
//            public void onError(final int code, final String message) {
//                runOnUiThread(new Runnable() {
//                    public void run() {
////                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
////                                Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(LoginActivity.this,
//                                MainActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                });
//            }
//        });
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
            if(!StringUtil.isNullOrEmpty(mobile.getText().toString()) && !StringUtil.isNullOrEmpty(pwr.getText().toString())){
                //都不是空的
                if(mobile.getText().toString().length() == 11 && pwr.getText().toString().length() > 5 && pwr.getText().toString().length()<19){
                    //手机号是11位 两次输入密码一致 密码大于6位小于18位
                    btn_login.setBackground(getDrawable(R.drawable.btn_big_active));
                }else {
                    btn_login.setBackground(getDrawable(R.drawable.btn_big_unactive));
                }
            }
        }
    };

    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("reg_success_guiren")){
                if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("mobile", ""), String.class)) ){
                    mobile.setText(getGson().fromJson(getSp().getString("mobile", ""), String.class));
                }if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("password", ""), String.class))){
                    pwr.setText(getGson().fromJson(getSp().getString("password", ""), String.class));
                }
            }
        }

    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("reg_success_guiren");
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

}
