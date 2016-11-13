package com.hfut.buaa.data.manager.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by tanweihan on 16/11/11.
 */
public class User implements Serializable {
    private int id;
    private long userId;
    private String userName;
    private String password;
    private Set<BucketInst> buckets = new HashSet<BucketInst>();


    public User() {
    }

    public User(long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<BucketInst> getBuckets() {
        return buckets;
    }

    public void setBuckets(Set<BucketInst> buckets) {
        this.buckets = buckets;
    }

    @Override
    public boolean equals(Object obj) {
        User comUser = (User) obj;
        if (this.userId != comUser.getUserId())
            return false;
        if (!this.userName.equals(comUser.getUserName()))
            return false;
        if (this.buckets.size() != comUser.getBuckets().size())
            return false;
        return true;
    }
}
