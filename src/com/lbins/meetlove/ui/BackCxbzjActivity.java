package com.lbins.meetlove.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
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
import com.lbins.meetlove.util.StringUtil;
import com.lbins.meetlove.widget.CustomProgressDialog;
import com.lbins.meetlove.widget.ReportPopWindow;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 */
public class BackCxbzjActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private Button btn_1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.back_cxbzj_activity);
        initView();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("申请退还诚信保证金");
        btn_1 = (Button) this.findViewById(R.id.btn_1);
        btn_1.setOnClickListener(this);


        this.findViewById(R.id.btn_kfrx).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_1:
            {
                progressDialog = new CustomProgressDialog(BackCxbzjActivity.this, "请稍后",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                saveData();
            }
                break;

            case R.id.btn_kfrx:
            {
                //客服热线
                getTel();
            }
            break;
        }
    }

    private void saveData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appSaveApplyBack,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    showMsg(BackCxbzjActivity.this, "提交申请成功，请等待管理员审核！");
                                    finish();
                                } else {
                                    Toast.makeText(BackCxbzjActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(BackCxbzjActivity.this, "提交申请失败，请稍后重试！", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(BackCxbzjActivity.this, "提交申请失败，请稍后重试！", Toast.LENGTH_SHORT).show();
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
                                                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mm_tel));
                                                    BackCxbzjActivity.this.startActivity(intent);
//                                                    showMsgDialog(mm_tel);
                                                }
                                            }
                                        }
                                    }else {
                                        showMsg(BackCxbzjActivity.this, "暂无客服电话！");
                                    }
                                } else {
                                    Toast.makeText(BackCxbzjActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(BackCxbzjActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(BackCxbzjActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
        final Dialog picAddDialog = new Dialog(BackCxbzjActivity.this, R.style.dialog);
        View picAddInflate = View.inflate(this, R.layout.msg_tel_dialog, null);
        TextView btn_sure = (TextView) picAddInflate.findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mm_tel));
                    BackCxbzjActivity.this.startActivity(intent);
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
