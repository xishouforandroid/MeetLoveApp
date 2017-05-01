package com.lbins.meetlove.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.lbins.meetlove.R;
import com.lbins.meetlove.dao.HappyHandMessage;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 */
public class ItemMessagesAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<HappyHandMessage> records;
    private Context mContext;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    public ItemMessagesAdapter(List<HappyHandMessage> records, Context mContext) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_mine_messages, null);
            holder.dateline = (TextView) convertView.findViewById(R.id.dateline);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final HappyHandMessage cell = records.get(position);
        if (cell != null) {
            holder.content.setText(cell.getTitle());
            holder.dateline.setText(cell.getDateline());
        }
        return convertView;
    }

    class ViewHolder {
        TextView dateline;
        TextView content;
    }
}
