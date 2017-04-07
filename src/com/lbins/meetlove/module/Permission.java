package com.lbins.meetlove.module;

import java.util.List;

/**
 * Created by zhl on 2015/8/6.
 */
public class Permission {
    private String id;
    private String name;
    private List<Permission> child;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Permission> getChild() {
        return child;
    }

    public void setChild(List<Permission> child) {
        this.child = child;
    }
}
