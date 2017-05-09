package com.lbins.meetlove.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.meetlove.R;
import com.lbins.meetlove.adapter.ItemTuijianPeopleAdapter;
import com.lbins.meetlove.base.BaseActivity;
import com.lbins.meetlove.base.InternetURL;
import com.lbins.meetlove.dao.Emp;
import com.lbins.meetlove.data.EmpsData;
import com.lbins.meetlove.util.PinyinComparator;
import com.lbins.meetlove.util.StringUtil;
import com.lbins.meetlove.widget.CustomProgressDialog;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by zhl on 2016/8/30.
 */
public class TuijianPeopleActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private ListView lstv;
    private ItemTuijianPeopleAdapter adapter;
    private List<Emp> lists = new ArrayList<Emp>();
    private ImageView search_null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tuijian_people_activity);
        initView();
        progressDialog = new CustomProgressDialog(TuijianPeopleActivity.this, "请稍后...",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getTuijianren1();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("推荐列表");

        search_null = (ImageView) this.findViewById(R.id.search_null);
        search_null.setVisibility(View.GONE);

        lstv = (ListView) this.findViewById(R.id.lstv);
        adapter = new ItemTuijianPeopleAdapter(lists, TuijianPeopleActivity.this);
        lstv.setAdapter(adapter);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(lists.size()>position){
                    Emp emp = lists.get(position);
                    if(emp != null){
                        Intent intent =  new Intent(TuijianPeopleActivity.this, ProfileEmpActivity.class);
                        intent.putExtra("empid", emp.getEmpid());
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    //推荐人-单身中
    private void getTuijianren1() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appTuijianPeoples,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    EmpsData data = getGson().fromJson(s, EmpsData.class);
                                    lists.clear();
                                    if(data != null){
                                        lists.addAll(data.getData());
                                    }
//                                    Collections.sort(lists, new PinyinComparator());
                                    adapter.notifyDataSetChanged();
                                    if(lists.size()>0){
                                        lstv.setVisibility(View.VISIBLE);
                                        search_null.setVisibility(View.GONE);
                                    }else {
                                        search_null.setVisibility(View.VISIBLE);
                                        lstv.setVisibility(View.GONE);
                                    }
                                }else {
                                    Toast.makeText(TuijianPeopleActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("size", "5");
                params.put("sex", getGson().fromJson(getSp().getString("sex", ""), String.class));
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
