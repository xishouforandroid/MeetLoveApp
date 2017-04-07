package com.lbins.meetlove.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.lbins.meetlove.R;
import com.lbins.meetlove.base.BaseActivity;

/**
 * Created by zhl on 2016/8/30.
 */
public class PayEmpRzActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;

    private Button btn_1;
    private Button btn_2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_emp_rz_activity);
        initView();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("服务费支付");
        btn_1 = (Button) this.findViewById(R.id.btn_1);
        btn_2 = (Button) this.findViewById(R.id.btn_2);

        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_1:
            {
                //点击微信支付
                showMsg(PayEmpRzActivity.this, "微信支付");
            }
                break;
            case R.id.btn_2:
            {
                //支付宝支付
                showMsg(PayEmpRzActivity.this, "支付宝支付");
            }
                break;
        }
    }
}
