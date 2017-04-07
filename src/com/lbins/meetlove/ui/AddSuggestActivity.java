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
public class AddSuggestActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private EditText content;
    private Button btn_1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_suggest_activity);
        initView();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("反馈");
        content = (EditText) this.findViewById(R.id.content);
        btn_1 = (Button) this.findViewById(R.id.btn_1);
        btn_1.setOnClickListener(this);

        //设置监听  随时更改注册按钮的状态
        content.addTextChangedListener(watcher);
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
                    showMsg(AddSuggestActivity.this, "请输入要反馈的内容！");
                    return;
                }
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
            if(!StringUtil.isNullOrEmpty(content.getText().toString())){
                btn_1.setBackground(getDrawable(R.drawable.btn_big_active));
            }
        }
    };

}
