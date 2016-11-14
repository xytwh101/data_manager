package com.hfut.buaa.data.manager.repository;

import com.hfut.buaa.data.manager.model.DataInst;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by tanweihan on 16/11/11.
 */
@Repository
public interface BucketInstDao {

    /**
     * @param bucketId
     * @return Set<DataInst>
     */
    public Set<DataInst> getDataInsts(long bucketId);

    /**
     * @param userId
     * @param dataInstId
     * @return DataInst
     */
    public DataInst getDataInst(long userId, long bucketId, long dataInstId);

    /**
     * @param userId
     * @param bucketId
     * @param dataInst
     */
    public void addDataInst(long userId, long bucketId, DataInst dataInst);

    /**
     * @param userId
     * @param bucketId
     * @param dataInst
     */
    public void deleteDataInst(long userId, long bucketId, long dataInst);

    /**
     * @param userId
     * @param bucketId
     * @param dataInst
     */
    public void updateDataInst(long userId, long bucketId, DataInst dataInst);

    /**
     * @param dataInstId
     * @return
     */
    public boolean isExist(long dataInstId);
}
