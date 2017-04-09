package com.lbins.meetlove.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.lbins.meetlove.R;
import com.lbins.meetlove.adapter.ItemAgeAdapter;
import com.lbins.meetlove.adapter.OnClickContentItemListener;

import java.util.ArrayList;
import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2015/3/19
 * Time: 20:58
 */
public class PopHeightlProfileWindow extends PopupWindow {
    private TextView  btnSure;
    private ListView lstv1;
    private View mMenuView;
    private ItemAgeAdapter adapter1,adapter2;
    private List<String> arrays1 = new ArrayList<String>();

    private TextView startage;

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }

    public PopHeightlProfileWindow(Activity context, final List<String> arrays1) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.pop_heightl_profile, null);
        this.arrays1 = arrays1;
        lstv1 = (ListView) mMenuView.findViewById(R.id.lstv1);
        btnSure = (TextView) mMenuView.findViewById(R.id.btnSure);
        adapter1 = new ItemAgeAdapter(arrays1, context);
        lstv1.setAdapter(adapter1);

        startage = (TextView) mMenuView.findViewById(R.id.startage);

        lstv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(arrays1.size()>position){
                    String string = arrays1.get(position);
                    startage.setText(string);
                }
            }
        });

        //取消按钮

        //设置按钮监听
//        btnSure.setOnClickListener(itemsOnClick);
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickContentItemListener.onClickContentItem(0, 4, startage.getText().toString());
            }
        });

        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    }

}