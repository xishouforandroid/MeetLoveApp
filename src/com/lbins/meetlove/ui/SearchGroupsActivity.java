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
import com.lbins.meetlove.adapter.ItemTuijianGroupAdapter;
import com.lbins.meetlove.adapter.TuijianGroupdapter;
import com.lbins.meetlove.base.BaseActivity;
import com.lbins.meetlove.base.InternetURL;
import com.lbins.meetlove.dao.HappyHandGroup;
import com.lbins.meetlove.data.HappyHandGroupData;
import com.lbins.meetlove.util.StringUtil;
import com.lbins.meetlove.widget.CustomProgressDialog;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SearchGroupsActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private ListView lstv;
    private ItemTuijianGroupAdapter adapter;
    private List<HappyHandGroup> lists = new ArrayList<HappyHandGroup>();
    private ImageView search_null;

    private String keywords;
    private String likeids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tuijian_people_activity);
        keywords = getIntent().getExtras().getString("keywords");
        likeids = getIntent().getExtras().getString("likeids");

        initView();
        progressDialog = new CustomProgressDialog(SearchGroupsActivity.this, "请稍后...",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getData();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("搜索结果");

        search_null = (ImageView) this.findViewById(R.id.search_null);
        search_null.setVisibility(View.GONE);

        lstv = (ListView) this.findViewById(R.id.lstv);
        adapter = new ItemTuijianGroupAdapter(lists, SearchGroupsActivity.this);
        lstv.setAdapter(adapter);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(lists.size()>position){
                    HappyHandGroup group = lists.get(position);
                    if(group != null){
                        Intent intent =  new Intent(SearchGroupsActivity.this, GroupDetailActivity.class);
                        intent.putExtra("groupid", group.getGroupid());
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


    private void getData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appSearchGroupsByKeywords,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    HappyHandGroupData data = getGson().fromJson(s, HappyHandGroupData.class);
                                    if(data != null){
                                        lists.addAll(data.getData());
                                    }
                                    adapter.notifyDataSetChanged();
                                    if(lists.size()>0){
                                        search_null.setVisibility(View.GONE);
                                        lstv.setVisibility(View.VISIBLE);
                                    }else{
                                        search_null.setVisibility(View.VISIBLE);
                                        lstv.setVisibility(View.GONE);
                                    }
                                }else {
                                    Toast.makeText(SearchGroupsActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
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
                if(!StringUtil.isNullOrEmpty(keywords)){
                    params.put("keywords", keywords);
                }
                if(!StringUtil.isNullOrEmpty(likeids)){
                    params.put("likeids", likeids);
                }
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
