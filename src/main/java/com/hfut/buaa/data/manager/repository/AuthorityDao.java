package com.hfut.buaa.data.manager.repository;

import com.hfut.buaa.data.manager.model.*;

import java.util.Set;

/**
 * Created by tanweihan on 16/11/11.
 */
public interface AuthorityDao {
    /**
     * @param bucketInst
     * @param authId
     */
    public void saveBucketInstAuthority(BucketInst bucketInst, int authId);

    /**
     * @param dataInst
     * @param authId
     */
    public void saveDataInstAuthority(DataInst dataInst, int authId);

    /**
     * @param bucketId
     */
    public void deleteBucketInstAuthority(long bucketId);

    /**
     * @param bucketId
     * @return
     */
    public Set<Authority> getBucketInstAuthority(long bucketId);

    /**
     * @param dataInstId
     */
    public void deleteDataInstAuthority(long dataInstId);

    /**
     * @param dataInstId
     * @return
     */
    public Set<Authority> getDataInstAuthority(long dataInstId);

}
