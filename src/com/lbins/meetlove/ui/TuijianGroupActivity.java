package com.lbins.meetlove.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.lbins.meetlove.R;
import com.lbins.meetlove.adapter.ItemTuijianGroupAdapter;
import com.lbins.meetlove.adapter.ItemTuijianPeopleAdapter;
import com.lbins.meetlove.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhl on 2016/8/30.
 */
public class TuijianGroupActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private ListView lstv;
    private ItemTuijianGroupAdapter adapter;
    private List<String> lists = new ArrayList<>();
    private ImageView search_null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tuijian_group_activity);
        initView();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.btn_right).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("推荐群列表");

        search_null = (ImageView) this.findViewById(R.id.search_null);
        search_null.setVisibility(View.GONE);
        lists.add("");
        lists.add("");
        lists.add("");
        lstv = (ListView) this.findViewById(R.id.lstv);
        adapter = new ItemTuijianGroupAdapter(lists, TuijianGroupActivity.this);
        lstv.setAdapter(adapter);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TuijianGroupActivity.this,GroupDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
}
