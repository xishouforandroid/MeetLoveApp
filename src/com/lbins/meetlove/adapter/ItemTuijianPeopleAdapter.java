package com.lbins.meetlove.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.lbins.meetlove.MeetLoveApplication;
import com.lbins.meetlove.R;
import com.lbins.meetlove.dao.Emp;
import com.lbins.meetlove.util.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 */
public class ItemTuijianPeopleAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<Emp> records;
    private Context mContext;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    public ItemTuijianPeopleAdapter(List<Emp> records, Context mContext) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_tuijian_people, null);
            holder.item_cover = (ImageView) convertView.findViewById(R.id.item_cover);
            holder.nickname = (TextView) convertView.findViewById(R.id.nickname);
            holder.state = (TextView) convertView.findViewById(R.id.state);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Emp cell = records.get(position);
        if (cell != null) {

            if(!StringUtil.isNullOrEmpty(cell.getCover())){
                imageLoader.displayImage(cell.getCover(), holder.item_cover, MeetLoveApplication.txOptions, animateFirstListener);
            }
            if(!StringUtil.isNullOrEmpty(cell.getState())){
                if("1".equals(cell.getState())){
                    //单身
                    holder.state.setText("单身");
                }
                if("2".equals(cell.getState())){
                    //交往中
                    holder.state.setText("交往中");
                }
            }
            String str = cell.getNickname()+" ";
            if (!StringUtil.isNullOrEmpty(cell.getAge())){
                str += cell.getAge().substring(2,4)+"年 ";
            }
            if(!StringUtil.isNullOrEmpty(cell.getHeightl())){
                str += cell.getHeightl() + "CM ";
            }
            if (!StringUtil.isNullOrEmpty(cell.getCityName())){
                str +=cell.getCityName();
            }
            holder.nickname.setText(str);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView item_cover;
        TextView nickname;
        TextView state;
    }
}
