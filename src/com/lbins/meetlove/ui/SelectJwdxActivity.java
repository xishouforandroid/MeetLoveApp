package com.lbins.meetlove.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.lbins.meetlove.adapter.ItemJwdxAdapter;
import com.lbins.meetlove.adapter.OnClickContentItemListener;
import com.lbins.meetlove.base.BaseActivity;
import com.lbins.meetlove.base.InternetURL;
import com.lbins.meetlove.data.FriendsData;
import com.lbins.meetlove.module.Friends;
import com.lbins.meetlove.util.StringUtil;
import com.lbins.meetlove.widget.CustomProgressDialog;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectJwdxActivity extends BaseActivity implements View.OnClickListener,OnClickContentItemListener ,AdapterView.OnItemClickListener{
    private TextView title;
    private TextView btn_right;

    private ListView lstv;
    private ItemJwdxAdapter adapter;
    private List<Friends> lists = new ArrayList<Friends>();

    private List<String> listEmpSelect = new ArrayList<String>();//存放选中的会员

    public SelectJwdxActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jwdx_activity);
        initView();
        adapter.isCheckMap.clear();
        progressDialog = new CustomProgressDialog(SelectJwdxActivity.this, "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getData();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        btn_right = (TextView) this.findViewById(R.id.btn_right);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("选择交往对象");
        btn_right.setVisibility(View.VISIBLE);
        btn_right.setOnClickListener(this);

        adapter = new ItemJwdxAdapter(SelectJwdxActivity.this, lists);
        adapter.setOnClickContentItemListener(this);
        lstv = (ListView) this.findViewById(R.id.lstv);
        lstv.setAdapter(adapter);
        lstv.setVisibility(View.VISIBLE);
        lstv.setOnItemClickListener(this);
        mapDone();
    }

    void mapDone(){
        listEmpSelect.clear();//先清空

        for (Map.Entry<Integer, Boolean> entry : adapter.isCheckMap.entrySet()) {
            Integer key = entry.getKey();
            Boolean value = entry.getValue();
            System.out.println("key=" + key + " value=" + value);
            if(value){
                //如果选中了
                if(lists.size()>key){
                    listEmpSelect.add(lists.get(key).getEmpid2());
                }
            }
        }
    }

    /**
     * 当ListView 子项点击的时候
     */
    @Override
    public void onItemClick(AdapterView<?> listView, View itemLayout,
                            int position, long id) {
        if (itemLayout.getTag() instanceof ItemJwdxAdapter.ViewHolder) {
            ItemJwdxAdapter.ViewHolder holder = (ItemJwdxAdapter.ViewHolder) itemLayout.getTag();
            // 会自动出发CheckBox的checked事件
            holder.cbCheck.toggle();
            mapDone();
        }
    }

    //查询
    private void getData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appFriends,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    FriendsData data = getGson().fromJson(s, FriendsData.class);
                                    lists.clear();
                                    lists.addAll(data.getData());
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(SelectJwdxActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(SelectJwdxActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SelectJwdxActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("is_check", "1");
                params.put("empid1", getGson().fromJson(getSp().getString("empid", ""), String.class));
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_right:
            {
                if(listEmpSelect.size() == 0){
                    showMsg(SelectJwdxActivity.this, "请选择一个交往对象！");
                    return;
                }
                if(listEmpSelect.size() > 1){
                    showMsg(SelectJwdxActivity.this, "一次只能选择一个交往对象！");
                    return;
                }
                String empid2 = listEmpSelect.get(0);
                progressDialog = new CustomProgressDialog(SelectJwdxActivity.this, "正在加载中",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                saveApply(empid2);
            }
            break;
        }
    }

    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        mapDone();
    }

    private void saveApply(final String empid2) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appSaveJiaowang,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    showMsg(SelectJwdxActivity.this, "请求成功，请耐心等待对方确认！");
                                    finish();
                                }else if (Integer.parseInt(code1) == 2) {
                                    //对方尚未确认，请耐心等待
                                    showMsgDialog(empid2);
                                } else {
                                    Toast.makeText(SelectJwdxActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(SelectJwdxActivity.this, "操作失败，请稍后重试！", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SelectJwdxActivity.this, "操作失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empid1", getGson().fromJson(getSp().getString("empid", ""), String.class));
                params.put("empid2", empid2);
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


    private void showMsgDialog(final String empid2) {
        final Dialog picAddDialog = new Dialog(SelectJwdxActivity.this, R.style.dialog);
        View picAddInflate = View.inflate(this, R.layout.jwdx_msg_dialog, null);
        TextView btn_sure = (TextView) picAddInflate.findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消邀请
                deleteYq(empid2);
                picAddDialog.dismiss();
            }
        });

        //取消
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


    private void deleteYq(final String empid2) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appDeleteJiaowang,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    showMsg(SelectJwdxActivity.this, "取消邀请成功！");
                                    finish();
                                } else {
                                    Toast.makeText(SelectJwdxActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(SelectJwdxActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SelectJwdxActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empid2", empid2);
                params.put("empid1", getGson().fromJson(getSp().getString("empid", ""), String.class));
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
