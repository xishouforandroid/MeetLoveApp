package com.lbins.meetlove.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.meetlove.R;
import com.lbins.meetlove.adapter.ItemFriendsApplyAdapter;
import com.lbins.meetlove.adapter.ItemJwdxApplyAdapter;
import com.lbins.meetlove.adapter.OnClickContentItemListener;
import com.lbins.meetlove.base.BaseActivity;
import com.lbins.meetlove.base.InternetURL;
import com.lbins.meetlove.dao.HappyHandJw;
import com.lbins.meetlove.data.FriendsData;
import com.lbins.meetlove.data.HappyHandJwDatas;
import com.lbins.meetlove.library.PullToRefreshBase;
import com.lbins.meetlove.library.PullToRefreshListView;
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
public class JwdxApplyActivity extends BaseActivity implements View.OnClickListener,OnClickContentItemListener {
    private TextView title;
    private PullToRefreshListView lstv;
    private ItemJwdxApplyAdapter adapter;
    List<HappyHandJw> lists = new ArrayList<HappyHandJw>();
    private int pageIndex = 1;
    private static boolean IS_REFRESH = true;
    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jwdx_apply_activity);
        res = getResources();
        initView();
        progressDialog = new CustomProgressDialog(JwdxApplyActivity.this, "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getData();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("交往消息");

        lstv = (PullToRefreshListView) this.findViewById(R.id.lstv);
        adapter = new ItemJwdxApplyAdapter(lists, JwdxApplyActivity.this);
        lstv.setMode(PullToRefreshBase.Mode.BOTH);
        lstv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(JwdxApplyActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = true;
                pageIndex = 1;
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(JwdxApplyActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = false;
                pageIndex++;
                getData();
            }
        });
        lstv.setAdapter(adapter);
        adapter.setOnClickContentItemListener(this);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(lists.size()>(position-1)){
                    HappyHandJw happyHandJw = lists.get(position-1);
                    if(happyHandJw != null){
                        Intent intent = new Intent(JwdxApplyActivity.this, ProfileEmpActivity.class);
                        intent.putExtra("empid", happyHandJw.getEmpid1());
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
                InternetURL.appJiaowangs,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    HappyHandJwDatas data = getGson().fromJson(s, HappyHandJwDatas.class);
                                    if(data != null){
//                                        if (IS_REFRESH) {
                                            lists.clear();
//                                        }
                                        lists.addAll(data.getData());
                                        lstv.onRefreshComplete();
                                        adapter.notifyDataSetChanged();
                                    }
                                    adapter.notifyDataSetChanged();
                                }else {
                                    Toast.makeText(JwdxApplyActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("empid2", getGson().fromJson(getSp().getString("empid", ""), String.class));
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

    private int tmpSelect=0;
    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        switch (flag){
            case 1:
            {
                if(lists.size()>position){
                    HappyHandJw friends = lists.get(position);
                    if(friends != null){
                        //接受
                        tmpSelect = position;
                        progressDialog = new CustomProgressDialog(JwdxApplyActivity.this, "正在加载中",R.anim.custom_dialog_frame);
                        progressDialog.setCancelable(true);
                        progressDialog.setIndeterminate(true);
                        progressDialog.show();
                        friends.setIs_check("1");
                        saveAcctept(friends);
                    }
                }
            }
                break;
            case 2:
            {
                if(lists.size()>position){
                    HappyHandJw friends = lists.get(position);
                    if(friends != null){
                        //拒绝
                        tmpSelect = position;
                        progressDialog = new CustomProgressDialog(JwdxApplyActivity.this, "正在加载中",R.anim.custom_dialog_frame);
                        progressDialog.setCancelable(true);
                        progressDialog.setIndeterminate(true);
                        progressDialog.show();
                        friends.setIs_check("2");
                        saveAcctept(friends);
                    }
                }
            }
            break;
        }
    }

    private void saveAcctept(final HappyHandJw friends) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appAcceptJiaowang,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    showMsg(JwdxApplyActivity.this, "操作成功!");
                                    lists.get(tmpSelect).setIs_check(friends.getIs_check());
                                    adapter.notifyDataSetChanged();
                                    //调用广播，刷新主页
                                    if("1".equals(friends.getIs_check())){
                                        //同意了
                                        save("state", "2");
                                        Intent intent1 = new Intent("update_jwdx_success");
                                        sendBroadcast(intent1);
                                    }
                                    if("2".equals(friends.getIs_check())){
                                        //拒绝
                                        Intent intent1 = new Intent("update_jwdx_refuse");
                                        sendBroadcast(intent1);
                                    }
                                }else {
                                    Toast.makeText(JwdxApplyActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("jwid", friends.getJwid());
                params.put("is_check", friends.getIs_check());
                params.put("empid1", friends.getEmpid1());
                params.put("empid2", friends.getEmpid2());
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
