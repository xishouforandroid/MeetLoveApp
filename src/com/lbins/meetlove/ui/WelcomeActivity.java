package com.lbins.meetlove.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.lbins.meetlove.MainActivity;
import com.lbins.meetlove.MeetLoveApplication;
import com.lbins.meetlove.R;
import com.lbins.meetlove.baidu.Utils;
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
            boolean isFirstRun = getSp().getBoolean("isFirstRun", true);
            if (isFirstRun) {
                SharedPreferences.Editor editor = getSp().edit();
                editor.putBoolean("isFirstRun", false);
                editor.commit();
                Intent loadIntent = new Intent(WelcomeActivity.this, AboutActivity.class);
                startActivity(loadIntent);
                finish();
            } else {
                if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("mobile", ""), String.class)) && !StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("password", ""), String.class))){
                    //查看是否有用户  是否使用用户----0否 1是 2尚未维护资料
                    if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("empid",""), String.class)) && !StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("is_use",""), String.class)) ){
                        if("2".equals(getGson().fromJson(getSp().getString("is_use",""), String.class))){
                            //尚未完善资料
                            Intent intent =  new Intent(WelcomeActivity.this, RegUpdateActivity.class);
                            intent.putExtra("empid", getGson().fromJson(getSp().getString("empid",""), String.class));
                            startActivity(intent);
                            finish();
                        }
                        else if("0".equals(getGson().fromJson(getSp().getString("is_use",""), String.class))){
                            //用户被禁用
                            showMsg(WelcomeActivity.this, "该用户已被禁用，请联系客服！");
                        }else{
                            loginData();
                        }
                    }else {
                        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

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
                                    finish();
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
                        finish();
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
        PushManager.startWork(getApplicationContext(),
                PushConstants.LOGIN_TYPE_API_KEY,
               Utils.getMetaValue(WelcomeActivity.this, "api_key"));

        save("empid", emp.getEmpid());
        save("mobile", emp.getMobile());
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
        save("pname", emp.getPname());
        save("cityName", emp.getCityName());

        save("chooseid", emp.getChooseid());
        save("agestart", emp.getAgestart());
        save("ageend", emp.getAgeend());
        save("heightlstart", emp.getHeightlstart());
        save("heightlend", emp.getHeightlend());
        save("educationm", emp.getEducationm());
        save("marriagem", emp.getMarriagem());


        EMClient.getInstance().login(emp.getEmpid(), "123456", new EMCallBack() {
            @Override
            public void onSuccess() {
                // ** manually load all local groups and conversation
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();

                // update current user's display name for APNs
                boolean updatenick = EMClient.getInstance().pushManager().updatePushNickname(
                        MeetLoveApplication.currentUserNick.trim());
                if (!updatenick) {
                    Log.e("LoginActivity", "update current user nick fail");
                }

                // get user's info (this should be get from App's server or 3rd party service)
//                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();

                Intent intent = new Intent(WelcomeActivity.this,
                        MainActivity.class);
                startActivity(intent);

                finish();
            }

            @Override
            public void onError(int i, String s) {
                Intent intent = new Intent(WelcomeActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });

    }


}
