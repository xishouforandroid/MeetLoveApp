package com.lbins.meetlove.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.meetlove.R;
import com.lbins.meetlove.base.BaseActivity;
import com.lbins.meetlove.base.InternetURL;
import com.lbins.meetlove.data.HappyHandMessageData;
import com.lbins.meetlove.data.MsgCountData;
import com.lbins.meetlove.module.*;
import com.lbins.meetlove.util.StringUtil;
import com.lbins.meetlove.widget.CustomProgressDialog;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 */
public class MineMsgActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private MsgCount msgCount;

    private TextView one_number;
    private TextView two_number;
    private TextView three_number;
    private TextView four_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_msg_activity);
        initView();
        progressDialog = new CustomProgressDialog(MineMsgActivity.this, "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getData();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("系统通知");

        this.findViewById(R.id.relate_one).setOnClickListener(this);
        this.findViewById(R.id.relate_two).setOnClickListener(this);
        this.findViewById(R.id.relate_three).setOnClickListener(this);
        this.findViewById(R.id.relate_four).setOnClickListener(this);

        one_number = (TextView) this.findViewById(R.id.one_number);
        two_number = (TextView) this.findViewById(R.id.two_number);
        three_number = (TextView) this.findViewById(R.id.three_number);
        four_number = (TextView) this.findViewById(R.id.four_number);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.relate_one:
            {
                //系统消息
                Intent intent = new Intent(MineMsgActivity.this, MessagesActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.relate_two:
            {
                //系统资讯
                Intent intent = new Intent(MineMsgActivity.this, NewsActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.relate_three:
            {
                //活动公告
                Intent intent = new Intent(MineMsgActivity.this, NoticesActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.relate_four:
            {
                //交往消息
                Intent intent = new Intent(MineMsgActivity.this, JwdxApplyActivity.class);
                startActivity(intent);
            }
                break;
        }
    }

    private void getData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appMsgAllList,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    MsgCountData data = getGson().fromJson(s, MsgCountData.class);
                                    if(data != null){
                                        msgCount = data.getData();
                                        if(msgCount != null){
                                            initData();
                                        }
                                    }
                                }else {
                                    Toast.makeText(MineMsgActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
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

    void initData(){
        if(msgCount != null){
            //系统消息
            List<HappyHandMessage> list1 = msgCount.getList1();
            if(list1 != null){
                if(list1.size() > 0){
                    one_number.setVisibility(View.VISIBLE);
                    one_number.setText(String.valueOf(list1.size()));
                }else {
                    one_number.setVisibility(View.GONE);
                }
            }else{
                one_number.setVisibility(View.GONE);
            }

            //系统资讯
            List<HappyHandNews> list2 = msgCount.getList2();
            if(list2 != null){
                if(list2.size() > 0){
                    two_number.setVisibility(View.VISIBLE);
                    two_number.setText(String.valueOf(list2.size()));
                }else {
                    two_number.setVisibility(View.GONE);
                }
            }else{
                two_number.setVisibility(View.GONE);
            }

            //活动公告
            List<HappyHandNotice> list3 = msgCount.getList3();
            if(list3 != null){
                if(list3.size() > 0){
                    three_number.setVisibility(View.VISIBLE);
                    three_number.setText(String.valueOf(list3.size()));
                }else {
                    three_number.setVisibility(View.GONE);
                }
            }else{
                three_number.setVisibility(View.GONE);
            }

            //交往消息
            List<HappyHandJw> list4 = msgCount.getList4();
            if(list4 != null){
                if(list4.size() > 0){
                    four_number.setVisibility(View.VISIBLE);
                    four_number.setText(String.valueOf(list4.size()));
                }else {
                    four_number.setVisibility(View.GONE);
                }
            }else{
                four_number.setVisibility(View.GONE);
            }
        }
    }



}
