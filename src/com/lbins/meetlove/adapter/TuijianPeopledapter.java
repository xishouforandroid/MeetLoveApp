package com.lbins.meetlove.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.lbins.meetlove.R;
import com.lbins.meetlove.widget.CustomImageView;
import com.lbins.meetlove.widget.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * 推荐首页 推荐人列表
 */
public class TuijianPeopledapter extends BaseAdapter {
    private ViewHolder holder;
    private List<String> contents;
    private Context mContext;

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }


    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    public TuijianPeopledapter(List<String> contents, Context mContext) {
        this.contents = contents;
        this.mContext = mContext;

    }

    @Override
    public int getCount() {
        return contents.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.item_index_tuijian_people, parent, false);
            holder.cover = (RoundImageView) convertView.findViewById(R.id.cover);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final String cell = contents.get(position);//获得元素
        if (cell != null) {
//            imageLoader.displayImage(cell.getAd_pic(), holder.cover, MeirenmeibaAppApplication.options, animateFirstListener);

        }
        return convertView;
    }

    class ViewHolder {
        RoundImageView cover;
    }

}
