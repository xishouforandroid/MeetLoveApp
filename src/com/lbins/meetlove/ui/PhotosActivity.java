package com.lbins.meetlove.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.meetlove.R;
import com.lbins.meetlove.adapter.ItemPhotosAdapter;
import com.lbins.meetlove.base.BaseActivity;
import com.lbins.meetlove.base.InternetURL;
import com.lbins.meetlove.data.HappyHandPhotoData;
import com.lbins.meetlove.module.HappyHandPhoto;
import com.lbins.meetlove.util.Constants;
import com.lbins.meetlove.util.StringUtil;
import com.lbins.meetlove.widget.CustomProgressDialog;
import com.lbins.meetlove.widget.PictureGridview;
import com.lbins.meetlove.widget.SelectPhotoPopWindow;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PhotosActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private String empid;
    private ImageView btn_right;
    private Resources res;

    private GridView gridview;
    private List<String> lists = new ArrayList<>();
    private ItemPhotosAdapter adapter;
    HappyHandPhoto happyHandPhoto;

    private SelectPhotoPopWindow photosWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos_activity);
        registerBoradcastReceiver();
        res =getResources();
        //用户ID
        empid = getIntent().getExtras().getString("empid");
        initView();
        progressDialog = new CustomProgressDialog(PhotosActivity.this, "请稍后",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getData();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("相册");

        btn_right = (ImageView) this.findViewById(R.id.btn_right);
        if(empid.equals(getGson().fromJson(getSp().getString("empid", ""), String.class))){
            btn_right.setVisibility(View.VISIBLE);
            btn_right.setImageDrawable(res.getDrawable(R.drawable.icon_navbar_more));
            btn_right.setOnClickListener(this);
        }else{
            btn_right.setVisibility(View.GONE);
        }
        gridview = (GridView) this.findViewById(R.id.gridview);
        adapter = new ItemPhotosAdapter(lists, PhotosActivity.this);
        gridview.setAdapter(adapter);
        gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(lists.size() > position){
                    final String[] picUrls = happyHandPhoto.getPhotos().split(",");//图片链接切割
                    Intent intent = new Intent(PhotosActivity.this, GalleryUrlActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    intent.putExtra(Constants.IMAGE_URLS, picUrls);
                    intent.putExtra(Constants.IMAGE_POSITION, position);
                    startActivity(intent);
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
            case R.id.btn_right:
            {
                //添加图片
                showDialogPhoto();
            }
                break;
        }
    }

    private void getData() {
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
                                        happyHandPhoto  = data.getData();
                                        if(happyHandPhoto != null){
                                            String photos = happyHandPhoto.getPhotos();
                                            if(!StringUtil.isNullOrEmpty(photos)){
                                                String[] arras = photos.split(",");
                                                if(arras != null){
                                                    lists.clear();
                                                    for(int i=0;i<arras.length;i++){
                                                        if(!StringUtil.isNullOrEmpty(arras[i])){
                                                            lists.add(arras[i]);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        adapter.notifyDataSetChanged();
                                    }
                                } else {
                                    Toast.makeText(PhotosActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(PhotosActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(PhotosActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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

    public void showDialogPhoto(){
        photosWindow = new SelectPhotoPopWindow(PhotosActivity.this, itemsOnClickPhoto);
        //显示窗口
        setBackgroundAlpha(0.5f);//设置屏幕透明度

        photosWindow.setBackgroundDrawable(new BitmapDrawable());
        photosWindow.setFocusable(true);
        photosWindow.showAtLocation(this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        photosWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     *            屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) PhotosActivity.this).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) PhotosActivity.this).getWindow().setAttributes(lp);
    }

    private View.OnClickListener itemsOnClickPhoto = new View.OnClickListener() {
        public void onClick(View v) {
            photosWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_photo: {
                    Intent pic = new Intent(PhotosActivity.this, PublishPicActivity.class);
                    pic.putExtra("SELECT_PHOTOORPIIC", "0");
                    startActivity(pic);
                }
                break;
                case R.id.btn_camera: {
                    Intent photo = new Intent(PhotosActivity.this, PublishPicActivity.class);
                    photo.putExtra("SELECT_PHOTOORPIIC", "1");
                    startActivity(photo);
                }
                break;
                default:
                    break;
            }
        }
    };


    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("update_photo_success")) {
                progressDialog = new CustomProgressDialog(PhotosActivity.this, "请稍后",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                getData();
            }
        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("update_photo_success");
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

}
