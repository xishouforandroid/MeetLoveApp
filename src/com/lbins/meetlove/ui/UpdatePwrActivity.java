package com.lbins.meetlove.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.lbins.meetlove.util.StringUtil;
import com.lbins.meetlove.widget.CustomProgressDialog;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 */
public class UpdatePwrActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private EditText pwr1;
    private EditText pwr2;
    private EditText pwr3;
    private Button btn_save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_pwr_activity);
        initView();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("密码修改");

        pwr1 = (EditText) this.findViewById(R.id.pwr1);
        pwr2 = (EditText) this.findViewById(R.id.pwr2);
        pwr3 = (EditText) this.findViewById(R.id.pwr3);
        btn_save = (Button) this.findViewById(R.id.btn_save);

        pwr1.addTextChangedListener(watcher);
        pwr2.addTextChangedListener(watcher);
        pwr3.addTextChangedListener(watcher);

        btn_save.setOnClickListener(this);
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
            if(!StringUtil.isNullOrEmpty(pwr1.getText().toString()) && !StringUtil.isNullOrEmpty(pwr2.getText().toString()) && !StringUtil.isNullOrEmpty(pwr3
            .getText().toString()) && pwr2.getText().toString().equals(pwr3.getText().toString())){
                btn_save.setBackground(getDrawable(R.drawable.btn_big_active));
                btn_save.setTextColor(getResources().getColor(R.color.white));
            }else {
                btn_save.setBackground(getDrawable(R.drawable.btn_big_unactive));
                btn_save.setTextColor(getResources().getColor(R.color.textColortwo));
            }
        }
    };
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_save:
            {
                if(StringUtil.isNullOrEmpty(pwr1.getText().toString())){
                    showMsg(UpdatePwrActivity.this, "请输入原始密码！");
                    return;
                }
                if(!pwr1.getText().toString().equals(getGson().fromJson(getSp().getString("password", ""), String.class))){
                    showMsg(UpdatePwrActivity.this, "原始密码不正确！");
                    return;
                }
                if(StringUtil.isNullOrEmpty(pwr2.getText().toString())){
                    showMsg(UpdatePwrActivity.this, "请输入新密码！");
                    return;
                }
                if(pwr2.getText().toString().length() <6 || pwr2.getText().toString().length() > 18){
                    showMsg(UpdatePwrActivity.this, "新密码长度在6到18位之间！");
                    return;
                }
                if(StringUtil.isNullOrEmpty(pwr3.getText().toString())){
                    showMsg(UpdatePwrActivity.this, "请再次输入确认密码！");
                    return;
                }
                if(!pwr2.getText().toString().equals(pwr3.getText().toString())){
                    showMsg(UpdatePwrActivity.this, "两次输入密码不一致！");
                    return;
                }progressDialog = new CustomProgressDialog(UpdatePwrActivity.this, "请稍后",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                saveData();

            }
                break;
        }
    }
    private void saveData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appUpdatePwrById,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    showMsg(UpdatePwrActivity.this, "修改密码成功！");
                                    save("password", pwr2.getText().toString());
                                    finish();
                                } else {
                                    Toast.makeText(UpdatePwrActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(UpdatePwrActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(UpdatePwrActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("password", pwr2.getText().toString());
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
