package com.lbins.meetlove.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.meetlove.MeetLoveApplication;
import com.lbins.meetlove.R;
import com.lbins.meetlove.adapter.AnimateFirstDisplayListener;
import com.lbins.meetlove.base.BaseActivity;
import com.lbins.meetlove.base.InternetURL;
import com.lbins.meetlove.dao.Emp;
import com.lbins.meetlove.dao.Friends;
import com.lbins.meetlove.data.EmpData;
import com.lbins.meetlove.data.FriendsData;
import com.lbins.meetlove.data.HappyHandLikeData;
import com.lbins.meetlove.module.HappyHandLike;
import com.lbins.meetlove.util.StringUtil;
import com.lbins.meetlove.widget.CustomProgressDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 */
public class ProfileDetailJwdxActivity extends BaseActivity implements View.OnClickListener {
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    private TextView title;

    private ImageView cover;
    private TextView sign;
    private TextView nickname;
    private TextView age;
    private TextView heightl;
    private TextView education;
    private TextView address;
    private TextView marragie;
    private TextView company;
    private TextView likes;
    private TextView state;

    private TextView age_marry;
    private TextView heightl_marry;
    private TextView education_marry;
    private TextView marry_marry;

    private Button btn_login;

    private String empid;//注册成功返回的会员ID

    private TextView mobile;
    private Emp emp;

    private ImageView vip_1;
    private TextView vip_2;
    private ImageView vip_3;
    private TextView vip_4;

    private Resources res;

    private int isFriends = 0;//是否是好友   默认0否 1是

