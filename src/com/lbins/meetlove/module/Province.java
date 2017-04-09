package com.lbins.meetlove.module;

import java.io.Serializable;

/**
 * Created by zhl on 2015/2/25.
 */
public class Province implements Serializable{
    private String pid;
    private String provinceid;
    private String pname;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }


    public String getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }
}
