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
import com.lbins.meetlove.adapter.ItemNoticesAdapter;
import com.lbins.meetlove.base.BaseActivity;
import com.lbins.meetlove.base.InternetURL;
import com.lbins.meetlove.dao.DBHelper;
import com.lbins.meetlove.dao.HappyHandNews;
import com.lbins.meetlove.dao.HappyHandNotice;
import com.lbins.meetlove.data.HappyHandNoticeDatas;
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
public class NoticesActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private PullToRefreshListView lstv;
    private ItemNoticesAdapter adapter;
    List<HappyHandNotice> lists = new ArrayList<HappyHandNotice>();
    private int pageIndex = 1;
    private static boolean IS_REFRESH = true;
    private Resources res;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notices_activity);
        initView();
        progressDialog = new CustomProgressDialog(NoticesActivity.this, "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getData();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("活动公告");
        lstv = (PullToRefreshListView) this.findViewById(R.id.lstv);
        adapter = new ItemNoticesAdapter(lists, NoticesActivity.this);
        lstv.setMode(PullToRefreshBase.Mode.BOTH);
        lstv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(NoticesActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = true;
                pageIndex = 1;
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(NoticesActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = false;
                pageIndex++;
                getData();
            }
        });
        lstv.setAdapter(adapter);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(lists.size()>(position-1)){
                    HappyHandNotice happyHandNotice= lists.get(position-1);
                    if(happyHandNotice != null){
                        Intent intent = new Intent(NoticesActivity.this, NoticesDetailActivity.class);
                        intent.putExtra("noticeid", happyHandNotice.getNoticeid());
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
                InternetURL.appNotices,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    HappyHandNoticeDatas data = getGson().fromJson(s, HappyHandNoticeDatas.class);
                                    if(data != null){
                                        if (IS_REFRESH) {
                                        lists.clear();
                                        }
                                        lists.addAll(data.getData());
                                        lstv.onRefreshComplete();
                                        adapter.notifyDataSetChanged();
                                        if(lists != null){
                                            for(HappyHandNotice happyHandNotice:lists){
                                                HappyHandNotice tmpT = DBHelper.getInstance(NoticesActivity.this).getHappyHandNoticeById(happyHandNotice.getNoticeid());
                                                if(tmpT != null){
                                                    //说明数据库里有该条记录,更新
                                                    tmpT.setIs_read("1");
                                                    DBHelper.getInstance(NoticesActivity.this).updateHappyHandNotice(tmpT);
                                                }else{
                                                    //说明数据库里没有该记录
                                                    happyHandNotice.setIs_read("1");
                                                    DBHelper.getInstance(NoticesActivity.this).saveHappyHandNotice(happyHandNotice);
                                                }
                                            }
                                        }
                                        Intent intent1 = new Intent("update_message_success");
                                        sendBroadcast(intent1);
                                    }
                                    adapter.notifyDataSetChanged();
                                }else {
                                    Toast.makeText(NoticesActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("page", String.valueOf(pageIndex));
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
