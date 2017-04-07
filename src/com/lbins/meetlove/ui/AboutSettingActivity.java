package com.lbins.meetlove.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.lbins.meetlove.R;
import com.lbins.meetlove.base.BaseActivity;

/**
 * Created by zhl on 2016/8/30.
 */
public class AboutSettingActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private TextView version;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        initView();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        version = (TextView) this.findViewById(R.id.version);
        title.setText("关于幸福牵手吧");

        this.findViewById(R.id.liner_about).setOnClickListener(this);
        this.findViewById(R.id.liner_version).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.liner_about:
            {
                //关于我们
                Intent intent = new Intent(AboutSettingActivity.this, AboutCompanyActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.liner_version:
            {
                //版本更新
            }
                break;
        }
    }
}
