package com.lbins.meetlove.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.lbins.meetlove.R;
import com.lbins.meetlove.base.ActivityTack;
import com.lbins.meetlove.base.BaseActivity;
import com.lbins.meetlove.widget.QuitePopWindow;
import com.lbins.meetlove.widget.SelectSuggestPopWindow;
import com.lbins.meetlove.widget.UpdatePwrPopWindow;

/**
 * Created by zhl on 2016/8/30.
 */
public class MineSettingActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private ImageView btn_switch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_setting_activity);
        initView();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("设置");
        btn_switch= (ImageView) this.findViewById(R.id.btn_switch);
        btn_switch.setOnClickListener(this);

        this.findViewById(R.id.liner_mobile).setOnClickListener(this);
        this.findViewById(R.id.liner_pwr).setOnClickListener(this);
        this.findViewById(R.id.liner_black).setOnClickListener(this);
        this.findViewById(R.id.liner_suggest).setOnClickListener(this);
        this.findViewById(R.id.liner_about).setOnClickListener(this);
        this.findViewById(R.id.liner_quite).setOnClickListener(this);
    }

    private SelectSuggestPopWindow selectSuggestPopWindow;

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_switch:
                break;
            case R.id.liner_mobile:
            {
                //手机号码
                Intent intent = new Intent(MineSettingActivity.this, UpdateMobileActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.liner_pwr:
            {
                //密码
                showPwrUpdate();
            }
                break;
            case R.id.liner_black:
            {
                //黑名单
            }
                break;
            case R.id.liner_suggest:
            {
                //建议与反馈
                showDialog();
            }
                break;
            case R.id.liner_about:
            {
                //关于我们
                Intent intent = new Intent(MineSettingActivity.this, AboutSettingActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.liner_quite:
            {
                //退出登录
                showQuite();
            }
                break;
        }
    }

    private void showDialog() {
        selectSuggestPopWindow = new SelectSuggestPopWindow(MineSettingActivity.this, itemsOnClick);
        //显示窗口
        setBackgroundAlpha(0.5f);//设置屏幕透明度

        selectSuggestPopWindow.setBackgroundDrawable(new BitmapDrawable());
        selectSuggestPopWindow.setFocusable(true);
        selectSuggestPopWindow.showAtLocation(this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        selectSuggestPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
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
        WindowManager.LayoutParams lp = ((Activity) MineSettingActivity.this).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) MineSettingActivity.this).getWindow().setAttributes(lp);
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            selectSuggestPopWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_suggest: {
                    Intent intent = new Intent(MineSettingActivity.this, AddSuggestActivity.class);
                    startActivity(intent);
                }
                break;
                case R.id.btn_report: {
                    Intent intent = new Intent(MineSettingActivity.this, AddReportActivity.class);
                    startActivity(intent);
                }
                break;
                default:
                    break;
            }
        }
    };

    private QuitePopWindow quitePopWindow;
    private void showQuite() {
        quitePopWindow = new QuitePopWindow(MineSettingActivity.this, quiteOnClick);
        //显示窗口
        setBackgroundAlpha(0.5f);//设置屏幕透明度
        quitePopWindow.setBackgroundDrawable(new BitmapDrawable());
        quitePopWindow.setFocusable(true);
        quitePopWindow.showAtLocation(this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        quitePopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
    }
    private View.OnClickListener quiteOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            quitePopWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_quite: {
                    save("password", "");
                    ActivityTack.getInstanse().popUntilActivity(LoginActivity.class);
                }
                break;
                default:
                    break;
            }
        }
    };

    private UpdatePwrPopWindow updatePwrPopWindow;
    private void showPwrUpdate() {
        updatePwrPopWindow = new UpdatePwrPopWindow(MineSettingActivity.this, updatePwrOnClick);
        //显示窗口
        setBackgroundAlpha(0.5f);//设置屏幕透明度
        updatePwrPopWindow.setBackgroundDrawable(new BitmapDrawable());
        updatePwrPopWindow.setFocusable(true);
        updatePwrPopWindow.showAtLocation(this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        updatePwrPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
    }
    private View.OnClickListener updatePwrOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            updatePwrPopWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_update: {
                    Intent intent = new Intent(MineSettingActivity.this, UpdatePwrActivity.class);
                    startActivity(intent);
                }
                break;
                case R.id.btn_chongzhi: {
                    Intent intent = new Intent(MineSettingActivity.this, ForgetPwrActivity.class);
                    startActivity(intent);
                }
                break;
                default:
                    break;
            }
        }
    };





}
