package com.lbins.meetlove.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.lbins.meetlove.MeetLoveApplication;
import com.lbins.meetlove.R;
import com.lbins.meetlove.module.Friends;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 */
public class ItemFriendsApplyAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<Friends> records;
    private Context mContext;
    private Resources res;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }

    public ItemFriendsApplyAdapter(List<Friends> records, Context mContext) {
        this.records = records;
        this.mContext = mContext;
        res = mContext.getResources();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_friends_apply, null);
            holder.item_cover = (ImageView) convertView.findViewById(R.id.item_cover);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.accept = (TextView) convertView.findViewById(R.id.accept);
            holder.nickname = (TextView) convertView.findViewById(R.id.nickname);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Friends cell = records.get(position);
        if (cell != null) {
            imageLoader.displayImage(cell.getEmpid1Cover(), holder.item_cover, MeetLoveApplication.txOptions, animateFirstListener);
            holder.nickname.setText(cell.getEmpid1Nickname());
            holder.content.setText(cell.getApplytitle());
            if("0".equals(cell.getIs_check())){
                holder.accept.setText("接受");
                holder.accept.setTextColor(res.getColor(R.color.white));
                holder.accept.setBackground(res.getDrawable(R.drawable.btn_small_navbarbtn_enabled));
            }
            if("1".equals(cell.getIs_check())){
                holder.accept.setText("已添加");
                holder.accept.setBackgroundColor(res.getColor(R.color.white));
                holder.accept.setTextColor(res.getColor(R.color.textColortwo));
            }
            if("2".equals(cell.getIs_check())){
                holder.accept.setText("已拒绝");
                holder.accept.setBackgroundColor(res.getColor(R.color.white));
                holder.accept.setTextColor(res.getColor(R.color.textColortwo));
            }
            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickContentItemListener.onClickContentItem(position, 1, "1000");
                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        ImageView item_cover;
        TextView content;
        TextView accept;
        TextView nickname;
    }
}
