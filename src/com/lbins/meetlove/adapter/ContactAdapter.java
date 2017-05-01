package com.lbins.meetlove.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import com.lbins.meetlove.MeetLoveApplication;
import com.lbins.meetlove.R;
import com.lbins.meetlove.dao.Friends;
import com.lbins.meetlove.util.PingYinUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhl on 2016/8/22.
 */
public class ContactAdapter extends BaseAdapter implements SectionIndexer {
    private Context mContext;
    private List<Friends> mNicks = new ArrayList<Friends>();

    static ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    static ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    public ContactAdapter(Context mContext, List<Friends> nicks) {
        this.mContext = mContext;
        this.mNicks = nicks;
    }

    @Override
    public int getCount() {
        return mNicks.size();
    }

    @Override
    public Object getItem(int position) {
        return mNicks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String nickName = mNicks.get(position).getEmpid2Nickname();
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.contact_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tvCatalog = (TextView) convertView
                    .findViewById(R.id.contactitem_catalog);
            viewHolder.ivAvatar = (ImageView) convertView
                    .findViewById(R.id.contactitem_avatar_iv);
            viewHolder.tvNick = (TextView) convertView
                    .findViewById(R.id.contactitem_nick);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String catalog = PingYinUtil.converterToFirstSpell(nickName)
                .substring(0, 1);
        if (position == 0) {
            viewHolder.tvCatalog.setVisibility(View.VISIBLE);
            viewHolder.tvCatalog.setText(catalog);
            imageLoader.displayImage( mNicks.get(position).getEmpid2Cover(),  viewHolder.ivAvatar, MeetLoveApplication.txOptions, animateFirstListener);
        } else {
            String lastCatalog = PingYinUtil.converterToFirstSpell(
                    mNicks.get(position - 1).getEmpid2Nickname()).substring(0, 1);
            if (catalog.equals(lastCatalog)) {
                viewHolder.tvCatalog.setVisibility(View.GONE);
            } else {
                viewHolder.tvCatalog.setVisibility(View.VISIBLE);
                viewHolder.tvCatalog.setText(catalog);
            }
            imageLoader.displayImage( mNicks.get(position ).getEmpid2Cover(),  viewHolder.ivAvatar, MeetLoveApplication.txOptions, animateFirstListener);
        }


        viewHolder.tvNick.setText(nickName);
        return convertView;
    }

    static class ViewHolder {
        TextView tvCatalog;// 目录
        ImageView ivAvatar;// 头像
        TextView tvNick;// 昵称
    }

    @Override
    public int getPositionForSection(int section) {
        for (int i = 0; i < mNicks.size(); i++) {
            String l = PingYinUtil.converterToFirstSpell(mNicks.get(i).getEmpid2Nickname())
                    .substring(0, 1);
            char firstChar = l.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}
