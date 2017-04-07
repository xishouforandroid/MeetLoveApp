package com.lbins.meetlove.ui;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.lbins.meetlove.R;
import com.lbins.meetlove.base.BaseActivity;
import com.lbins.meetlove.util.StringUtil;
import com.lbins.meetlove.widget.QuitePopWindow;
import com.lbins.meetlove.widget.ReportPopWindow;

/**
 * Created by zhl on 2016/8/30.
 */
public class AddReportActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private EditText nickname;
    private EditText content;
    private Button btn_1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_report_activity);
        initView();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("投诉");
        nickname = (EditText) this.findViewById(R.id.nickname);
        content = (EditText) this.findViewById(R.id.content);
        btn_1 = (Button) this.findViewById(R.id.btn_1);
        btn_1.setOnClickListener(this);

        //设置监听  随时更改注册按钮的状态
        content.addTextChangedListener(watcher);
        nickname.addTextChangedListener(watcher);

        this.findViewById(R.id.btn_tsxz).setOnClickListener(this);
        this.findViewById(R.id.btn_kfrx).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_1:
            {
                //提交
                if(StringUtil.isNullOrEmpty(content.getText().toString())){
                    showMsg(AddReportActivity.this, "请输入要反馈的内容！");
                    return;
                }
            }
                break;
            case R.id.btn_tsxz:
            {
                //投诉须知
                showReport();
            }
                break;
            case R.id.btn_kfrx:
            {
                //客服热线
            }
            break;
        }
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
            if(!StringUtil.isNullOrEmpty(content.getText().toString()) && !StringUtil.isNullOrEmpty(nickname.getText().toString())){
                btn_1.setBackground(getDrawable(R.drawable.btn_big_active));
            }
        }
    };

    private ReportPopWindow reportPopWindow;
    private void showReport() {
        reportPopWindow = new ReportPopWindow(AddReportActivity.this, reportOnClick);
        //显示窗口
        setBackgroundAlpha(0.5f);//设置屏幕透明度
        reportPopWindow.setBackgroundDrawable(new BitmapDrawable());
        reportPopWindow.setFocusable(true);
        reportPopWindow.showAtLocation(this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        reportPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
    }
    private View.OnClickListener reportOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            reportPopWindow.dismiss();
            switch (v.getId()) {
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
        WindowManager.LayoutParams lp = ((Activity) AddReportActivity.this).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) AddReportActivity.this).getWindow().setAttributes(lp);
    }


}
