package com.lbins.meetlove.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
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
import com.lbins.meetlove.data.VersonCodeObjData;
import com.lbins.meetlove.module.VersonCodeObj;
import com.lbins.meetlove.util.StringUtil;
import com.lbins.meetlove.widget.CustomProgressDialog;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 */
public class AboutSettingActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private TextView version;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        initView();
        String versionCode = getV();
        version.setText("V"+versionCode);
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        version = (TextView) this.findViewById(R.id.version);
        title.setText("关于幸福牵手吧");

        this.findViewById(R.id.liner_about).setOnClickListener(this);
        this.findViewById(R.id.liner_version).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.liner_about:
            {
                //关于我们
                Intent intent = new Intent(AboutSettingActivity.this, AboutCompanyActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.liner_version:
            {
                //版本更新
                Resources res = getBaseContext().getResources();
                String message = res.getString(R.string.check_new_version).toString();
                progressDialog = new CustomProgressDialog(AboutSettingActivity.this, "",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                initData();
            }
                break;
        }
    }

    public void initData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.getVersionCode,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    VersonCodeObjData data = getGson().fromJson(s, VersonCodeObjData.class);
                                    VersonCodeObj versionUpdateObj = data.getData();
                                    if(getV().equals(versionUpdateObj.getMm_version_code())){
                                        showMsg(AboutSettingActivity.this, "已是最新版本");
                                    }else{
                                        //更新
                                        final Uri uri = Uri.parse(InternetURL.UPDATE_URL);
                                        final Intent it = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(it);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(AboutSettingActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AboutSettingActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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

    String getV(){
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
