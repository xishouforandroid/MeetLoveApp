package com.lbins.meetlove.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.lbins.meetlove.MeetLoveApplication;
import com.lbins.meetlove.R;
import com.lbins.meetlove.adapter.AnimateFirstDisplayListener;
import com.lbins.meetlove.adapter.ItemPicAdapter;
import com.lbins.meetlove.base.BaseFragment;
import com.lbins.meetlove.ui.MineRenzhengActivity;
import com.lbins.meetlove.ui.MineSettingActivity;
import com.lbins.meetlove.ui.RegUpdateActivity;
import com.lbins.meetlove.util.StringUtil;
import com.lbins.meetlove.widget.PictureGridview;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhl on 2016/7/1.
 * 推荐
 */
public class FourFragment extends BaseFragment implements View.OnClickListener  {
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private View view;
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
    private TextView jwdx_txt;

    private PictureGridview gridview;
    private ItemPicAdapter adapterGrid;
    private List<String> picLists = new ArrayList<String>();

    private ImageView btn_right;

    private TextView txt_pic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.four_fragment, null);
        res = getActivity().getResources();
        initView();
        initData();
        return view;
    }

    private void initData() {
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("nickname", ""), String.class))){
            nickname.setText(getGson().fromJson(getSp().getString("nickname", ""), String.class));
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("cover", ""), String.class))){
            imageLoader.displayImage(getGson().fromJson(getSp().getString("cover", ""), String.class), cover, MeetLoveApplication.txOptions, animateFirstListener);
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("state", ""), String.class))){
            if("1".equals(getGson().fromJson(getSp().getString("state", ""), String.class))){
                //单身
                is_state.setText("单身");
            }
            if("2".equals(getGson().fromJson(getSp().getString("state", ""), String.class))){
                //交往中
                is_state.setText("交往中");
            }
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("sign", ""), String.class))){
            sign.setText("个性签名:"+getGson().fromJson(getSp().getString("sign", ""), String.class));
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("age", ""), String.class))){
            age.setText(getGson().fromJson(getSp().getString("age", ""), String.class) + "年");
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("heightl", ""), String.class))){
            heightl.setText(getGson().fromJson(getSp().getString("heightl", ""), String.class) + "cm");
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("cityName", ""), String.class))){
            address.setText(getGson().fromJson(getSp().getString("cityName", ""), String.class));
        }

        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("rzstate1", ""), String.class))){
            if("1".equals(getGson().fromJson(getSp().getString("rzstate1", ""), String.class))){
                //进行身份认证了
                vip_2.setTextColor(res.getColor(R.color.main_color));
            }else {
                vip_2.setTextColor(res.getColor(R.color.textColortwo));
            }
        }else {
            vip_2.setTextColor(res.getColor(R.color.textColortwo));
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("rzstate2", ""), String.class))){
            if("1".equals(getGson().fromJson(getSp().getString("rzstate2", ""), String.class))){
                vip_1.setImageDrawable(res.getDrawable(R.drawable.icon_verify_id_enabled));
            }else {
                vip_1.setImageDrawable(res.getDrawable(R.drawable.icon_verify_id_disable));
            }
        }else {
            vip_1.setImageDrawable(res.getDrawable(R.drawable.icon_verify_id_disable));
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("rzstate3", ""), String.class))){
            if("1".equals(getGson().fromJson(getSp().getString("rzstate3", ""), String.class))){
                //进行身份认证了
                vip_3.setImageDrawable(res.getDrawable(R.drawable.icon_verify_honesty_enabled));
                vip_4.setTextColor(res.getColor(R.color.main_color));
            }else {
                vip_3.setImageDrawable(res.getDrawable(R.drawable.icon_verify_honesty_disable));
                vip_4.setTextColor(res.getColor(R.color.textColortwo));
            }
        }else {
            vip_3.setImageDrawable(res.getDrawable(R.drawable.icon_verify_honesty_disable));
            vip_4.setTextColor(res.getColor(R.color.textColortwo));
        }

    }

    void initView(){
        picLists.add("");
        picLists.add("");
        picLists.add("");
        gridview = (PictureGridview) view.findViewById(R.id.gridview);
        adapterGrid = new ItemPicAdapter(picLists, getActivity());
        gridview.setAdapter(adapterGrid);
        gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        cover = (ImageView) view.findViewById(R.id.cover);
        nickname = (TextView) view.findViewById(R.id.nickname);
        is_tuijian = (TextView) view.findViewById(R.id.is_tuijian);
        is_state = (TextView) view.findViewById(R.id.is_state);
        sign = (TextView) view.findViewById(R.id.sign);
        vip_1 = (ImageView) view.findViewById(R.id.vip_1);
        vip_2 = (TextView) view.findViewById(R.id.vip_2);
        vip_3 = (ImageView) view.findViewById(R.id.vip_3);
        vip_4 = (TextView) view.findViewById(R.id.vip_4);
        age = (TextView) view.findViewById(R.id.age);
        heightl = (TextView) view.findViewById(R.id.heightl);
        address = (TextView) view.findViewById(R.id.address);
        jwdx_txt = (TextView) view.findViewById(R.id.jwdx_txt);
        btn_right = (ImageView) view.findViewById(R.id.btn_right);
        btn_right.setImageDrawable(res.getDrawable(R.drawable.icon_navbar_search));

        view.findViewById(R.id.liner_photo).setOnClickListener(this);
        view.findViewById(R.id.liner_friends).setOnClickListener(this);
        view.findViewById(R.id.liner_rz).setOnClickListener(this);
        view.findViewById(R.id.liner_share).setOnClickListener(this);
        view.findViewById(R.id.liner_set).setOnClickListener(this);
        cover.setOnClickListener(this);
        sign.setOnClickListener(this);
        btn_right.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_right:
            {
                //搜索
            }
                break;
            case R.id.liner_photo:
            {
                //相册
            }
            break;
            case R.id.liner_friends:
            {
                //对象
            }
            break;
            case R.id.liner_rz:
            {
                //认证
                Intent intent = new Intent(getActivity(), MineRenzhengActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.liner_share:
            {
                //分享
            }
            break;
            case R.id.liner_set:
            {
                //设置
                Intent intent = new Intent(getActivity(), MineSettingActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.cover:
            {
                //头像点击
                Intent intent =  new Intent(getActivity(), RegUpdateActivity.class);
                intent.putExtra("empid", getGson().fromJson(getSp().getString("empid", ""), String.class));
                startActivity(intent);
            }
                break;
            case R.id.sign:
            {
                //签名
            }
                break;
        }
    }

}
