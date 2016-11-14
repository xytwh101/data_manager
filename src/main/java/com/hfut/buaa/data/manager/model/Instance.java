package com.hfut.buaa.data.manager.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
/**
 * Created by tanweihan on 16/11/11.
 */
public class Instance {
    protected int id;
    protected long userId;
    protected long bucketId;
    protected String bucketName;
    protected Set<Authority> authoritySet = new HashSet<Authority>();

    public Instance() {
    }

    public Instance(String json) {
    }

    public Map<Long, Integer> getAuthorityMap() {
        Map<Long, Integer> authorityMap = new HashMap<Long, Integer>();
        if (0 == authoritySet.size())
            return null;
        for (Authority authority : this.authoritySet) {
            authorityMap.put(Long.valueOf(authority.getUserId()),
                    Integer.valueOf(authority.getAuthority()));
        }
        return authorityMap;
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

    public long getBucketId() {
        return bucketId;
    }

    public void setBucketId(long bucketId) {
        this.bucketId = bucketId;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public Set<Authority> getAuthoritySet() {
        return authoritySet;
    }

    public void setAuthoritySet(Set<Authority> authoritySet) {
        this.authoritySet = authoritySet;
    }
}
