package com.lbins.meetlove.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.lbins.meetlove.R;
import com.lbins.meetlove.base.BaseActivity;

/**
 * Created by zhl on 2016/8/30.
 */
public class GroupDetailActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private ImageView cover;
    private TextView name;
    private TextView content;
    private Button btn_1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_detail_activity);
        initView();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("电影群");
        cover = (ImageView) this.findViewById(R.id.cover);
        name = (TextView) this.findViewById(R.id.name);
        content = (TextView) this.findViewById(R.id.content);
        btn_1 = (Button) this.findViewById(R.id.btn_1);
        btn_1.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_1:
            {
                //加群
            }
                break;
        }
    }
}
