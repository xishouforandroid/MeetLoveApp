package com.lbins.meetlove.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.lbins.meetlove.R;
import com.lbins.meetlove.base.BaseActivity;
import com.lbins.meetlove.util.StringUtil;

/**
 * Created by zhl on 2016/8/30.
 */
public class UpdatePwrActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private EditText pwr1;
    private EditText pwr2;
    private EditText pwr3;
    private Button btn_save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_pwr_activity);
        initView();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("密码修改");

        pwr1 = (EditText) this.findViewById(R.id.pwr1);
        pwr2 = (EditText) this.findViewById(R.id.pwr2);
        pwr3 = (EditText) this.findViewById(R.id.pwr3);
        btn_save = (Button) this.findViewById(R.id.btn_save);

        pwr1.addTextChangedListener(watcher);
        pwr2.addTextChangedListener(watcher);
        pwr3.addTextChangedListener(watcher);

        btn_save.setOnClickListener(this);
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
            if(!StringUtil.isNullOrEmpty(pwr1.getText().toString()) && !StringUtil.isNullOrEmpty(pwr2.getText().toString()) && !StringUtil.isNullOrEmpty(pwr3
            .getText().toString()) && pwr2.getText().toString().equals(pwr3.getText().toString())){
                btn_save.setBackground(getDrawable(R.drawable.btn_big_active));
            }else {
                btn_save.setBackground(getDrawable(R.drawable.btn_big_unactive));
            }
        }
    };
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_save:
            {
                if(StringUtil.isNullOrEmpty(pwr1.getText().toString())){
                    showMsg(UpdatePwrActivity.this, "请输入原始密码！");
                    return;
                }
                if(StringUtil.isNullOrEmpty(pwr2.getText().toString())){
                    showMsg(UpdatePwrActivity.this, "请输入新密码！");
                    return;
                }
                if(pwr2.getText().toString().length() <6 || pwr2.getText().toString().length() > 18){
                    showMsg(UpdatePwrActivity.this, "新密码长度在6到18位之间！");
                    return;
                }
                if(StringUtil.isNullOrEmpty(pwr3.getText().toString())){
                    showMsg(UpdatePwrActivity.this, "请再次输入确认密码！");
                    return;
                }
                if(!pwr2.getText().toString().equals(pwr3.getText().toString())){
                    showMsg(UpdatePwrActivity.this, "两次输入密码不一致！");
                    return;
                }
            }
                break;
        }
    }
}
