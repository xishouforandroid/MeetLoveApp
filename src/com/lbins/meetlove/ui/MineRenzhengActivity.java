package com.lbins.meetlove.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.lbins.meetlove.R;
import com.lbins.meetlove.base.BaseActivity;
import com.lbins.meetlove.widget.SelectPhotoPopWindow;
import com.lbins.meetlove.widget.SelectRzxfPopWindow;
import com.lbins.meetlove.widget.SelectSuggestPopWindow;

/**
 * Created by zhl on 2016/8/30.
 */
public class MineRenzhengActivity extends BaseActivity implements View.OnClickListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.renzheng_activity);
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
                showDialogPhoto();
            }
                break;
            case R.id.btn_2:
            {
                //会员认证
                Intent intent = new Intent(MineRenzhengActivity.this, PayEmpRzActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.btn_3:
            {
                //诚信认证
                Intent intent = new Intent(MineRenzhengActivity.this, PayEmpCxActivity.class);
                startActivity(intent);
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

    private View.OnClickListener itemsOnClickPhoto = new View.OnClickListener() {
        public void onClick(View v) {
            photosWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_photo: {
                    showMsg(MineRenzhengActivity.this, "相册");
                }
                break;
                case R.id.btn_camera: {
                    showMsg(MineRenzhengActivity.this, "拍照");
                }
                break;
                default:
                    break;
            }
        }
    };

}
