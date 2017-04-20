package com.lbins.meetlove.ui;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.meetlove.MeetLoveApplication;
import com.lbins.meetlove.R;
import com.lbins.meetlove.adapter.AnimateFirstDisplayListener;
import com.lbins.meetlove.adapter.ItemPicAdapter;
import com.lbins.meetlove.base.BaseActivity;
import com.lbins.meetlove.base.InternetURL;
import com.lbins.meetlove.data.EmpData;
import com.lbins.meetlove.data.EmpsData;
import com.lbins.meetlove.data.HappyHandPhotoData;
import com.lbins.meetlove.module.Emp;
import com.lbins.meetlove.module.HappyHandPhoto;
import com.lbins.meetlove.util.StringUtil;
import com.lbins.meetlove.widget.CustomProgressDialog;
import com.lbins.meetlove.widget.PictureGridview;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 */
public class ProfileEmpActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;

    private Resources res;

    private ImageView cover;
    private TextView nickname;
    private TextView is_tuijian;
    private TextView is_state;
    private TextView sign;
    private ImageView vip_1;
    private TextView vip_2;
    private ImageView vip_3;
    private TextView vip_4;
    private TextView age;
    private TextView heightl;
    private TextView address;

    private PictureGridview gridview;
    private ItemPicAdapter adapterGrid;
    private List<String> picLists = new ArrayList<String>();

    private String empid;
    private Emp emp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_emp_activity);
        empid = getIntent().getExtras().getString("empid");
        res = getResources();
        initView();
        initView();
        progressDialog = new CustomProgressDialog(ProfileEmpActivity.this, "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getEmpById();
        getPhotos();
    }

    private void getEmpById() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appEmpByEmpId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    EmpData data = getGson().fromJson(s, EmpData.class);
                                    if(data != null){
                                        emp = data.getData();
                                        if(emp != null){
                                            initData();
                                        }
                                    }
                                }else {
                                    Toast.makeText(ProfileEmpActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("empid", empid);
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

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.VISIBLE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("会员资料");

        gridview = (PictureGridview) this.findViewById(R.id.gridview);
        adapterGrid = new ItemPicAdapter(picLists, ProfileEmpActivity.this);
        gridview.setAdapter(adapterGrid);
        gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ProfileEmpActivity.this, PhotosActivity.class);
                intent.putExtra("empid", empid);
                startActivity(intent);
            }
        });

        cover = (ImageView) this.findViewById(R.id.cover);
        nickname = (TextView) this.findViewById(R.id.nickname);
        is_tuijian = (TextView) this.findViewById(R.id.is_tuijian);
        is_state = (TextView) this.findViewById(R.id.is_state);
        sign = (TextView) this.findViewById(R.id.sign);
        vip_1 = (ImageView) this.findViewById(R.id.vip_1);
        vip_2 = (TextView) this.findViewById(R.id.vip_2);
        vip_3 = (ImageView) this.findViewById(R.id.vip_3);
        vip_4 = (TextView) this.findViewById(R.id.vip_4);
        age = (TextView) this.findViewById(R.id.age);
        heightl = (TextView) this.findViewById(R.id.heightl);
        address = (TextView) this.findViewById(R.id.address);

        cover.setOnClickListener(this);
        nickname.setOnClickListener(this);
        this.findViewById(R.id.liner_photo).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.cover:
            {
                //头像
                Intent intent = new Intent(ProfileEmpActivity.this, ProfileDetailActivity.class);
                intent.putExtra("empid", empid);
                startActivity(intent);
            }
                break;
            case R.id.liner_photo:
            {
                //相册
                Intent intent = new Intent(ProfileEmpActivity.this, PhotosActivity.class);
                intent.putExtra("empid", empid);
                startActivity(intent);
            }
                break;
            case R.id.nickname:
            {
                if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("rzstate1", ""), String.class))){
                    if("1".equals(getGson().fromJson(getSp().getString("rzstate1", ""), String.class))){
                        //进行身份认证了
                    }else {
                        showMsgDialog();
                    }
                }else {
                    showMsgDialog();
                }
            }
                break;
        }
    }

    public void AddToFriends(View view){
        //
    }

    private void showMsgDialog() {
        final Dialog picAddDialog = new Dialog(ProfileEmpActivity.this, R.style.dialog);
        View picAddInflate = View.inflate(this, R.layout.msg_dialog, null);
        TextView btn_sure = (TextView) picAddInflate.findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileEmpActivity.this, MineRenzhengActivity.class);
                startActivity(intent);
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

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private void initData() {
        if(!StringUtil.isNullOrEmpty(emp.getNickname())){
            if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("rzstate1", ""), String.class))){
                if("1".equals(getGson().fromJson(getSp().getString("rzstate1", ""), String.class))){
                    //进行身份认证了
                    nickname.setText(emp.getNickname());
                }else {
                    nickname.setBackground(res.getDrawable(R.drawable.btn_profile_check));
                }
            }else {
                nickname.setBackground(res.getDrawable(R.drawable.btn_profile_check));
            }
        }
        if(!StringUtil.isNullOrEmpty(emp.getCover())){
            imageLoader.displayImage(emp.getCover(), cover, MeetLoveApplication.txOptions, animateFirstListener);
        }
        if(!StringUtil.isNullOrEmpty(emp.getState())){
            if("1".equals(emp.getState())){
                //单身
                is_state.setText("单身");
            }
            if("2".equals(emp.getState())){
                //交往中
                is_state.setText("交往中");
            }
        }
        if(!StringUtil.isNullOrEmpty(emp.getSign())){
            sign.setText("个性签名:"+ emp.getSign());
        }
        if(!StringUtil.isNullOrEmpty(emp.getAge())){
            age.setText(emp.getAge() + "年");
        }
        if(!StringUtil.isNullOrEmpty(emp.getHeightl())){
            heightl.setText(emp.getHeightl() + "cm");
        }
        if(!StringUtil.isNullOrEmpty(emp.getCityName())){
            address.setText(emp.getCityName());
        }

        if(!StringUtil.isNullOrEmpty(emp.getRzstate1())){
            if("1".equals(emp.getRzstate1())){
                //进行身份认证了
                vip_2.setTextColor(res.getColor(R.color.main_color));
            }else {
                vip_2.setTextColor(res.getColor(R.color.textColortwo));
            }
        }else {
            vip_2.setTextColor(res.getColor(R.color.textColortwo));
        }
        if(!StringUtil.isNullOrEmpty(emp.getRzstate2())){
            if("1".equals(emp.getRzstate2())){
                vip_1.setImageDrawable(res.getDrawable(R.drawable.icon_verify_id_enabled));
            }else {
                vip_1.setImageDrawable(res.getDrawable(R.drawable.icon_verify_id_disable));
            }
        }else {
            vip_1.setImageDrawable(res.getDrawable(R.drawable.icon_verify_id_disable));
        }
        if(!StringUtil.isNullOrEmpty(emp.getRzstate3())){
            if("1".equals(emp.getRzstate3())){
                //进行身份认证了
                vip_3.setImageDrawable(res.getDrawable(R.drawable.icon_verify_honesty_enabled));
                vip_4.setTextColor(res.getColor(R.color.main_color));
            }else {
                vip_3.setImageDrawable(res.getDrawable(R.drawable.icon_verify_honesty_disable));
                vip_4.setTextColor(res.getColor(R.color.textColortwo));
            }
        }else {
            vip_3.setImageDrawable(res.getDrawable(R.drawable.icon_verify_honesty_disable));
            vip_4.setTextColor(res.getColor(R.color.textColortwo));
        }
    }

    private void getPhotos() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appPhotos,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    HappyHandPhotoData data = getGson().fromJson(s, HappyHandPhotoData.class);
                                    if(data != null){
                                        HappyHandPhoto happyHandPhoto  = data.getData();
                                        if(happyHandPhoto != null){
                                            String photos = happyHandPhoto.getPhotos();
                                            if(!StringUtil.isNullOrEmpty(photos)){
                                                String[] arras = photos.split(",");
                                                if(arras != null){
                                                    picLists.clear();
                                                    for(int i=0;i<arras.length;i++){
                                                        if(!StringUtil.isNullOrEmpty(arras[i])){
                                                            picLists.add(arras[i]);
                                                        }
                                                    }
                                                }
                                                adapterGrid.notifyDataSetChanged();
                                            }
                                        }
                                    }
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
                params.put("empid", empid);
                params.put("size", "3");
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
