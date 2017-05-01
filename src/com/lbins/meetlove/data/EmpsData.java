package com.lbins.meetlove.data;


import com.lbins.meetlove.dao.Emp;

import java.util.List;

/**
 * Created by zhl on 2017/4/4.
 */
public class EmpsData extends Data {
    private List<Emp> data;

    public List<Emp> getData() {
        return data;
    }

    public void setData(List<Emp> data) {
        this.data = data;
    }
}
