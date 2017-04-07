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
public class ForgetPwrActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private EditText mobile;
    private EditText card;
    private EditText pwr2;
    private EditText pwr3;
    private Button btn_card;
    private Button btn_save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_pwr_activity);
        initView();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("忘记密码");
        mobile = (EditText) this.findViewById(R.id.mobile);
        card = (EditText) this.findViewById(R.id.card);
        pwr2 = (EditText) this.findViewById(R.id.pwr2);
        pwr3 = (EditText) this.findViewById(R.id.pwr3);
        btn_card = (Button) this.findViewById(R.id.btn_card);
        btn_save = (Button) this.findViewById(R.id.btn_save);


        this.findViewById(R.id.btn_tel).setOnClickListener(this);
        pwr2.addTextChangedListener(watcher);
        pwr3.addTextChangedListener(watcher);
        mobile.addTextChangedListener(watcher);
        card.addTextChangedListener(watcher);
        btn_save.setOnClickListener(this);
        btn_card.setOnClickListener(this);
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
            if(!StringUtil.isNullOrEmpty(mobile.getText().toString())
                    && !StringUtil.isNullOrEmpty(card.getText().toString())
                    && !StringUtil.isNullOrEmpty(pwr2.getText().toString())
                    && !StringUtil.isNullOrEmpty(pwr3.getText().toString())
                    && pwr2.getText().toString().equals(pwr3.getText().toString())){
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
            case R.id.btn_tel:
            {
                //联系我们
            }
                break;
            case R.id.btn_card:
            {
                //验证码
            }
                break;
            case R.id.btn_save:
            {
                //保存
            }
                break;
        }
    }
}
