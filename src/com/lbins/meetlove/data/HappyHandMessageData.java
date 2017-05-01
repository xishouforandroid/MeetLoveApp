package com.lbins.meetlove.data;


import com.lbins.meetlove.dao.HappyHandMessage;

import java.util.List;

/**
 * Created by zhl on 2017/4/24.
 */
public class HappyHandMessageData extends Data {
    private List<HappyHandMessage> data;

    public List<HappyHandMessage> getData() {
        return data;
    }

    public void setData(List<HappyHandMessage> data) {
        this.data = data;
    }
}
