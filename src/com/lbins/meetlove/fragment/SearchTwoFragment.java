package com.lbins.meetlove.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.meetlove.R;
import com.lbins.meetlove.adapter.ItemLikesAdapter;
import com.lbins.meetlove.base.BaseFragment;
import com.lbins.meetlove.base.InternetURL;
import com.lbins.meetlove.data.HappyHandLikeData;
import com.lbins.meetlove.module.HappyHandLike;
import com.lbins.meetlove.ui.SearchGroupsActivity;
import com.lbins.meetlove.ui.SearchPeopleActivity;
import com.lbins.meetlove.util.StringUtil;
import com.lbins.meetlove.widget.CustomProgressDialog;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchTwoFragment extends BaseFragment implements View.OnClickListener  {

    private View view;
    private Resources res;

    private EditText keywords;
    private Button btn_login;

    private GridView gridview;
    private ItemLikesAdapter adapterGrid;
    private List<HappyHandLike> lists = new ArrayList<HappyHandLike>();

    private String likeids= "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_two_fragment, null);
        res = getActivity().getResources();

        initView();
        progressDialog = new CustomProgressDialog( getActivity(), "请稍后",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getDataLikes();

        return view;
    }

    void initView(){
        keywords = (EditText) view.findViewById(R.id.keywords);
        keywords.addTextChangedListener(watcher);
        btn_login = (Button) view.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        gridview = (GridView) view.findViewById(R.id.gridview);
        adapterGrid = new ItemLikesAdapter(lists, getActivity());
        gridview.setAdapter(adapterGrid);
        gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(lists.size()>position){
                    HappyHandLike happyHandLike = lists.get(position);
                    if(happyHandLike != null){
                        if("0".equals(happyHandLike.getIs_use())){
                            Toast.makeText(getActivity(), "暂不可用!", Toast.LENGTH_SHORT).show();
                        }else{
                            for(int i= 0 ;i<lists.size();i++){
                                HappyHandLike cell = lists.get(i);
                                if("3".equals(cell.getIs_use())){
                                    lists.get(i).setIs_use("1");
                                }
                            }
                            if(!"0".equals(happyHandLike.getIs_use())){
                                //只要不是禁用的爱好兴趣 就选中状态
                                lists.get(position).setIs_use("3");
                                likeids = lists.get(position).getLikeid();
                            }
                            adapterGrid.notifyDataSetChanged();
                            btn_login.setBackground(getResources().getDrawable(R.drawable.btn_big_active));
                            btn_login.setTextColor(getResources().getColor(R.color.white));
                        }
                    }
                }
            }
        });
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
            if(!StringUtil.isNullOrEmpty(keywords.getText().toString())
                    || !StringUtil.isNullOrEmpty(likeids)
                    )
            {
                btn_login.setBackground(getActivity().getDrawable(R.drawable.btn_big_active));
                btn_login.setTextColor(getResources().getColor(R.color.white));
            } else {
                btn_login.setBackground(getActivity().getDrawable(R.drawable.btn_big_unactive));
                btn_login.setTextColor(getResources().getColor(R.color.textColortwo));
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
            {
                //查找
                if(!StringUtil.isNullOrEmpty(keywords.getText().toString())
                        || !StringUtil.isNullOrEmpty(likeids)
                        ){
                    Intent intent = new Intent(getActivity(), SearchGroupsActivity.class);
                    intent.putExtra("keywords", keywords.getText().toString());
                    intent.putExtra("likeids", likeids);
                    startActivity(intent);
                }else{
                    Toast.makeText(getActivity(), "请选择查询条件!", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(getActivity(), jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
