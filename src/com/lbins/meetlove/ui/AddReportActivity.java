package com.lbins.meetlove.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
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
import com.lbins.meetlove.data.EmpData;
import com.lbins.meetlove.data.KefuTelData;
import com.lbins.meetlove.module.KefuTel;
import com.lbins.meetlove.util.StringUtil;
import com.lbins.meetlove.widget.CustomProgressDialog;
import com.lbins.meetlove.widget.QuitePopWindow;
import com.lbins.meetlove.widget.ReportPopWindow;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 */
public class AddReportActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private EditText nickname;
    private EditText content;
    private Button btn_1;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_report_activity);
        name = getIntent().getExtras().getString("name");
        initView();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("投诉");
        nickname = (EditText) this.findViewById(R.id.nickname);
        if(!StringUtil.isNullOrEmpty(name)){
            nickname.setText(name);
        }
        content = (EditText) this.findViewById(R.id.content);
        btn_1 = (Button) this.findViewById(R.id.btn_1);
        btn_1.setOnClickListener(this);

        //设置监听  随时更改注册按钮的状态
        content.addTextChangedListener(watcher);
        nickname.addTextChangedListener(watcher);

        this.findViewById(R.id.btn_tsxz).setOnClickListener(this);
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
                //提交
                if(StringUtil.isNullOrEmpty(nickname.getText().toString())){
                    showMsg(AddReportActivity.this, "请输入投诉对象！");
                    return;
                }if(StringUtil.isNullOrEmpty(content.getText().toString())){
                    showMsg(AddReportActivity.this, "请输入投诉原因！");
                    return;
                }
                if(nickname.getText().toString().length()>50){
                    showMsg(AddReportActivity.this, "举报对象超出字符长度限制！");
                    return;
                }
                if(content.getText().toString().length()>250){
                    showMsg(AddReportActivity.this, "内容最多250个字！");
                    return;
                }
                progressDialog = new CustomProgressDialog(AddReportActivity.this, "请稍后",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                saveData();
            }
                break;
            case R.id.btn_tsxz:
            {
                //投诉须知
                showReport();
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
            if(!StringUtil.isNullOrEmpty(content.getText().toString()) && !StringUtil.isNullOrEmpty(nickname.getText().toString())){
                btn_1.setBackgroundResource(R.drawable.btn_big_active);
                btn_1.setTextColor(getResources().getColor(R.color.white));
            }else {
                btn_1.setBackgroundResource(R.drawable.btn_big_unactive);
                btn_1.setTextColor(getResources().getColor(R.color.textColortwo));
            }
        }
    };

    private ReportPopWindow reportPopWindow;
    private void showReport() {
        reportPopWindow = new ReportPopWindow(AddReportActivity.this, reportOnClick);
        //显示窗口
        setBackgroundAlpha(0.5f);//设置屏幕透明度
        reportPopWindow.setBackgroundDrawable(new BitmapDrawable());
        reportPopWindow.setFocusable(true);
        reportPopWindow.showAtLocation(this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        reportPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
    }
    private View.OnClickListener reportOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            reportPopWindow.dismiss();
            switch (v.getId()) {
                default:
                    break;
            }
        }
    };

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     *            屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) AddReportActivity.this).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) AddReportActivity.this).getWindow().setAttributes(lp);
    }


    private void saveData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appSaveReport,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    showMsg(AddReportActivity.this, "提交投诉信息成功，谢谢您的参与！");
                                    finish();
                                } else {
                                    Toast.makeText(AddReportActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(AddReportActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AddReportActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nickname", nickname.getText().toString());
                params.put("content", content.getText().toString());
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
                                                    showMsgDialog(mm_tel);
                                                }
                                            }
                                        }
                                    }else {
                                        showMsg(AddReportActivity.this, "暂无客服电话！");
                                    }
                                } else {
                                    Toast.makeText(AddReportActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(AddReportActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AddReportActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
        final Dialog picAddDialog = new Dialog(AddReportActivity.this, R.style.dialog);
        View picAddInflate = View.inflate(this, R.layout.msg_tel_dialog, null);
        TextView btn_sure = (TextView) picAddInflate.findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mm_tel));
                    AddReportActivity.this.startActivity(intent);
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
