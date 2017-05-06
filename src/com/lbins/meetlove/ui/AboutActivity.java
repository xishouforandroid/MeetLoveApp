package com.lbins.meetlove.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import com.lbins.meetlove.R;
import com.lbins.meetlove.adapter.AboutViewPageAdapter;
import com.lbins.meetlove.base.BaseActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AboutActivity extends BaseActivity implements View.OnClickListener {
    private static final int PICTURE_COUNT = 2;

    private static final int[] PICTURE_RESOURCES = {R.drawable.intropage_1,
            R.drawable.intropage_2};

    private static final String[] PICTURE_TITLE = {"", ""};
    private JSONArray jsonArray;
    private ViewPager viewPager;
    private AboutViewPageAdapter adapter;
    private ImageView[] circles = new ImageView[PICTURE_RESOURCES.length];
    private TextView txt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_viewpage);
        initLoadData();
        initView();
    }

    private void initLoadData() {
        jsonArray = new JSONArray();
        for (int i = 0; i < PICTURE_COUNT; i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("resourceId", PICTURE_RESOURCES[i]);
                jsonObject.put("title", PICTURE_TITLE[i]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }
    }

    private void initView() {
        txt = (TextView) this.findViewById(R.id.txt);
        txt.setVisibility(View.GONE);
        txt.setOnClickListener(this);
        viewPager = (ViewPager) findViewById(R.id.viewpage);
        adapter = new AboutViewPageAdapter(this, jsonArray);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }
            @Override
            public void onPageSelected(int position) {
                for (int j = 0; j < circles.length; j++) {
                    if(j == 1){
                        txt.setVisibility(View.VISIBLE);
                    }else {
                        txt.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt:
            {
                Intent intent = new Intent(AboutActivity.this, LoginActivity.class);
                startActivity(intent);
            }
                break;
        }
    }
}
