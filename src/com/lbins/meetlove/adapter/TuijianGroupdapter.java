package com.lbins.meetlove.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.lbins.meetlove.MeetLoveApplication;
import com.lbins.meetlove.R;
import com.lbins.meetlove.dao.HappyHandGroup;
import com.lbins.meetlove.widget.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * 推荐首页 推荐群列表
 */
public class TuijianGroupdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<HappyHandGroup> contents;
    private Context mContext;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    public TuijianGroupdapter(List<HappyHandGroup> contents, Context mContext) {
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
            convertView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.item_index_tuijian_group, parent, false);
            holder.cover = (ImageView) convertView.findViewById(R.id.cover);
            holder.nickname = (TextView) convertView.findViewById(R.id.nickname);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final HappyHandGroup cell = contents.get(position);//获得元素
        if (cell != null) {
            imageLoader.displayImage(cell.getPic(), holder.cover, MeetLoveApplication.options, animateFirstListener);
            holder.nickname.setText(cell.getTitle());
            holder.content.setText(cell.getContent());
        }
        return convertView;
    }

    class ViewHolder {
        ImageView cover;
        TextView nickname;
        TextView content;
    }

}
