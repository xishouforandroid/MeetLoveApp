package com.lbins.meetlove.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.lbins.meetlove.MeetLoveApplication;
import com.lbins.meetlove.R;
import com.lbins.meetlove.dao.Emp;
import com.lbins.meetlove.util.StringUtil;
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
    private List<Emp> contents;
    private Context mContext;
    private Resources res;

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }


    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    public TuijianPeopledapter(List<Emp> contents, Context mContext) {
        this.contents = contents;
        this.mContext = mContext;
        res = mContext.getResources();
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
            holder.cover = (ImageView) convertView.findViewById(R.id.cover);
            holder.nickname = (TextView) convertView.findViewById(R.id.nickname);
            holder.state = (TextView) convertView.findViewById(R.id.state);
            holder.vip1_img = (ImageView) convertView.findViewById(R.id.vip1_img);
            holder.vip2_img = (ImageView) convertView.findViewById(R.id.vip2_img);
            holder.vip1_text = (TextView) convertView.findViewById(R.id.vip1_text);
            holder.vip2_text = (TextView) convertView.findViewById(R.id.vip2_text);
            holder.age = (TextView) convertView.findViewById(R.id.age);
            holder.height = (TextView) convertView.findViewById(R.id.height);
            holder.address = (TextView) convertView.findViewById(R.id.address);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Emp cell = contents.get(position);//获得元素
        if (cell != null) {
            if(!StringUtil.isNullOrEmpty(cell.getNickname())){
                holder.nickname.setText(cell.getNickname());
            }
            if(!StringUtil.isNullOrEmpty(cell.getCover())){
                imageLoader.displayImage(cell.getCover(), holder.cover, MeetLoveApplication.txOptions, animateFirstListener);
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

            if(!StringUtil.isNullOrEmpty(cell.getHeightl())){
                holder.height.setText(cell.getHeightl() + "CM");
            }
            if (!StringUtil.isNullOrEmpty(cell.getCityName())){
                holder.address.setText(cell.getCityName());
            }
            if (!StringUtil.isNullOrEmpty(cell.getAge())){
                holder.age.setText(cell.getAge().substring(2,4)+"年");
            }
            if(!StringUtil.isNullOrEmpty(cell.getRzstate1())){
                if("1".equals(cell.getRzstate1())){
                    //进行身份认证了
                    holder.vip1_text.setTextColor(res.getColor(R.color.main_color));
                }else {
                    holder.vip1_text.setTextColor(res.getColor(R.color.textColortwo));
                }
            }else {
                holder.vip1_text.setTextColor(res.getColor(R.color.textColortwo));
            }
            if(!StringUtil.isNullOrEmpty(cell.getRzstate2())){
                if("1".equals(cell.getRzstate2())){
                    holder.vip1_img.setImageDrawable(res.getDrawable(R.drawable.icon_verify_id_enabled));
                }else {
                    holder.vip1_img.setImageDrawable(res.getDrawable(R.drawable.icon_verify_id_disable));
                }
            }else {
                holder.vip1_img.setImageDrawable(res.getDrawable(R.drawable.icon_verify_id_disable));
            }
            if(!StringUtil.isNullOrEmpty(cell.getRzstate3())){
                if("1".equals(cell.getRzstate3())){
                    //进行身份认证了
                    holder.vip2_img.setImageDrawable(res.getDrawable(R.drawable.icon_verify_honesty_enabled));
                    holder.vip2_text.setTextColor(res.getColor(R.color.main_color));
                }else {
                    holder.vip2_img.setImageDrawable(res.getDrawable(R.drawable.icon_verify_honesty_disable));
                    holder.vip2_text.setTextColor(res.getColor(R.color.textColortwo));
                }
            }else {
                holder.vip2_img.setImageDrawable(res.getDrawable(R.drawable.icon_verify_honesty_disable));
                holder.vip2_text.setTextColor(res.getColor(R.color.textColortwo));
            }
        }
        return convertView;
    }

    class ViewHolder {
        ImageView cover;
        TextView nickname;
        TextView state;
        ImageView vip1_img;
        ImageView vip2_img;
        TextView vip1_text;
        TextView vip2_text;
        TextView age;
        TextView height;
        TextView address;
    }

}
