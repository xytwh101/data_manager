package com.hfut.buaa.data.manager.model;

/**
 * Created by tanweihan on 16/11/11.
 */
public class BucketInstAuthority extends Authority {
    @Override
    public void initAuthority(Instance instance, int authorityId) {
        BucketInst bucketInst = (BucketInst) instance;
        setUserId(bucketInst.getUserId());
        setAuthority(authorityId);
        setInstId(bucketInst.getBucketId());
    }
}
