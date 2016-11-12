package com.hfut.buaa.data.manager.model;

/**
 * Created by tanweihan on 16/11/11.
 */
public class Authority {
    protected int id;
    protected long userId;
    protected long instId;
    protected int authority;

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
}
