package com.lbins.meetlove.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.meetlove.MainActivity;
import com.lbins.meetlove.R;
import com.lbins.meetlove.base.BaseActivity;
import com.lbins.meetlove.base.InternetURL;
import com.lbins.meetlove.data.EmpData;
import com.lbins.meetlove.module.Emp;
import com.lbins.meetlove.util.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhl on 2016/5/6.
 */
public class WelcomeActivity extends BaseActivity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        // 启动一个线程
        new Thread(WelcomeActivity.this).start();
    }


    @Override
    public void run() {
        try {
            // 3秒后跳转到登录界面
            Thread.sleep(1500);
            SharedPreferences.Editor editor = getSp().edit();
            boolean isFirstRun = getSp().getBoolean("isFirstRun", true);
            if (isFirstRun) {
                editor.putBoolean("isFirstRun", false);
                editor.commit();
                Intent loadIntent = new Intent(WelcomeActivity.this, AboutActivity.class);
                startActivity(loadIntent);
                finish();
            } else {
                if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("mobile", ""), String.class)) && !StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("password", ""), String.class))){
                    loginData();
                }else{
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
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
                                    EmpData data = getGson().fromJson(s,EmpData.class);
                                    saveAccount(data.getData());
                                } else {
                                    showMsg(WelcomeActivity.this, jo.getString("message"));
                                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                                    startActivity(intent);
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
                        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(WelcomeActivity.this, "登录失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", getGson().fromJson(getSp().getString("mobile", ""), String.class));
                params.put("password", getGson().fromJson(getSp().getString("password", ""), String.class));
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
        //登录成功，绑定百度云推送
//        if (StringUtil.isNullOrEmpty(emp.getUserId())) {
            //进行绑定
//            PushManager.startWork(getApplicationContext(),
//                    PushConstants.LOGIN_TYPE_API_KEY,
//                    Utils.getMetaValue(WelcomeActivity.this, "api_key"));
//        } else {
//            //如果已经绑定，就保存
//            save("userId", emp.getUserId());
//        }

        // 登陆成功，保存用户名密码
//        save("mm_emp_id", emp.getMm_emp_id());

        Intent intent = new Intent(WelcomeActivity.this,
                        MainActivity.class);
        startActivity(intent);
        finish();

    }


}
