package com.lbins.meetlove.data;

import com.lbins.meetlove.module.HappyHandNews;
import com.lbins.meetlove.module.HappyHandNotice;

import java.util.List;

/**
 * Created by zhl on 2017/4/21.
 */
public class HappyHandNewsDatas extends Data {
    private List<HappyHandNews> data;

    public List<HappyHandNews> getData() {
        return data;
    }

    public void setData(List<HappyHandNews> data) {
        this.data = data;
    }
}
