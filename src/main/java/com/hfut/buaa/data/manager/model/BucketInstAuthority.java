package com.hfut.buaa.data.manager.model;

/**
 * Created by tanweihan on 16/11/11.
 */
public class BucketInstAuthority extends Authority {

    public BucketInstAuthority() {
    }

    public BucketInstAuthority(String json) {
        super(json);
    }

    @Override
    public void initAuthority(Instance instance, int authorityId) {
        BucketInst bucketInst = (BucketInst) instance;
        setUserId(bucketInst.getUserId());
        setAuthority(authorityId);
        setInstId(bucketInst.getBucketId());
    }

    @Override
    public void buildAuthority(Instance instance, long authorityUserId, int authorityId) {
        initAuthority(instance, authorityId);
        setUserId(authorityUserId);
    }
}
