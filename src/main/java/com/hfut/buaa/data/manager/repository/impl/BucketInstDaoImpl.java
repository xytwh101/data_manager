package com.hfut.buaa.data.manager.repository.impl;

import com.hfut.buaa.data.manager.exception.DataInstNotInThisBucketException;
import com.hfut.buaa.data.manager.exception.DataInstsNotFoundException;
import com.hfut.buaa.data.manager.model.DataInst;
import com.hfut.buaa.data.manager.repository.BucketInstDao;
import com.hfut.buaa.data.manager.repository.DaoInst;
import com.hfut.buaa.data.manager.repository.DataInstDao;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tanweihan on 16/11/12.
 */
@Repository
public class BucketInstDaoImpl extends DaoInst implements BucketInstDao {
    @Autowired(required = true)
    private DataInstDao dataInstDao;

    /**
     * 通过bucketId获取DataInst集合
     *
     * @param bucketId
     * @return Set<DataInst>
     */
    @Override
    public Set<DataInst> getDataInsts(long bucketId) {
        //TODO 添加权限验证
        Session session = openSession();
        Query query = session.createQuery("from DataInst where bucketId=:para ");
        query.setParameter("para", bucketId);
        List<DataInst> list = query.list();
        session.close();
        Set<DataInst> set = new HashSet<DataInst>();
        set.addAll(list);
        return set;
//        throw new DataInstsNotFoundException(
//                "there is no DataInsts in BucketInst that bucketId is " + bucketId + " !");
    }

    /**
     * 通过userId,buckId,dataInstId得到有权限的DataInst
     *
     * @param userId
     * @param dataInstId
     * @return
     */
    @Override
    public DataInst getDataInst(long userId, long bucketId, long dataInstId) {
//        Session session = openSession();
//        Query query = session.createQuery("select DataInst from DataInst inst ,DataInstAuthority au " +
//                "where au.userId = :para1 and au.instId = inst.dataInstId and " +
//                "inst.bucketId = :para2 and au.instId = :para3");
//        query.setParameter("para1", userId);
//        query.setParameter("para2", bucketId);
//        query.setParameter("para3", dataInstId);
//        List<DataInst> list = query.list();
//        session.close();
//        if (0 != list.size()) {
//            DataInst data = list.get(0);
//            data.setFileString(dataInstDao.getFileString(data.getFilePath()));
//            return data;
//        }
//        throw new DataInstNotInThisBucketException("this dataInst that id is " +
//                dataInstId + " is not in the bucket where the bucketId is + " + bucketId);
        return null;
    }

    /**
     * @param userId
     * @param bucketId
     * @param dataInst
     */
    @Override
    public void addDataInst(long userId, long bucketId, DataInst dataInst) {

    }

    /**
     * @param userId
     * @param bucketId
     * @param dataInst
     */
    @Override
    public void deleteDataInst(long userId, long bucketId, long dataInst) {

    }

    /**
     * @param userId
     * @param bucketId
     * @param dataInst
     */
    @Override
    public void updateDataInst(long userId, long bucketId, DataInst dataInst) {

    }

}
