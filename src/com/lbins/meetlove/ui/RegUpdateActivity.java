package com.lbins.meetlove.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.meetlove.MainActivity;
import com.lbins.meetlove.MeetLoveApplication;
import com.lbins.meetlove.R;
import com.lbins.meetlove.adapter.AnimateFirstDisplayListener;
import com.lbins.meetlove.adapter.OnClickContentItemListener;
import com.lbins.meetlove.base.BaseActivity;
import com.lbins.meetlove.base.InternetURL;
import com.lbins.meetlove.data.EmpData;
import com.lbins.meetlove.data.HappyHandLikeData;
import com.lbins.meetlove.module.City;
import com.lbins.meetlove.module.Emp;
import com.lbins.meetlove.module.HappyHandLike;
import com.lbins.meetlove.module.Province;
import com.lbins.meetlove.util.CompressPhotoUtil;
import com.lbins.meetlove.util.StringUtil;
import com.lbins.meetlove.widget.*;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 */
public class RegUpdateActivity extends BaseActivity implements View.OnClickListener,OnClickContentItemListener {
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    private String pics = "";
    private static final File PHOTO_CACHE_DIR = new File(Environment.getExternalStorageDirectory() + "/meetlove/PhotoCache");

    AsyncHttpClient client = new AsyncHttpClient();

    private TextView title;

    private ImageView cover;
    private EditText sign;
    private EditText nickname;
    private TextView age;
    private TextView heightl;
    private TextView education;
    private TextView address;
    private TextView marragie;
    private EditText company;
    private TextView likes;

    private TextView age_marry;
    private TextView heightl_marry;
    private TextView education_marry;
    private TextView marry_marry;

    private Button btn_login;

    private SelectPhotoPopWindow photosWindow;
    private PopEducationWindow popEducationWindow;
    private PopMarryWindow popMarryWindow;
    private PopAgeWindow popAgeWindow;
    private PopHeightlWindow popHeightlWindow;
    private PopAgeProfileWindow popAgeProfileWindow;
    private PopHeightlProfileWindow popHeightlProfileWindow;
    private PopAreaWindow popAreaWindow;

    private String empid;//注册成功返回的会员ID

    private String ageStr = "";
    private String heightlStr = "";
    private String educationID = "";
    private String provinceid = "";
    private String cityid = "";
    private String marragieID = "";
    private String likeids= "";

    //择偶要求
    private String agestart="";
    private String ageend="";
    private String heightlstart="";
    private String heightlend="";
    private String educationID2="";
    private String marragieID2 = "";

    private TextView txt_pic;
    private TextView mobile;

    private List<HappyHandLike> likeLists = new ArrayList<>();//兴趣爱好集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_update_activity);
        empid =getIntent().getExtras().getString("empid");
        initView();

