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
import com.lbins.meetlove.dao.Friends;
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
            holder.accept = (ImageView) convertView.findViewById(R.id.accept);
            holder.reject = (ImageView) convertView.findViewById(R.id.reject);
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
                holder.accept.setVisibility(View.VISIBLE);
                holder.reject.setVisibility(View.VISIBLE);
                holder.accept.setImageDrawable(res.getDrawable(R.drawable.btn_newfirend_accept));
                holder.reject.setImageDrawable(res.getDrawable(R.drawable.btn_newfirend_refuse));
            }
            if("1".equals(cell.getIs_check())){
                holder.accept.setVisibility(View.VISIBLE);
                holder.reject.setVisibility(View.GONE);
                holder.accept.setImageDrawable(res.getDrawable(R.drawable.btn_newfriend_alreadyis));
            }
            if("2".equals(cell.getIs_check())){
                holder.accept.setVisibility(View.VISIBLE);
                holder.reject.setVisibility(View.GONE);
                holder.accept.setImageDrawable(res.getDrawable(R.drawable.btn_newfriend_refused));
            }
            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickContentItemListener.onClickContentItem(position, 1, "1000");
                }
            });
            holder.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickContentItemListener.onClickContentItem(position, 2, "1000");
                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        ImageView item_cover;
        TextView content;
        ImageView accept;
        ImageView reject;
        TextView nickname;
    }
}
