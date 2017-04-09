package com.lbins.meetlove.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.meetlove.R;
import com.lbins.meetlove.base.BaseActivity;
import com.lbins.meetlove.base.InternetURL;
import com.lbins.meetlove.data.Data;
import com.lbins.meetlove.data.EmpData;
import com.lbins.meetlove.util.StringUtil;
import com.lbins.meetlove.widget.CustomProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private EditText mobile;
    private EditText pwr;
    private EditText pwrsure;
    private Button btn_login;
    private Resources res;
    private TextView sex_man;
    private TextView sex_woman;

    private LinearLayout sex_liner;

    private String sex = "1";//0女 1男

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        res = getResources();
        initView();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("注册");
        this.findViewById(R.id.btn_fwtk).setOnClickListener(this);
        this.findViewById(R.id.btn_ysbh).setOnClickListener(this);
        mobile = (EditText) this.findViewById(R.id.mobile);
        pwr = (EditText) this.findViewById(R.id.pwr);
        pwrsure = (EditText) this.findViewById(R.id.pwrsure);
        btn_login = (Button) this.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        sex_man = (TextView) this.findViewById(R.id.sex_man);
        sex_woman = (TextView) this.findViewById(R.id.sex_woman);
        sex_man.setOnClickListener(this);
        sex_woman.setOnClickListener(this);
        sex_liner = (LinearLayout) this.findViewById(R.id.sex_liner);


        //设置监听  随时更改注册按钮的状态
        mobile.addTextChangedListener(watcher);
        pwr.addTextChangedListener(watcher);
        pwrsure.addTextChangedListener(watcher);

        btn_login.setBackground(res.getDrawable(R.drawable.btn_big_unactive));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_fwtk:
            {
                //服务条款
                Intent intent = new Intent(RegisterActivity.this, FutkActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.btn_ysbh:
            {
                //隐私保护
                Intent intent = new Intent(RegisterActivity.this, YsbhActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.btn_login:
            {
                //点击注册
                if(StringUtil.isNullOrEmpty(mobile.getText().toString())){
                    showMsg(RegisterActivity.this, res.getString(R.string.error_login_one));
                    return;
                }
                if(StringUtil.isNullOrEmpty(pwr.getText().toString())){
                    showMsg(RegisterActivity.this, res.getString(R.string.error_login_two));
                    return;
                }
                if(pwr.getText().toString().length() > 18 || pwr.getText().toString().length()<6){
                    showMsg(RegisterActivity.this, res.getString(R.string.error_pwr_six_eighteen));
                    return;
                }
                if(StringUtil.isNullOrEmpty(pwrsure.getText().toString())){
                    showMsg(RegisterActivity.this, res.getString(R.string.error_pwr_again));
                    return;
                }
                if(!pwr.getText().toString().equals(pwrsure.getText().toString())){
                    showMsg(RegisterActivity.this, res.getString(R.string.error_pwr_two_no));
                    return;
                }

                progressDialog = new CustomProgressDialog(RegisterActivity.this, "正在注册",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                reg();
            }
                break;
            case R.id.sex_man:
            {
                //男
                sex_liner.setBackground(res.getDrawable(R.drawable.btn_sex_left));
                sex_man.setTextColor(res.getColor(R.color.white));
                sex_woman.setTextColor(res.getColor(R.color.main_color));
                sex = "1";
            }
                break;
            case R.id.sex_woman:
            {
                //女
                sex_liner.setBackground(res.getDrawable(R.drawable.btn_sex_right));
                sex_man.setTextColor(res.getColor(R.color.main_color));
                sex_woman.setTextColor(res.getColor(R.color.white));
                sex = "0";
            }
                break;
        }
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
            if(!StringUtil.isNullOrEmpty(mobile.getText().toString()) && !StringUtil.isNullOrEmpty(pwr.getText().toString()) && !StringUtil.isNullOrEmpty(pwrsure.getText().toString())){
                //都不是空的
                if(mobile.getText().toString().length() == 11 && pwr.getText().toString().equals(pwrsure.getText().toString()) && pwr.getText().toString().length() > 5 && pwr.getText().toString().length()<19){
                    //手机号是11位 两次输入密码一致 密码大于6位小于18位
                    btn_login.setBackground(getDrawable(R.drawable.btn_big_active));
                }else {
                    btn_login.setBackground(getDrawable(R.drawable.btn_big_unactive));
                }
            }
        }
    };

    private void reg(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appReg,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    Intent intent =  new Intent(RegisterActivity.this, RegUpdateActivity.class);
                                    startActivity(intent);
                                }  else {
                                    showMsg(RegisterActivity.this,  jo.getString("message"));
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
                        Toast.makeText(RegisterActivity.this, "注册失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", mobile.getText().toString());
                params.put("password", pwr.getText().toString());
                params.put("sex", sex);
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

}