        initData();
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("likeids", ""), String.class))){
            //兴趣爱好查询
            getLikes();
        }
    }

    //实例化
    private void initData() {
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("nickname", ""), String.class))){
            nickname.setText(getGson().fromJson(getSp().getString("nickname", ""), String.class));
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("cover", ""), String.class))){
            imageLoader.displayImage(getGson().fromJson(getSp().getString("cover", ""), String.class), cover, MeetLoveApplication.txOptions, animateFirstListener);
            txt_pic.setText("更换头像");
        }else {
            txt_pic.setText("上传照片");
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("sign", ""), String.class))){
            sign.setText(getGson().fromJson(getSp().getString("sign", ""), String.class));
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("mobile", ""), String.class))){
            mobile.setText(getGson().fromJson(getSp().getString("mobile", ""), String.class));
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("age", ""), String.class))){
            age.setText(getGson().fromJson(getSp().getString("age", ""), String.class)+"年");
            ageStr = getGson().fromJson(getSp().getString("age", ""), String.class);
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("heightl", ""), String.class))){
            heightl.setText(getGson().fromJson(getSp().getString("heightl", ""), String.class)+"cm");
            heightlStr = getGson().fromJson(getSp().getString("heightl", ""), String.class);
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("education", ""), String.class))){
            educationID = getGson().fromJson(getSp().getString("education", ""), String.class);
            switch (Integer.parseInt(getGson().fromJson(getSp().getString("education", ""), String.class))){
                case 1:
                {
                    education.setText("高中及以下");
                }
                    break;
                case 2:
                {
                    education.setText("中专");
                }
                break;
                case 3:
                {
                    education.setText("专科");
                }
                break;
                case 4:
                {
                    education.setText("本科");
                }
                break;
                case 5:
                {
                    education.setText("研究生及以上");
                }
                break;
            }
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("pname", ""), String.class)) && !StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("cityName", ""), String.class))){
            address.setText(getGson().fromJson(getSp().getString("pname", ""), String.class)+getGson().fromJson(getSp().getString("cityName", ""), String.class));
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("provinceid", ""), String.class))){
            provinceid = getGson().fromJson(getSp().getString("provinceid", ""), String.class);
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("cityid", ""), String.class))){
            cityid = getGson().fromJson(getSp().getString("cityid", ""), String.class);
        }

        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("marriage", ""), String.class))){
            marragieID = getGson().fromJson(getSp().getString("marriage", ""), String.class);
            switch (Integer.parseInt(getGson().fromJson(getSp().getString("marriage", ""), String.class))){
                case 1:
                {
                    marragie.setText("未婚");
                }
                break;
                case 2:
                {
                    marragie.setText("离异");
                }
                break;
                case 3:
                {
                    marragie.setText("丧偶");
                }
                break;
            }
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("company", ""), String.class))){
            company.setText(getGson().fromJson(getSp().getString("company", ""), String.class)+"cm");
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("likeids", ""), String.class))){
            likeids = getGson().fromJson(getSp().getString("likeids", ""), String.class);
        }

        //-------------择偶要求------------------
        if (!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("agestart", ""), String.class)) && !StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("ageend", ""), String.class))){
            age_marry.setText(getGson().fromJson(getSp().getString("agestart", ""), String.class)+"-" + getGson().fromJson(getSp().getString("ageend", ""), String.class)+"年");
            agestart = getGson().fromJson(getSp().getString("agestart", ""), String.class);
            ageend = getGson().fromJson(getSp().getString("ageend", ""), String.class);

        }
        if (!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("heightlstart", ""), String.class)) && !StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("heightlend", ""), String.class))){
            heightl_marry.setText(getGson().fromJson(getSp().getString("heightlstart", ""), String.class)+"-" + getGson().fromJson(getSp().getString("heightlend", ""), String.class)+"年");
            heightlstart = getGson().fromJson(getSp().getString("heightlstart", ""), String.class);
            heightlend = getGson().fromJson(getSp().getString("heightlend", ""), String.class);
        }

        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("educationm", ""), String.class))){
            educationID2 = getGson().fromJson(getSp().getString("educationm", ""), String.class);
            switch (Integer.parseInt(getGson().fromJson(getSp().getString("educationm", ""), String.class))){
                case 1:
                {
                    education_marry.setText("高中及以下");
                }
                break;
                case 2:
                {
                    education_marry.setText("中专");
                }
                break;
                case 3:
                {
                    education_marry.setText("专科");
                }
                break;
                case 4:
                {
                    education_marry.setText("本科");
                }
                break;
                case 5:
                {
                    education_marry.setText("研究生及以上");
                }
                break;
            }
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("marriagem", ""), String.class))){
            marragieID2 = getGson().fromJson(getSp().getString("marriagem", ""), String.class);
            switch (Integer.parseInt(getGson().fromJson(getSp().getString("marriagem", ""), String.class))){
                case 1:
                {
                    marry_marry.setText("未婚");
                }
                break;
                case 2:
                {
                    marry_marry.setText("离异");
                }
                break;
                case 3:
                {
                    marry_marry.setText("丧偶");
                }
                break;
            }
        }

    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("填写资料");

        cover = (ImageView) this.findViewById(R.id.cover);
        sign = (EditText) this.findViewById(R.id.sign);
        nickname = (EditText)this.findViewById(R.id.nickname);
        age = (TextView) this.findViewById(R.id.age);
        heightl = (TextView) this.findViewById(R.id.heightl);
        education = (TextView) this.findViewById(R.id.education);
        address = (TextView) this.findViewById(R.id.address);
        marragie = (TextView) this.findViewById(R.id.marragie);
        likes = (TextView) this.findViewById(R.id.likes);
        company = (EditText) this.findViewById(R.id.company);
        txt_pic = (TextView) this.findViewById(R.id.txt_pic);
        mobile = (TextView) this.findViewById(R.id.mobile);

        age_marry = (TextView) this.findViewById(R.id.age_marry);
        heightl_marry = (TextView) this.findViewById(R.id.heightl_marry);
        education_marry = (TextView) this.findViewById(R.id.education_marry);
        marry_marry = (TextView) this.findViewById(R.id.marry_marry);

        btn_login = (Button) this.findViewById(R.id.btn_login);

        cover.setOnClickListener(this);
        age.setOnClickListener(this);
        heightl.setOnClickListener(this);
        education.setOnClickListener(this);
        address.setOnClickListener(this);
        marragie.setOnClickListener(this);
        likes.setOnClickListener(this);
        age_marry.setOnClickListener(this);
        heightl_marry.setOnClickListener(this);
        education_marry.setOnClickListener(this);
        marry_marry.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        sign.addTextChangedListener(watcher);
        nickname.addTextChangedListener(watcher);
        company.addTextChangedListener(watcher);
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
            if(!StringUtil.isNullOrEmpty(sign.getText().toString()) && !StringUtil.isNullOrEmpty(nickname.getText().toString())&& !StringUtil.isNullOrEmpty(company.getText().toString())){
                btn_login.setBackground(getDrawable(R.drawable.btn_big_active));
            }else{
                btn_login.setBackground(getDrawable(R.drawable.btn_big_unactive));
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.cover:
            {
                //头像点击
                showDialogPhoto();
            }
                break;
            case R.id.age:
            {
                //年龄
                showAgeProfile();
            }
                break;
            case R.id.heightl:
            {
                //身高
                showPopHeightlProfile();
            }
            break;
            case R.id.education:
            {
                //学历
                showPopEducation();
            }
            break;
            case R.id.address:
            {
                //所在地
                Intent intent = new Intent(RegUpdateActivity.this, SelectAreaActivity.class);
                startActivityForResult(intent, 1000);
            }
            break;
            case R.id.marragie:
            {
                //婚姻状况
                showPopMarry();
            }
            break;
            case R.id.likes:
            {
                //爱好
                Intent intent = new Intent(RegUpdateActivity.this, LikesActivity.class);
                startActivityForResult(intent, 1001);
            }
            break;



            case R.id.age_marry:
            {
                //年龄-择偶
                showPopAge();
            }
            break;
            case R.id.heightl_marry:
            {
                //身高--择偶
                showPopHeightl();
            }
            break;
            case R.id.education_marry:
            {
                //学历-择偶
                showPopEducation2();
            }
            break;
            case R.id.marry_marry:
            {
                //婚姻状况-择偶
                showPopMarry2();
            }
            break;
            case R.id.btn_login:
            {
                if(StringUtil.isNullOrEmpty(sign.getText().toString())){
                    showMsg(RegUpdateActivity.this, "请输入个性签名");
                    return;
                }
                if(StringUtil.isNullOrEmpty(nickname.getText().toString())){
                    showMsg(RegUpdateActivity.this, "请输入姓名");
                    return;
                }
                if(StringUtil.isNullOrEmpty(ageStr)){
                    showMsg(RegUpdateActivity.this, "请选择您的年龄");
                    return;
                }
                if(StringUtil.isNullOrEmpty(heightlStr)){
                    showMsg(RegUpdateActivity.this, "请选择您的身高");
                    return;
                }
                if(StringUtil.isNullOrEmpty(educationID)){
                    showMsg(RegUpdateActivity.this, "请选择您的学历");
                    return;
                }
                if(StringUtil.isNullOrEmpty(provinceid) || StringUtil.isNullOrEmpty(cityid)){
                    showMsg(RegUpdateActivity.this, "请选择您的所在地");
                    return;
                }
                if(StringUtil.isNullOrEmpty(marragieID)){
                    showMsg(RegUpdateActivity.this, "请选择您的婚姻状况");
                    return;
                }
                if(StringUtil.isNullOrEmpty(company.getText().toString())){
                    showMsg(RegUpdateActivity.this, "请输入工作单位");
                    return;
                }
                if(StringUtil.isNullOrEmpty(likeids)){
                    showMsg(RegUpdateActivity.this, "请选择您的兴趣爱好");
                    return;
                }

                //------------择偶标准-0------

                if(StringUtil.isNullOrEmpty(agestart) || StringUtil.isNullOrEmpty(ageend)){
                    showMsg(RegUpdateActivity.this, "请选择择偶年龄范围");
                    return;
                }
                if(StringUtil.isNullOrEmpty(heightlstart) || StringUtil.isNullOrEmpty(heightlend)){
                    showMsg(RegUpdateActivity.this, "请选择择偶身高范围");
                    return;
                }
                if(StringUtil.isNullOrEmpty(educationID2)){
                    showMsg(RegUpdateActivity.this, "请选择择偶学历");
                    return;
                }
                if(StringUtil.isNullOrEmpty(marragieID2)){
                    showMsg(RegUpdateActivity.this, "请选择择偶婚姻状况");
                    return;
                }
                progressDialog = new CustomProgressDialog(RegUpdateActivity.this, "正在加载中",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                reg();
            }
                break;
        }
    }

    private void reg(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appUpdateProfile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    EmpData data = getGson().fromJson(s, EmpData.class);
                                    saveAccount(data.getData());

                                }  else {
                                    showMsg(RegUpdateActivity.this,  jo.getString("message"));
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
                        Toast.makeText(RegUpdateActivity.this, R.string.login_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empid", empid);
                params.put("sign", sign.getText().toString());
                params.put("nickname", nickname.getText().toString());
                params.put("age", ageStr);
                params.put("heightl", heightlStr);
                params.put("education", educationID);
                params.put("provinceid", provinceid);
                params.put("cityid", cityid);
                params.put("marriage", marragieID);
                params.put("company", company.getText().toString());
                params.put("likeids", likeids);
                params.put("state", "1");

                if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("chooseid", ""), String.class))){
                    params.put("chooseid", getGson().fromJson(getSp().getString("chooseid", ""), String.class));
                }
                params.put("agestart", agestart);
                params.put("ageend", ageend);
                params.put("heightlstart", heightlstart);
                params.put("heightlend", heightlend);
                params.put("educationm", educationID2);
                params.put("marriagem", marragieID2);

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

    public void saveAccount(final Emp emp) {
        save("empid", emp.getEmpid());
        save("mobile", emp.getMobile());
        save("nickname", emp.getNickname());
        save("cover", emp.getCover());
        save("sign", emp.getSign());
        save("age", emp.getAge());
        save("sex", emp.getSex());
        save("heightl", emp.getHeightl());
        save("education", emp.getEducation());
        save("provinceid", emp.getProvinceid());
        save("cityid", emp.getCityid());
        save("areaid", emp.getAreaid());
        save("marriage", emp.getMarriage());
        save("company", emp.getCompany());
        save("likeids", emp.getLikeids());
        save("state", emp.getState());
        save("cardpic", emp.getCardpic());
        save("rzstate1", emp.getRzstate1());
        save("rzstate2", emp.getRzstate2());
        save("rzstate3", emp.getRzstate3());
        save("is_use", emp.getIs_use());
        save("pname", emp.getPname());
        save("cityName", emp.getCityName());

        save("chooseid", emp.getChooseid());
        save("agestart", emp.getAgestart());
        save("ageend", emp.getAgeend());
        save("heightlstart", emp.getHeightlstart());
        save("heightlend", emp.getHeightlend());
        save("educationm", emp.getEducationm());
        save("marriagem", emp.getMarriagem());

        boolean isFirstRun = getSp().getBoolean("isFirstRunUpdate", true);
        if (isFirstRun) {
            SharedPreferences.Editor editor = getSp().edit();
            editor.putBoolean("isFirstRunUpdate", false);
            editor.commit();
            Intent intent = new Intent(RegUpdateActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            //已经登录过了
            finish();
        }

    }
    public void showDialogPhoto(){
        photosWindow = new SelectPhotoPopWindow(RegUpdateActivity.this, itemsOnClickPhoto);
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

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     *            屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) RegUpdateActivity.this).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) RegUpdateActivity.this).getWindow().setAttributes(lp);
    }

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
            case 1000:
            {
                if(resultCode == 10001){
                    City cityObj = (City) data.getExtras().get("cityObj");
                    Province provinceObj = (Province) data.getExtras().get("provinceObj");
                    if(provinceObj != null && cityObj != null){
                        address.setText(provinceObj.getPname() + cityObj.getCityName());
                        provinceid = provinceObj.getProvinceid();
                        cityid = cityObj.getCityid();
                    }
                }
            }
                break;
            case 1001:
            {
                if(resultCode == 1001){
                    String likeNames = (String) data.getExtras().get("likeNames");
                    likeids = (String) data.getExtras().get("likesids");
                    if(!StringUtil.isNullOrEmpty(likeNames)){
                        likes.setText(likeNames);
                    }
                }
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
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoomBg(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 2);
        intent.putExtra("aspectY", 3);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 300);
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
                cover.setImageBitmap(photo);
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
                                    updateCover(key);
                                }
                            }, null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    //更新
    private void updateCover(final String uploadpic) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appUpdateCover,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if(Integer.parseInt(code) == 200) {
                                    save("cover", InternetURL.QINIU_URL + uploadpic);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                params.put("empid", empid);
                params.put("cover", uploadpic);
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

    //----点击弹框--
    public void showPopEducation(){
        popEducationWindow = new PopEducationWindow(RegUpdateActivity.this, itemsOnClickEducation);
        //显示窗口
        setBackgroundAlpha(0.5f);//设置屏幕透明度

        popEducationWindow.setBackgroundDrawable(new BitmapDrawable());
        popEducationWindow.setFocusable(true);
        popEducationWindow.showAtLocation(this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popEducationWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
    }

    private View.OnClickListener itemsOnClickEducation = new View.OnClickListener() {
        public void onClick(View v) {
            popEducationWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn1: {
                    education.setText("高中及以下");
                    educationID = "1";
                }
                break;
                case R.id.btn2: {
                    education.setText("中专");
                    educationID = "2";
                }
                break;
                case R.id.btn3: {
                    education.setText("专科");
                    educationID = "3";
                }
                break;
                case R.id.btn4: {
                    education.setText("本科");
                    educationID = "4";
                }
                break;
                case R.id.btn5: {
                    education.setText("研究生及以上");
                    educationID = "5";
                }
                break;
                default:
                    break;
            }
        }
    };

    public void showPopMarry(){
        popMarryWindow = new PopMarryWindow(RegUpdateActivity.this, itemsOnClickMarry);
        //显示窗口
        setBackgroundAlpha(0.5f);//设置屏幕透明度

        popMarryWindow.setBackgroundDrawable(new BitmapDrawable());
        popMarryWindow.setFocusable(true);
        popMarryWindow.showAtLocation(this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popMarryWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
    }

    private View.OnClickListener itemsOnClickMarry = new View.OnClickListener() {
        public void onClick(View v) {
            popMarryWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn1: {
                    marragie.setText("未婚");
                    marragieID = "1";
                }
                break;
                case R.id.btn2: {
                    marragie.setText("离异");
                    marragieID = "2";
                }
                break;
                case R.id.btn3: {
                    marragie.setText("丧偶");
                    marragieID = "3";
                }
                break;
                default:
                    break;
            }
        }
    };

    private List<String> arrays1 = new ArrayList<String>();
    private List<String> arrays2 = new ArrayList<String>();

    public void showPopAge(){
        arrays1.add("不限");
        arrays1.add("1970");
        arrays1.add("1971");
        arrays1.add("1972");
        arrays1.add("1973");
        arrays1.add("1974");
        arrays1.add("1975");
        arrays1.add("1976");
        arrays1.add("1977");
        arrays1.add("1978");
        arrays1.add("1979");
        arrays1.add("1980");
        arrays1.add("1981");
        arrays1.add("1982");
        arrays1.add("1983");
        arrays1.add("1984");
        arrays1.add("1985");
        arrays1.add("1986");
        arrays1.add("1987");
        arrays1.add("1988");
        arrays1.add("1989");
        arrays1.add("1990");
        arrays1.add("1991");
        arrays1.add("1992");
        arrays1.add("1993");
        arrays1.add("1994");
        arrays1.add("1995");
        arrays1.add("1996");
        arrays1.add("1997");
        arrays1.add("1998");
        arrays1.add("1999");

        arrays2.add("不限");
        arrays2.add("1999");
        arrays2.add("1998");
        arrays2.add("1997");
        arrays2.add("1996");
        arrays2.add("1995");
        arrays2.add("1994");
        arrays2.add("1993");
        arrays2.add("1992");
        arrays2.add("1991");
        arrays2.add("1990");
        arrays2.add("1989");
        arrays2.add("1988");
        arrays2.add("1987");
        arrays2.add("1986");
        arrays2.add("1985");
        arrays2.add("1984");
        arrays2.add("1983");
        arrays2.add("1982");
        arrays2.add("1981");
        arrays2.add("1980");
        arrays2.add("1979");
        arrays2.add("1978");
        arrays2.add("1977");
        arrays2.add("1976");
        arrays2.add("1975");
        arrays2.add("1974");
        arrays2.add("1973");
        arrays2.add("1972");
        arrays2.add("1971");
        arrays2.add("1970");

        popAgeWindow = new PopAgeWindow(RegUpdateActivity.this ,arrays1 , arrays2);
        //显示窗口
        setBackgroundAlpha(0.5f);//设置屏幕透明度
        popAgeWindow.setOnClickContentItemListener(this);

        popAgeWindow.setBackgroundDrawable(new BitmapDrawable());
        popAgeWindow.setFocusable(true);
        popAgeWindow.showAtLocation(this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popAgeWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
    }


    private List<String> arrays5 = new ArrayList<String>();

    public void showAgeProfile(){
        arrays5.add("1970");
        arrays5.add("1971");
        arrays5.add("1972");
        arrays5.add("1973");
        arrays5.add("1974");
        arrays5.add("1975");
        arrays5.add("1976");
        arrays5.add("1977");
        arrays5.add("1978");
        arrays5.add("1979");
        arrays5.add("1980");
        arrays5.add("1981");
        arrays5.add("1982");
        arrays5.add("1983");
        arrays5.add("1984");
        arrays5.add("1985");
        arrays5.add("1986");
        arrays5.add("1987");
        arrays5.add("1988");
        arrays5.add("1989");
        arrays5.add("1990");
        arrays5.add("1991");
        arrays5.add("1992");
        arrays5.add("1993");
        arrays5.add("1994");
        arrays5.add("1995");
        arrays5.add("1996");
        arrays5.add("1997");
        arrays5.add("1998");
        arrays5.add("1999");

        popAgeProfileWindow = new PopAgeProfileWindow(RegUpdateActivity.this ,arrays5);
        //显示窗口
        setBackgroundAlpha(0.5f);//设置屏幕透明度
        popAgeProfileWindow.setOnClickContentItemListener(this);

        popAgeProfileWindow.setBackgroundDrawable(new BitmapDrawable());
        popAgeProfileWindow.setFocusable(true);
        popAgeProfileWindow.showAtLocation(this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popAgeProfileWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
    }


    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        switch (flag){
            case 0:
            {
                popAgeWindow.dismiss();
                String ageStr = (String) object;
                if(!StringUtil.isNullOrEmpty(ageStr)){
                    String[] arrs = ageStr.split(",");
                    if(arrs != null){
                        if(arrs.length > 1){
                            age_marry.setText(arrs[0] + "-" + arrs[1]);
                            if("不限".equals(arrs[0])){
                                agestart = "1970";
                            }else {
                                agestart = arrs[0];
                            }
                            if("不限".equals(arrs[1])){
                                ageend = "1999";
                            }else {
                                ageend = arrs[1];
                            }
                        }
                    }
                }
            }
                break;
            case 1:
            {
                popHeightlWindow.dismiss();
                String ageStr = (String) object;
                if(!StringUtil.isNullOrEmpty(ageStr)){
                    String[] arrs = ageStr.split(",");
                    if(arrs != null){
                        if(arrs.length > 1){
                            heightl_marry.setText(arrs[0] + "-" + arrs[1]);
                            if("不限".equals(arrs[0])){
                                heightlstart = "150";
                            }else {
                                heightlstart = arrs[0];
                            }
                            if("不限".equals(arrs[1])){
                                heightlend = "190";
                            }else {
                                heightlend = arrs[1];
                            }
                        }
                    }
                }
            }
                break;
            case 3:
            {
                popAgeProfileWindow.dismiss();
                ageStr = (String) object;
                if(!StringUtil.isNullOrEmpty(ageStr)){
                    age.setText(ageStr);
                }
            }
                break;
            case 4:
            {
                popHeightlProfileWindow.dismiss();
                heightlStr = (String) object;
                if(!StringUtil.isNullOrEmpty(heightlStr)){
                    heightl.setText(heightlStr);
                }
            }
            break;
        }
    }

    private List<String> arrays3 = new ArrayList<String>();
    private List<String> arrays4 = new ArrayList<String>();

    public void showPopHeightl(){
        arrays3.add("不限");
        arrays3.add("150");
        arrays3.add("151");
        arrays3.add("152");
        arrays3.add("153");
        arrays3.add("154");
        arrays3.add("155");
        arrays3.add("156");
        arrays3.add("157");
        arrays3.add("158");
        arrays3.add("159");
        arrays3.add("160");
        arrays3.add("161");
        arrays3.add("162");
        arrays3.add("153");
        arrays3.add("163");
        arrays3.add("164");
        arrays3.add("165");
        arrays3.add("166");
        arrays3.add("167");
        arrays3.add("168");
        arrays3.add("169");
        arrays3.add("170");
        arrays3.add("171");
        arrays3.add("172");
        arrays3.add("173");
        arrays3.add("174");
        arrays3.add("175");
        arrays3.add("176");
        arrays3.add("177");
        arrays3.add("178");
        arrays3.add("179");
        arrays3.add("180");
        arrays3.add("181");
        arrays3.add("182");
        arrays3.add("183");
        arrays3.add("184");
        arrays3.add("185");
        arrays3.add("186");
        arrays3.add("187");
        arrays3.add("188");
        arrays3.add("189");
        arrays3.add("190");


        arrays4.add("不限");
        arrays4.add("190");
        arrays4.add("189");
        arrays4.add("188");
        arrays4.add("187");
        arrays4.add("186");
        arrays4.add("185");
        arrays4.add("184");
        arrays4.add("183");
        arrays4.add("182");
        arrays4.add("181");
        arrays4.add("180");
        arrays4.add("179");
        arrays4.add("178");
        arrays4.add("177");
        arrays4.add("176");
        arrays4.add("175");
        arrays4.add("174");
        arrays4.add("173");
        arrays4.add("172");
        arrays4.add("171");
        arrays4.add("170");
        arrays4.add("169");
        arrays4.add("168");
        arrays4.add("167");
        arrays4.add("166");
        arrays4.add("165");
        arrays4.add("164");
        arrays4.add("163");
        arrays4.add("162");
        arrays4.add("161");
        arrays4.add("160");
        arrays4.add("159");
        arrays4.add("158");
        arrays4.add("157");
        arrays4.add("156");
        arrays4.add("155");
        arrays4.add("154");
        arrays4.add("153");
        arrays4.add("152");
        arrays4.add("151");
        arrays4.add("150");


        popHeightlWindow = new PopHeightlWindow(RegUpdateActivity.this ,arrays3 , arrays4);
        //显示窗口
        setBackgroundAlpha(0.5f);//设置屏幕透明度
        popHeightlWindow.setOnClickContentItemListener(this);

        popHeightlWindow.setBackgroundDrawable(new BitmapDrawable());
        popHeightlWindow.setFocusable(true);
        popHeightlWindow.showAtLocation(this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popHeightlWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
    }


    private List<String> arrays6 =  new ArrayList<String>();

    public void showPopHeightlProfile(){
        arrays6.add("150");
        arrays6.add("151");
        arrays6.add("152");
        arrays6.add("153");
        arrays6.add("154");
        arrays6.add("155");
        arrays6.add("156");
        arrays6.add("157");
        arrays6.add("158");
        arrays6.add("159");
        arrays6.add("160");
        arrays6.add("161");
        arrays6.add("162");
        arrays6.add("153");
        arrays6.add("163");
        arrays6.add("164");
        arrays6.add("165");
        arrays6.add("166");
        arrays6.add("167");
        arrays6.add("168");
        arrays6.add("169");
        arrays6.add("170");
        arrays6.add("171");
        arrays6.add("172");
        arrays6.add("173");
        arrays6.add("174");
        arrays6.add("175");
        arrays6.add("176");
        arrays6.add("177");
        arrays6.add("178");
        arrays6.add("179");
        arrays6.add("180");
        arrays6.add("181");
        arrays6.add("182");
        arrays6.add("183");
        arrays6.add("184");
        arrays6.add("185");
        arrays6.add("186");
        arrays6.add("187");
        arrays6.add("188");
        arrays6.add("189");
        arrays6.add("190");


        popHeightlProfileWindow = new PopHeightlProfileWindow(RegUpdateActivity.this ,arrays6);
        //显示窗口
        setBackgroundAlpha(0.5f);//设置屏幕透明度
        popHeightlProfileWindow.setOnClickContentItemListener(this);
        popHeightlProfileWindow.setBackgroundDrawable(new BitmapDrawable());
        popHeightlProfileWindow.setFocusable(true);
        popHeightlProfileWindow.showAtLocation(this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popHeightlProfileWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
    }

    public void showPopEducation2(){
        popEducationWindow = new PopEducationWindow(RegUpdateActivity.this, itemsOnClickEducation2);
        //显示窗口
        setBackgroundAlpha(0.5f);//设置屏幕透明度

        popEducationWindow.setBackgroundDrawable(new BitmapDrawable());
        popEducationWindow.setFocusable(true);
        popEducationWindow.showAtLocation(this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popEducationWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
    }

    private View.OnClickListener itemsOnClickEducation2 = new View.OnClickListener() {
        public void onClick(View v) {
            popEducationWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn1: {
                    education_marry.setText("高中及以下");
                    educationID2 = "1";
                }
                break;
                case R.id.btn2: {
                    education_marry.setText("中专");
                    educationID2 = "2";
                }
                break;
                case R.id.btn3: {
                    education_marry.setText("专科");
                    educationID2 = "3";
                }
                break;
                case R.id.btn4: {
                    education_marry.setText("本科");
                    educationID2 = "4";
                }
                break;
                case R.id.btn5: {
                    education_marry.setText("研究生及以上");
                    educationID2 = "5";
                }
                break;
                default:
                    break;
            }
        }
    };

    public void showPopMarry2(){
        popMarryWindow = new PopMarryWindow(RegUpdateActivity.this, itemsOnClickMarry2);
        //显示窗口
        setBackgroundAlpha(0.5f);//设置屏幕透明度

        popMarryWindow.setBackgroundDrawable(new BitmapDrawable());
        popMarryWindow.setFocusable(true);
        popMarryWindow.showAtLocation(this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popMarryWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
    }

    private View.OnClickListener itemsOnClickMarry2 = new View.OnClickListener() {
        public void onClick(View v) {
            popMarryWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn1: {
                    marry_marry.setText("未婚");
                    marragieID2 = "1";
                }
                break;
                case R.id.btn2: {
                    marry_marry.setText("离异");
                    marragieID2 = "2";
                }
                break;
                case R.id.btn3: {
                    marry_marry.setText("丧偶");
                    marragieID2 = "3";
                }
                break;
                default:
                    break;
            }
        }
    };


    private void getLikes(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appLikesBylikeIds,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    HappyHandLikeData data = getGson().fromJson(s, HappyHandLikeData.class);
                                    if(data != null){
                                        likeLists.clear();
                                        likeLists.addAll(data.getData());
                                        if(likeLists != null){
                                            String str = "";
                                            for(HappyHandLike happyHandLike:likeLists){
                                                if(happyHandLike != null){
                                                    str = str+ happyHandLike.getLikename()+",";
                                                }
                                            }
                                            if(str.length()>1){
                                                str = str.substring(0,str.length()-1);
                                            }
                                            likes.setText(str);
                                        }
                                    }
                                }  else {
                                    showMsg(RegUpdateActivity.this,  jo.getString("message"));
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
                        Toast.makeText(RegUpdateActivity.this, R.string.login_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("likeids", getGson().fromJson(getSp().getString("likeids", ""), String.class));
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
