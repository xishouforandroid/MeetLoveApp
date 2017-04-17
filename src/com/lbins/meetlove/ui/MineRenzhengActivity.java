package com.lbins.meetlove.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.meetlove.MeetLoveApplication;
import com.lbins.meetlove.R;
import com.lbins.meetlove.adapter.AnimateFirstDisplayListener;
import com.lbins.meetlove.base.BaseActivity;
import com.lbins.meetlove.base.InternetURL;
import com.lbins.meetlove.module.City;
import com.lbins.meetlove.module.Province;
import com.lbins.meetlove.util.CompressPhotoUtil;
import com.lbins.meetlove.util.StringUtil;
import com.lbins.meetlove.widget.CustomProgressDialog;
import com.lbins.meetlove.widget.SelectPhotoPopWindow;
import com.lbins.meetlove.widget.SelectRzxfPopWindow;
import com.lbins.meetlove.widget.SelectSuggestPopWindow;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 */
public class MineRenzhengActivity extends BaseActivity implements View.OnClickListener {
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private String pics = "";
    private static final File PHOTO_CACHE_DIR = new File(Environment.getExternalStorageDirectory() + "/meetlove/PhotoCache");

    AsyncHttpClient client = new AsyncHttpClient();

    private TextView title;
    private ImageView btn_right;
    private SelectRzxfPopWindow popWindow;
    private SelectPhotoPopWindow photosWindow;

    private ImageView idcard;
    private TextView nickname;
    private TextView mobile;

