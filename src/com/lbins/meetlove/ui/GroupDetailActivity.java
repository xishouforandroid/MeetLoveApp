package com.lbins.meetlove.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hyphenate.chat.EMConversation;
import com.lbins.meetlove.MeetLoveApplication;
import com.lbins.meetlove.R;
import com.lbins.meetlove.adapter.AnimateFirstDisplayListener;
import com.lbins.meetlove.base.BaseActivity;
import com.lbins.meetlove.base.InternetURL;
import com.lbins.meetlove.chat.Constant;
import com.lbins.meetlove.chat.ui.ChatActivity;
import com.lbins.meetlove.dao.DBHelper;
import com.lbins.meetlove.dao.HappyHandGroup;
import com.lbins.meetlove.data.HappyHandGroupDataSingle;
import com.lbins.meetlove.util.StringUtil;
import com.lbins.meetlove.widget.CustomProgressDialog;
import com.lbins.meetlove.widget.SelectPhotoPopWindow;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 */
public class GroupDetailActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private ImageView cover;
    private TextView name;
    private TextView content;
    private Button btn_1;

    private String groupid;
    private HappyHandGroup happyHandGroup;

    private String flag = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_detail_activity);
        groupid = getIntent().getExtras().getString("groupid");

        initView();
        progressDialog = new CustomProgressDialog(GroupDetailActivity.this, "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getDetailGroups();
        //判断用户是否已经加入群聊
        isGroups();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("群聊");
        cover = (ImageView) this.findViewById(R.id.cover);
        name = (TextView) this.findViewById(R.id.name);
        content = (TextView) this.findViewById(R.id.content);
        btn_1 = (Button) this.findViewById(R.id.btn_1);
        btn_1.setOnClickListener(this);
    }

    private void getDetailGroups() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appGroupsById,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    HappyHandGroupDataSingle data = getGson().fromJson(s, HappyHandGroupDataSingle.class);
                                    if(data != null){
                                        happyHandGroup = data.getData();
                                        if(happyHandGroup != null){
                                            initData();
                                        }
                                    }
                                }else {
                                    Toast.makeText(GroupDetailActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("groupid", groupid);
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


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_1:
            {
                if(InternetURL.DEFAULT_GROUPS_ID1.equals(happyHandGroup.getGroupid())){
                    //如果是沈阳用户交流群 只允许身份认证之后的进入该群
                    //身份认证
                    if("1".equals(getGson().fromJson(getSp().getString("rzstate1", ""), String.class))){
                        //身份认证了
                        progressDialog = new CustomProgressDialog(GroupDetailActivity.this, "请稍后",R.anim.custom_dialog_frame);
                        progressDialog.setCancelable(true);
                        progressDialog.setIndeterminate(true);
                        progressDialog.show();
                        saveG();
                    }else {
                        //未认证
                        showMsgDialog();
                    }
                }else if(InternetURL.DEFAULT_GROUPS_ID2.equals(happyHandGroup.getGroupid())){
                    //沈阳情侣群
                    if("2".equals(getGson().fromJson(getSp().getString("state", ""), String.class))){
                        //只有交往中的才能进入该群
                        progressDialog = new CustomProgressDialog(GroupDetailActivity.this, "请稍后",R.anim.custom_dialog_frame);
                        progressDialog.setCancelable(true);
                        progressDialog.setIndeterminate(true);
                        progressDialog.show();
                        saveG();
                    }else {
                        //未认证
                        showMsg(GroupDetailActivity.this, "您暂无交往对象，不能进入该群");
                    }
                }else{
                    //加群
                    if("1".equals(flag)){
                        //发消息
                        Intent intent = new Intent(GroupDetailActivity.this, ChatActivity.class);
                        intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                        intent.putExtra(Constant.EXTRA_USER_ID, groupid);
                        startActivity(intent);
                    }else  if("0".equals(flag)){
                        //身份认证
                        if("1".equals(getGson().fromJson(getSp().getString("rzstate1", ""), String.class))){
                            //身份认证了
                            progressDialog = new CustomProgressDialog(GroupDetailActivity.this, "请稍后",R.anim.custom_dialog_frame);
                            progressDialog.setCancelable(true);
                            progressDialog.setIndeterminate(true);
                            progressDialog.show();
                            saveG();
                        }else {
                            //未认证
                            showMsgDialog();
                        }
                    }
                }

            }
                break;
        }
    }

    private void showMsgDialog() {
        final Dialog picAddDialog = new Dialog(GroupDetailActivity.this, R.style.dialog);
        View picAddInflate = View.inflate(this, R.layout.msg_dialog, null);
        TextView btn_sure = (TextView) picAddInflate.findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupDetailActivity.this, MineRenzhengActivity.class);
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

    private void saveG() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appEmpGroupsSave,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    showMsg(GroupDetailActivity.this, "加群成功！");
                                    // start chat acitivity
                                    Intent intent = new Intent(GroupDetailActivity.this, ChatActivity.class);
                                    intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                                    intent.putExtra(Constant.EXTRA_USER_ID, groupid);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(GroupDetailActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("groupid", groupid);
                params.put("empid", getGson().fromJson(getSp().getString("empid", ""), String.class));
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



    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    void initData(){
        if(happyHandGroup != null){
            if(!StringUtil.isNullOrEmpty(happyHandGroup.getPic())){
                imageLoader.displayImage(happyHandGroup.getPic(), cover, MeetLoveApplication.options, animateFirstListener);
            }
            if(!StringUtil.isNullOrEmpty(happyHandGroup.getTitle())){
                name.setText(happyHandGroup.getTitle());
                title.setText(happyHandGroup.getTitle());
            }
            if(!StringUtil.isNullOrEmpty(happyHandGroup.getContent())){
                content.setText(happyHandGroup.getContent());
            }
            DBHelper.getInstance(GroupDetailActivity.this).saveHappyHandGroup(happyHandGroup);
        }
    }



    private void isGroups() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appEmpIsExist,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    //已经加入群聊
                                    flag = "1";
                                    btn_1.setText("发消息");
                                }else {
                                    //尚未加入群聊
                                    flag = "0";
                                    btn_1.setText("加群");
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
                params.put("groupid", groupid);
                params.put("empid", getGson().fromJson(getSp().getString("empid", ""), String.class));
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
