package com.lbins.meetlove.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.meetlove.R;
import com.lbins.meetlove.adapter.ItemLikesAdapter;
import com.lbins.meetlove.base.BaseActivity;
import com.lbins.meetlove.base.InternetURL;
import com.lbins.meetlove.data.HappyHandLikeData;
import com.lbins.meetlove.module.HappyHandLike;
import com.lbins.meetlove.util.StringUtil;
import com.lbins.meetlove.widget.CustomProgressDialog;
import com.lbins.meetlove.widget.PictureGridview;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 */
public class LikesActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;

    private PictureGridview gridview;
    private ItemLikesAdapter adapterGrid;
    private List<HappyHandLike> lists = new ArrayList<HappyHandLike>();
    private Button btnSure;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.likes_activity);
        initView();
        progressDialog = new CustomProgressDialog(LikesActivity.this, "请稍后",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getDataLikes();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("兴趣爱好选择");
        btnSure = (Button) this.findViewById(R.id.btnSure);
        btnSure.setOnClickListener(this);
        gridview = (PictureGridview) this.findViewById(R.id.gridview);
        adapterGrid = new ItemLikesAdapter(lists, LikesActivity.this);
        gridview.setAdapter(adapterGrid);
        gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(lists.size()>position){
                    HappyHandLike happyHandLike = lists.get(position);
                    if(happyHandLike != null){
                        //先判断是否已经选中的够三个了
                        List<HappyHandLike> listSelect = new ArrayList<>();
                        for(HappyHandLike cell:lists){
                            if("3".equals(cell.getIs_use())){
                                listSelect.add(cell);
                            }
                        }
                        if(listSelect.size()==3){
                            //已经选中三个了
                            showMsg(LikesActivity.this, "最多选择三个兴趣爱好！");
                        }else {
                            if(!"0".equals(happyHandLike.getIs_use())){
                                //只要不是禁用的爱好兴趣 就选中状态
                                lists.get(position).setIs_use("3");
                            }
                            adapterGrid.notifyDataSetChanged();
                        }

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
            case R.id.btnSure:
            {
                if(lists != null){
                    List<HappyHandLike> listSelect = new ArrayList<>();
                    for(HappyHandLike happyHandLike:lists){
                        if("3".equals(happyHandLike.getIs_use())){
                            listSelect.add(happyHandLike);
                        }
                    }
                    if(listSelect.size()==3){
                        //说明选中了三个兴趣爱好
                        String likeNames = listSelect.get(0).getLikename()+","+listSelect.get(1).getLikename()+","+listSelect.get(2).getLikename();
                        String likesids = listSelect.get(0).getLikeid()+","+listSelect.get(1).getLikeid()+","+listSelect.get(2).getLikeid();
                        Intent intent = new Intent();
                        intent.putExtra("likeNames", likeNames);
                        intent.putExtra("likesids", likesids);
                        setResult(1001, intent);
                        finish();
                    }else {
                        showMsg(LikesActivity.this, "选择三个兴趣爱好！");
                    }
                }else {
                    showMsg(LikesActivity.this, "选择三个兴趣爱好！");
                }
            }
                break;
        }
    }
    private void getDataLikes() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appLikes,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    HappyHandLikeData data = getGson().fromJson(s, HappyHandLikeData.class);
                                    if(data != null){
                                        lists.clear();
                                        lists.addAll(data.getData());
                                        adapterGrid.notifyDataSetChanged();
                                    }
                                } else {
                                    Toast.makeText(LikesActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(LikesActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(LikesActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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


}
