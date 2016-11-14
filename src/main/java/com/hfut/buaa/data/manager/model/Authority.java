package com.hfut.buaa.data.manager.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by tanweihan on 16/11/11.
 */
public class Authority {
    protected int id;
    protected long userId;
    protected long instId;
    protected int authority;

    public Authority() {
    }

    public Authority(String json) {
        JSONObject jsonObj = JSON.parseObject(json);
        setId(jsonObj.getIntValue("id"));
        setAuthority(jsonObj.getIntValue("authority"));
        setUserId(jsonObj.getLongValue("userId"));
        setInstId(jsonObj.getLongValue("instId"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getInstId() {
        return instId;
    }

    public void setInstId(long instId) {
        this.instId = instId;
    }

    public int getAuthority() {
        return authority;
    }

    public void setAuthority(int authority) {
        this.authority = authority;
    }

    public void initAuthority(Instance instance, int authorityId) {

    }

}