    private LinearLayout liner_jcjw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_detail_activity);
        res = getResources();

        empid =getIntent().getExtras().getString("empid");
        initView();
        progressDialog = new CustomProgressDialog(ProfileDetailJwdxActivity.this, "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getEmpById();
    }

    private void getEmpById() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appEmpByEmpId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    EmpData data = getGson().fromJson(s, EmpData.class);
                                    if(data != null){
                                        emp = data.getData();
                                        if(emp != null){
                                            initData();
                                        }
                                    }
                                }else {
                                    Toast.makeText(ProfileDetailJwdxActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
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
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empid", empid);
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


    //实例化
    private void initData() {
        if(!StringUtil.isNullOrEmpty(emp.getNickname())){
            if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("rzstate1", ""), String.class))){
                if("1".equals(getGson().fromJson(getSp().getString("rzstate1", ""), String.class))){
                    //进行身份认证了
                    nickname.setText(emp.getNickname());
                }else {
                    nickname.setBackgroundResource(R.drawable.btn_profile_check);
                }
            }else {
                nickname.setBackgroundResource(R.drawable.btn_profile_check);
            }
        }
        if(!StringUtil.isNullOrEmpty(emp.getCover())){
            imageLoader.displayImage(emp.getCover(), cover, MeetLoveApplication.txOptions, animateFirstListener);
        }else {
        }
        if(!StringUtil.isNullOrEmpty(emp.getSign())){
            sign.setText(emp.getSign());
        }
        if(!StringUtil.isNullOrEmpty(emp.getMobile())){
            if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("rzstate1", ""), String.class))){
                if("1".equals(getGson().fromJson(getSp().getString("rzstate1", ""), String.class))){
                    //进行身份认证了
                    mobile.setText(emp.getMobile());
                }else {
                    mobile.setBackgroundResource(R.drawable.btn_profile_check);
                }
            }else {
                mobile.setBackgroundResource(R.drawable.btn_profile_check);
            }
        }
        if(!StringUtil.isNullOrEmpty(emp.getState())){
           if("1".equals(emp.getState())){
               state.setText("单身");
           }
            if("2".equals(emp.getState())){
                state.setText("交往中");
            }
        }
        if(!StringUtil.isNullOrEmpty(emp.getAge())){
            age.setText(emp.getAge()+"年");
        }
        if(!StringUtil.isNullOrEmpty(emp.getHeightl())){
            heightl.setText(emp.getHeightl() +"CM");
        }
        if(!StringUtil.isNullOrEmpty(emp.getEducation())){
            switch (Integer.parseInt(emp.getEducation())){

                case 2:
                {
                    education.setText("专科以下");
                }
                break;
                case 3:
                {
                    education.setText("专科");
                }
                break;
                case 4:
                {
                    education.setText("本科");
                }
                break;
                case 5:
                {
                    education.setText("研究生及以上");
                }
                break;
            }
        }
        if(!StringUtil.isNullOrEmpty(emp.getCityName())){
            address.setText(emp.getCityName());
        }

        if(!StringUtil.isNullOrEmpty(emp.getMarriage())){
            switch (Integer.parseInt(emp.getMarriage())){
                case 1:
                {
                    marragie.setText("未婚");
                }
                break;
                case 2:
                {
                    marragie.setText("离异");
                }
                break;
                case 3:
                {
                    marragie.setText("丧偶");
                }
                break;
            }
        }
        if(!StringUtil.isNullOrEmpty(emp.getCompany())){
            if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("rzstate1", ""), String.class))){
                if("1".equals(getGson().fromJson(getSp().getString("rzstate1", ""), String.class))){
                    //进行身份认证了
                    company.setText(emp.getCompany());
                }else {
                    company.setBackgroundResource(R.drawable.btn_profile_check);
                }
            }else {
                company.setBackgroundResource(R.drawable.btn_profile_check);
            }
        }
        if(!StringUtil.isNullOrEmpty(emp.getLikeids())){
            getLikes();
        }

        if(!StringUtil.isNullOrEmpty(emp.getRzstate1())){
            if("1".equals(emp.getRzstate1())){
                //进行身份认证了
                vip_2.setTextColor(res.getColor(R.color.main_color));
            }else {
                vip_2.setTextColor(res.getColor(R.color.textColortwo));
            }
        }else {
            vip_2.setTextColor(res.getColor(R.color.textColortwo));
        }
        if(!StringUtil.isNullOrEmpty(emp.getRzstate2())){
            if("1".equals(emp.getRzstate2())){
                vip_1.setImageDrawable(res.getDrawable(R.drawable.icon_verify_id_enabled));
            }else {
                vip_1.setImageDrawable(res.getDrawable(R.drawable.icon_verify_id_disable));
            }
        }else {
            vip_1.setImageDrawable(res.getDrawable(R.drawable.icon_verify_id_disable));
        }
        if(!StringUtil.isNullOrEmpty(emp.getRzstate3())){
            if("1".equals(emp.getRzstate3())){
                //进行身份认证了
                vip_3.setImageDrawable(res.getDrawable(R.drawable.icon_verify_honesty_enabled));
                vip_4.setTextColor(res.getColor(R.color.main_color));
            }else {
                vip_3.setImageDrawable(res.getDrawable(R.drawable.icon_verify_honesty_disable));
                vip_4.setTextColor(res.getColor(R.color.textColortwo));
            }
        }else {
            vip_3.setImageDrawable(res.getDrawable(R.drawable.icon_verify_honesty_disable));
            vip_4.setTextColor(res.getColor(R.color.textColortwo));
        }

        //-------------择偶要求------------------
        if (!StringUtil.isNullOrEmpty(emp.getAgestart()) && !StringUtil.isNullOrEmpty(emp.getAgeend())){
            age_marry.setText(emp.getAgestart() +"-" + emp.getAgeend() + "年");

        }
        if (!StringUtil.isNullOrEmpty(emp.getHeightlstart()) && !StringUtil.isNullOrEmpty(emp.getHeightlend())){
            heightl_marry.setText(emp.getHeightlstart()+"-" + emp.getHeightlend()+"年");
        }

        if(!StringUtil.isNullOrEmpty(emp.getEducationm())){
            switch (Integer.parseInt(emp.getEducationm())){

                case 2:
                {
                    education_marry.setText("专科以下");
                }
                break;
                case 3:
                {
                    education_marry.setText("专科");
                }
                break;
                case 4:
                {
                    education_marry.setText("本科");
                }
                break;
                case 5:
                {
                    education_marry.setText("研究生及以上");
                }
                break;
            }
        }

        if(!StringUtil.isNullOrEmpty(emp.getMarriagem())){
            switch (Integer.parseInt(emp.getMarriagem())){
                case 1:
                {
                    marry_marry.setText("未婚");
                }
                break;
                case 2:
                {
                    marry_marry.setText("离异");
                }
                break;
                case 3:
                {
                    marry_marry.setText("丧偶");
                }
                break;
            }
        }


    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("个人详情");

        cover = (ImageView) this.findViewById(R.id.cover);
        sign = (TextView) this.findViewById(R.id.sign);
        nickname = (TextView)this.findViewById(R.id.nickname);
        age = (TextView) this.findViewById(R.id.age);
        heightl = (TextView) this.findViewById(R.id.heightl);
        education = (TextView) this.findViewById(R.id.education);
        address = (TextView) this.findViewById(R.id.address);
        marragie = (TextView) this.findViewById(R.id.marragie);
        likes = (TextView) this.findViewById(R.id.likes);
        company = (TextView) this.findViewById(R.id.company);
        mobile = (TextView) this.findViewById(R.id.mobile);
        state = (TextView) this.findViewById(R.id.state);

        age_marry = (TextView) this.findViewById(R.id.age_marry);
        heightl_marry = (TextView) this.findViewById(R.id.heightl_marry);
        education_marry = (TextView) this.findViewById(R.id.education_marry);
        marry_marry = (TextView) this.findViewById(R.id.marry_marry);

        btn_login = (Button) this.findViewById(R.id.btn_login);
        vip_1 = (ImageView) this.findViewById(R.id.vip_1);
        vip_2 = (TextView) this.findViewById(R.id.vip_2);
        vip_3 = (ImageView) this.findViewById(R.id.vip_3);
        vip_4 = (TextView) this.findViewById(R.id.vip_4);

        nickname.setOnClickListener(this);
        mobile.setOnClickListener(this);
        company.setOnClickListener(this);
        btn_login.setVisibility(View.GONE);

        liner_jcjw = (LinearLayout) this.findViewById(R.id.liner_jcjw);
        liner_jcjw.setVisibility(View.VISIBLE);
        liner_jcjw.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.nickname:
            {
                //姓名
                if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("rzstate1", ""), String.class))){
                    if("1".equals(getGson().fromJson(getSp().getString("rzstate1", ""), String.class))){
                        //进行身份认证了
                    }else {
                        showMsgDialog();
                    }
                }else {
                    showMsgDialog();
                }
            }
                break;
            case R.id.mobile:
            {
                //手机号
                if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("rzstate1", ""), String.class))){
                    if("1".equals(getGson().fromJson(getSp().getString("rzstate1", ""), String.class))){
                        //进行身份认证了
                    }else {
                        showMsgDialog();
                    }
                }else {
                    showMsgDialog();
                }
            }
                break;
            case R.id.company:
            {
                //工作单位
                if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("rzstate1", ""), String.class))){
                    if("1".equals(getGson().fromJson(getSp().getString("rzstate1", ""), String.class))){
                        //进行身份认证了
                    }else {
                        showMsgDialog();
                    }
                }else {
                    showMsgDialog();
                }
            }
                break;
            case R.id.liner_jcjw:
            {
                //解除交往关系
                showMsgDialogDel();
            }
                break;
        }
    }


    private List<HappyHandLike> likeLists = new ArrayList<>();//兴趣爱好集合

    private void getLikes(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appLikesBylikeIds,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    HappyHandLikeData data = getGson().fromJson(s, HappyHandLikeData.class);
                                    if(data != null){
                                        likeLists.clear();
                                        likeLists.addAll(data.getData());
                                        if(likeLists != null){
                                            String str = "";
                                            for(HappyHandLike happyHandLike:likeLists){
                                                if(happyHandLike != null){
                                                    str = str+ happyHandLike.getLikename()+",";
                                                }
                                            }
                                            if(str.length()>1){
                                                str = str.substring(0,str.length()-1);
                                            }
                                            likes.setText(str);
                                        }
                                    }
                                }  else {
                                    showMsg(ProfileDetailJwdxActivity.this,  jo.getString("message"));
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
                        Toast.makeText(ProfileDetailJwdxActivity.this, R.string.login_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("likeids", emp.getLikeids());
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


    private void showMsgDialog() {
        final Dialog picAddDialog = new Dialog(ProfileDetailJwdxActivity.this, R.style.dialog);
        View picAddInflate = View.inflate(this, R.layout.msg_dialog, null);
        TextView btn_sure = (TextView) picAddInflate.findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileDetailJwdxActivity.this, MineRenzhengActivity.class);
                startActivity(intent);
                picAddDialog.dismiss();
            }
        });

        //取消
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


    private void showMsgDialogDel() {
        final Dialog picAddDialog = new Dialog(ProfileDetailJwdxActivity.this, R.style.dialog);
        View picAddInflate = View.inflate(this, R.layout.msg_delete_jw_dialog, null);
        TextView btn_sure = (TextView) picAddInflate.findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new CustomProgressDialog(ProfileDetailJwdxActivity.this, "正在处理",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                deleteYq();
                picAddDialog.dismiss();
            }
        });

        //取消
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


    private void deleteYq() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appDeleteJiaowangDx,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    showMsg(ProfileDetailJwdxActivity.this, "解除交往关系成功！");
                                    save("state", "1");
                                    Intent intent1 = new Intent("update_jwdx_success");
                                    sendBroadcast(intent1);
                                    finish();
                                } else {
                                    Toast.makeText(ProfileDetailJwdxActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(ProfileDetailJwdxActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ProfileDetailJwdxActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empid2", empid);
                params.put("empid1", getGson().fromJson(getSp().getString("empid", ""), String.class));
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