    private Button btn_1;
    private Button btn_2;
    private Button btn_3;

    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.renzheng_activity);
        res = getResources();
        initView();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("认证");
        btn_right = (ImageView) this.findViewById(R.id.btn_right);
        btn_right.setVisibility(View.VISIBLE);
        btn_right.setOnClickListener(this);

        idcard = (ImageView) this.findViewById(R.id.idcard);
        nickname = (TextView) this.findViewById(R.id.nickname);
        mobile = (TextView) this.findViewById(R.id.mobile);

        btn_1 = (Button) this.findViewById(R.id.btn_1);
        btn_2 = (Button) this.findViewById(R.id.btn_2);
        btn_3 = (Button) this.findViewById(R.id.btn_3);

        idcard.setOnClickListener(this);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);

        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("nickname", ""), String.class))){
            nickname.setText(getGson().fromJson(getSp().getString("nickname", ""), String.class));
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("mobile", ""), String.class))){
            mobile.setText(getGson().fromJson(getSp().getString("mobile", ""), String.class));
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("cardpic", ""), String.class))){
            imageLoader.displayImage(getGson().fromJson(getSp().getString("cardpic", ""), String.class), idcard, MeetLoveApplication.options, animateFirstListener);
        }
        if("1".equals(getGson().fromJson(getSp().getString("rzstate1", ""), String.class))){
            //身份认证了
            btn_1.setBackground(res.getDrawable(R.drawable.btn_big_unactive));
            btn_1.setTextColor(res.getColor(R.color.textColortwo));
            btn_1.setText("身份已认证");
        }else {
            //未认证
            btn_1.setBackground(res.getDrawable(R.drawable.btn_big_active));
            btn_1.setTextColor(res.getColor(R.color.white));
            btn_1.setText("身份认证");
        }
        if("1".equals(getGson().fromJson(getSp().getString("rzstate2", ""), String.class))){
            //会员认证了
            btn_2.setBackground(res.getDrawable(R.drawable.btn_big_unactive));
            btn_2.setTextColor(res.getColor(R.color.textColortwo));
            btn_2.setText("会员已认证");
        }else {
            //未认证
            btn_2.setBackground(res.getDrawable(R.drawable.btn_big_active));
            btn_2.setTextColor(res.getColor(R.color.white));
            btn_2.setText("会员认证");
        }
        if("1".equals(getGson().fromJson(getSp().getString("rzstate3", ""), String.class))){
            //诚信认证了
            btn_3.setBackground(res.getDrawable(R.drawable.btn_big_unactive));
            btn_3.setTextColor(res.getColor(R.color.textColortwo));
            btn_3.setText("诚信已认证");
        }else {
            //未认证
            btn_3.setBackground(res.getDrawable(R.drawable.btn_big_active));
            btn_3.setTextColor(res.getColor(R.color.white));
            btn_3.setText("诚信认证");
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_right:
            {
                //右上角点击
                showDialog();
            }
                break;
            case R.id.idcard:
            {
                //点击身份证上传
                showDialogPhoto();
            }
                break;
            case R.id.btn_1:
            {
                //身份认证
                if("1".equals(getGson().fromJson(getSp().getString("rzstate1", ""), String.class))){
                    //身份认证了
                    showMsg(MineRenzhengActivity.this , "身份已认证!");
                }else {
                    //未认证
                    showDialogPhoto();
                }
            }
                break;
            case R.id.btn_2:
            {
                //会员认证
                if("1".equals(getGson().fromJson(getSp().getString("rzstate2", ""), String.class))){
                    //会员认证了
                    showMsg(MineRenzhengActivity.this, "会员已认证!");
                }else {
                    //未认证
                    Intent intent = new Intent(MineRenzhengActivity.this, PayEmpRzActivity.class);
                    startActivity(intent);
                }
            }
                break;
            case R.id.btn_3:
            {
                //诚信认证
                if("1".equals(getGson().fromJson(getSp().getString("rzstate3", ""), String.class))){
                    //诚信认证了
                    showMsg(MineRenzhengActivity.this, "诚信已认证!");
                }else {
                    //未认证
                    Intent intent = new Intent(MineRenzhengActivity.this, PayEmpCxActivity.class);
                    startActivity(intent);
                }
            }
                break;
        }
    }

    private void showDialog() {
        popWindow = new SelectRzxfPopWindow(MineRenzhengActivity.this, itemsOnClick);
        //显示窗口
        setBackgroundAlpha(0.5f);//设置屏幕透明度

        popWindow.setBackgroundDrawable(new BitmapDrawable());
        popWindow.setFocusable(true);
        popWindow.showAtLocation(this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
    }

    public void showDialogPhoto(){
        photosWindow = new SelectPhotoPopWindow(MineRenzhengActivity.this, itemsOnClickPhoto);
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
        WindowManager.LayoutParams lp = ((Activity) MineRenzhengActivity.this).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) MineRenzhengActivity.this).getWindow().setAttributes(lp);
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            popWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_xfzf: {
                    showMsg(MineRenzhengActivity.this, "111");
                }
                break;
                case R.id.btn_thbzj: {
                    showMsg(MineRenzhengActivity.this, "222");
                }
                break;
                default:
                    break;
            }
        }
    };

    //身份认证拍照
    private View.OnClickListener itemsOnClickPhoto = new View.OnClickListener() {
        public void onClick(View v) {
            photosWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_photo: {
                    Intent mapstorage = new Intent(Intent.ACTION_PICK, null);
                    mapstorage.setDataAndType(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            "image/*");
                    startActivityForResult(mapstorage, 1);
                }
                break;
                case R.id.btn_camera: {
                    Intent camera = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    //下面这句指定调用相机拍照后的照片存储的路径
                    camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                            .fromFile(new File(Environment
                                    .getExternalStorageDirectory(),
                                    "meetlove_cover.jpg")));
                    startActivityForResult(camera, 2);
                }
                break;
                default:
                    break;
            }
        }
    };

    @Override
     public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // 如果是直接从相册获取
            case 1:
                if (data != null) {
                    startPhotoZoom(data.getData());
                }
                break;
            // 如果是调用相机拍照时
            case 2:
                File temp = new File(Environment.getExternalStorageDirectory()
                        + "/meetlove_cover.jpg");
                startPhotoZoom(Uri.fromFile(temp));
                break;
            // 取得裁剪后的图片
            case 3:
                if (data != null) {
                    setPicToView(data);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 0);
        intent.putExtra("aspectY", 0);
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            if (photo != null) {
                pics = CompressPhotoUtil.saveBitmap2file(photo, System.currentTimeMillis() + ".jpg", PHOTO_CACHE_DIR);
                idcard.setImageBitmap(photo);
                progressDialog = new CustomProgressDialog(MineRenzhengActivity.this, "正在加载中",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                //上传图片到七牛
                uploadCover();
            }
        }
    }

    //上传到七牛云存贮
    void uploadCover(){
        Map<String,String> map = new HashMap<String,String>();
        map.put("space", InternetURL.QINIU_SPACE);
        RequestParams params = new RequestParams(map);
        client.get(InternetURL.UPLOAD_TOKEN, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String token = response.getString("data");
                    UploadManager uploadManager = new UploadManager();
                    uploadManager.put(StringUtil.getBytes(pics), StringUtil.getUUID(), token,
                            new UpCompletionHandler() {
                                @Override
                                public void complete(String key, ResponseInfo info, JSONObject response) {
                                    updateCard(key);
                                }
                            }, null);
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);if (progressDialog != null) {
                    progressDialog.dismiss();
                }

            }
        });
    }

    //更新身份证
    private void updateCard(final String uploadpic) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appUpdateCard,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if(Integer.parseInt(code) == 200) {
                                    save("cardpic", InternetURL.QINIU_URL + uploadpic);
                                    save("rzstate1", "1");
                                    btn_1.setBackground(res.getDrawable(R.drawable.btn_big_unactive));
                                    btn_1.setTextColor(res.getColor(R.color.textColortwo));
                                    btn_1.setText("身份已认证");
                                    showMsg(MineRenzhengActivity.this, "身份认证成功！");
                                }else{
                                    showMsg(MineRenzhengActivity.this, jo.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empid", getGson().fromJson(getSp().getString("empid", ""), String.class));
                params.put("cardpic", uploadpic);
                params.put("nickname", nickname.getText().toString());
                params.put("mobile", mobile.getText().toString());
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
