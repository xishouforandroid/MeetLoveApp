package com.lbins.meetlove.ui;

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
import com.lbins.meetlove.data.HappyHandCompanyData;
import com.lbins.meetlove.data.HappyHandLikeData;
import com.lbins.meetlove.module.HappyHandCompany;
import com.lbins.meetlove.util.StringUtil;
import com.lbins.meetlove.widget.CustomProgressDialog;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 */
public class AboutCompanyActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private TextView content;
    private TextView title1;
    private TextView cont1;
    private TextView cont2;
    private TextView cont3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_company_activity);
        initView();
        progressDialog = new CustomProgressDialog(AboutCompanyActivity.this, "请稍后",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getData();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("公司介绍");

        content = (TextView) this.findViewById(R.id.content);
        title1 = (TextView) this.findViewById(R.id.title1);
        cont1 = (TextView) this.findViewById(R.id.cont1);
        cont2 = (TextView) this.findViewById(R.id.cont2);
        cont3 = (TextView) this.findViewById(R.id.cont3);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    private void getData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appAboutUs,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    List<HappyHandCompany> lists = new ArrayList<>();
                                    HappyHandCompanyData data = getGson().fromJson(s, HappyHandCompanyData.class);
                                    if(data != null){
                                        lists.clear();
                                        lists.addAll(data.getData());
                                        if(lists != null && lists.size()>0){
                                            HappyHandCompany happyHandCompany = lists.get(0);
                                            if(happyHandCompany != null){
                                                //公司不为空
                                                initData(happyHandCompany);
                                            }
                                        }
                                    }
                                } else {
                                    Toast.makeText(AboutCompanyActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(AboutCompanyActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AboutCompanyActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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



    void initData(HappyHandCompany happyHandCompany){
        content.setText(happyHandCompany.getContent());
        title1.setText(happyHandCompany.getTitle());
        cont1.setText(happyHandCompany.getCont1());
        cont2.setText(happyHandCompany.getCont2());
        cont3.setText(happyHandCompany.getCont3());
    }


}
