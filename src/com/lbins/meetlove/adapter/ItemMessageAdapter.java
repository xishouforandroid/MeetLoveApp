package com.lbins.meetlove.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.lbins.meetlove.MeetLoveApplication;
import com.lbins.meetlove.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 */
public class ItemMessageAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<String> records;
    private Context mContext;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    public ItemMessageAdapter(List<String> records, Context mContext) {
        this.records = records;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return records.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_message, null);
//            holder.item_pic = (ImageView) convertView.findViewById(R.id.item_pic);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final String cell = records.get(position);
        if (cell != null) {
//            imageLoader.displayImage(cell, holder.item_pic, MeetLoveApplication.txOptions, animateFirstListener);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView item_pic;
    }
}
