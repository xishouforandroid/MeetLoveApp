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
public class MineMsgActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_msg_activity);
        initView();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("系统通知");

        this.findViewById(R.id.relate_one).setOnClickListener(this);
        this.findViewById(R.id.relate_two).setOnClickListener(this);
        this.findViewById(R.id.relate_three).setOnClickListener(this);
        this.findViewById(R.id.relate_four).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.relate_one:
            {
                //系统消息
                Intent intent = new Intent(MineMsgActivity.this, MessagesActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.relate_two:
            {
                //系统资讯
                Intent intent = new Intent(MineMsgActivity.this, NewsActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.relate_three:
            {
                //活动公告
                Intent intent = new Intent(MineMsgActivity.this, NoticesActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.relate_four:
            {
                //交往消息
                Intent intent = new Intent(MineMsgActivity.this, JwdxApplyActivity.class);
                startActivity(intent);
            }
                break;
        }
    }
}
