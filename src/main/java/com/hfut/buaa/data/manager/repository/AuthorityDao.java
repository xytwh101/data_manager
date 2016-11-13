package com.hfut.buaa.data.manager.repository;

import com.hfut.buaa.data.manager.model.BucketInst;
import com.hfut.buaa.data.manager.model.BucketInstAuthority;
import com.hfut.buaa.data.manager.model.DataInst;
import com.hfut.buaa.data.manager.model.DataInstAuthority;

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
    public Set<BucketInstAuthority> getBucketInstAuthority(long bucketId);

    /**
     * @param dataInstId
     */
    public void deleteDataInstAuthority(long dataInstId);

    /**
     * @param dataInstId
     * @return
     */
    public Set<DataInstAuthority> getDataInstAuthority(long dataInstId);
}
