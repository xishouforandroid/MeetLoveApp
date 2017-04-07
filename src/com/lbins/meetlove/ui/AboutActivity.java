package com.lbins.meetlove.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.widget.ImageView;
import com.lbins.meetlove.R;
import com.lbins.meetlove.adapter.AboutViewPageAdapter;
import com.lbins.meetlove.base.BaseActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AboutActivity extends BaseActivity {
    private static final int PICTURE_COUNT = 2;

    private static final int[] PICTURE_RESOURCES = {R.drawable.about_one,
            R.drawable.about_two};

    private static final String[] PICTURE_TITLE = {"", ""};
    private JSONArray jsonArray;
    private ViewPager viewPager;
    private AboutViewPageAdapter adapter;
    private ImageView[] circles = new ImageView[PICTURE_RESOURCES.length];

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
                }
            }
            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }
}
