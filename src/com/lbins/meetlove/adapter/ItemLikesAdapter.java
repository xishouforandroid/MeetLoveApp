package com.lbins.meetlove.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.lbins.meetlove.R;
import com.lbins.meetlove.module.HappyHandLike;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 */
public class ItemLikesAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<HappyHandLike> records;
    private Context mContext;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    public ItemLikesAdapter(List<HappyHandLike> records, Context mContext) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_like, null);
            holder.nickname = (TextView) convertView.findViewById(R.id.nickname);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final HappyHandLike cell = records.get(position);
        if (cell != null) {
            if("0".equals(cell.getIs_use())){
                //不使用
                holder.nickname.setBackgroundResource(R.drawable.icon_hobby_disable);
                holder.nickname.setTextColor(mContext.getColor(R.color.textColortwo));
            }else
            if("1".equals(cell.getIs_use())){
                //使用
                holder.nickname.setBackground(mContext.getDrawable(R.drawable.icon_hobby_normal));
                holder.nickname.setTextColor(mContext.getColor(R.color.textColor));
            }else{
                //被选中的 要变黄色
                holder.nickname.setBackground(mContext.getDrawable(R.drawable.icon_hobby_active));
                holder.nickname.setTextColor(mContext.getColor(R.color.white));
            }
            holder.nickname.setText(cell.getLikename());
        }
        return convertView;
    }

    class ViewHolder {
        TextView nickname;
    }
}
