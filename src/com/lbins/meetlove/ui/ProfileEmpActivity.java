package com.lbins.meetlove.ui;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.lbins.meetlove.R;
import com.lbins.meetlove.adapter.ItemPicAdapter;
import com.lbins.meetlove.base.BaseActivity;
import com.lbins.meetlove.widget.PictureGridview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhl on 2016/8/30.
 */
public class ProfileEmpActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;

    private Resources res;
    private ImageView cover;
    private TextView nickname;
    private TextView is_tuijian;
    private TextView is_state;
    private TextView sign;
    private ImageView vip_1;
    private TextView vip_2;
    private ImageView vip_3;
    private TextView vip_4;
    private TextView age;
    private TextView heightl;
    private TextView address;

    private PictureGridview gridview;
    private ItemPicAdapter adapterGrid;
    private List<String> picLists = new ArrayList<String>();

    private String empid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_emp_activity);
        empid = getIntent().getExtras().getString("empid");
        res = getResources();
        initView();
        initView();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.VISIBLE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("会员资料");

        picLists.add("");
        picLists.add("");
        picLists.add("");
        gridview = (PictureGridview) this.findViewById(R.id.gridview);
        adapterGrid = new ItemPicAdapter(picLists, ProfileEmpActivity.this);
        gridview.setAdapter(adapterGrid);
        gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));

        cover = (ImageView) this.findViewById(R.id.cover);
        nickname = (TextView) this.findViewById(R.id.nickname);
        is_tuijian = (TextView) this.findViewById(R.id.is_tuijian);
        is_state = (TextView) this.findViewById(R.id.is_state);
        sign = (TextView) this.findViewById(R.id.sign);
        vip_1 = (ImageView) this.findViewById(R.id.vip_1);
        vip_2 = (TextView) this.findViewById(R.id.vip_2);
        vip_3 = (ImageView) this.findViewById(R.id.vip_3);
        vip_4 = (TextView) this.findViewById(R.id.vip_4);
        age = (TextView) this.findViewById(R.id.age);
        heightl = (TextView) this.findViewById(R.id.heightl);
        address = (TextView) this.findViewById(R.id.address);

        cover.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.cover:
            {
                //头像
            }
                break;
        }
    }

    public void AddToFriends(View view){
        //
    }
}
