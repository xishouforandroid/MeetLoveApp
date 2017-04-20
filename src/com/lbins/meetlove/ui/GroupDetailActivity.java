package com.lbins.meetlove.ui;

import android.os.Bundle;
import android.view.View;
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
import com.lbins.meetlove.data.HappyHandGroupData;
import com.lbins.meetlove.data.HappyHandGroupDataSingle;
import com.lbins.meetlove.module.HappyHandGroup;
import com.lbins.meetlove.util.StringUtil;
import com.lbins.meetlove.widget.CustomProgressDialog;
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
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("电影群");
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
                //加群
            }
                break;
        }
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
            }
            if(!StringUtil.isNullOrEmpty(happyHandGroup.getContent())){
                content.setText(happyHandGroup.getContent());
            }
        }
    }
}
