package com.hfut.buaa.data.manager.repository.impl;

import com.hfut.buaa.data.manager.exception.CreateDataInstValidationException;
import com.hfut.buaa.data.manager.exception.DataInstNotInThisBucketException;
import com.hfut.buaa.data.manager.exception.DataInstsNotFoundException;
import com.hfut.buaa.data.manager.exception.UserNotMatchException;
import com.hfut.buaa.data.manager.model.BucketInst;
import com.hfut.buaa.data.manager.model.DataInst;
import com.hfut.buaa.data.manager.model.ResourceInst;
import com.hfut.buaa.data.manager.model.User;
import com.hfut.buaa.data.manager.repository.AuthorityDao;
import com.hfut.buaa.data.manager.repository.BucketInstDao;
import com.hfut.buaa.data.manager.repository.DaoInst;
import com.hfut.buaa.data.manager.repository.DataInstDao;
import com.hfut.buaa.data.manager.utils.AuthorityType;
import com.hfut.buaa.data.manager.utils.InstanceType;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

/**
 * Created by tanweihan on 16/11/12.
 */
@Repository
public class BucketInstDaoImpl extends DaoInst implements BucketInstDao {
    @Autowired(required = true)
    private DataInstDao dataInstDao;
    @Autowired(required = true)
    private AuthorityDao authorityDao;
    @Autowired(required = true)
    private ResourceInst resourceInst;

    /**
     * 通过bucketId获取DataInst集合
     *
     * @param bucketId
     * @return Set<DataInst>
     */
    @Override
    public Set<DataInst> getDataInsts(long bucketId) {
        Session session = openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("from DataInst where bucketId=:para ");
        query.setParameter("para", bucketId);
        List<DataInst> list = query.list();
        transaction.commit();
        session.close();
        if (0 != list.size()) {
            Set<DataInst> set = new HashSet<DataInst>();
            for (DataInst data : list) {
                String filePath = data.getFilePath();
                // 如果有文件者进行提取
                if (filePath.length() > 0) {
                    data.setFileString(dataInstDao.getFileString(filePath));
                }
                // data.setAuthoritySet(authorityDao.getDataInstAuthority(data.getDataInstId()));
                set.add(data);
            }
            return set;
        }
        throw new DataInstsNotFoundException(
                "there is no DataInsts in BucketInst that bucketId is " + bucketId + " !");
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
        Session session = openSession();
        Transaction transaction = session.beginTransaction();
        // 判断该用户是否有权限
        if (authorityDao.getBucketAuthorityType(userId, bucketId) > 0) {
            Query query = session.createQuery("from DataInst where bucketId = :bid and dataInstId = :did");
            query.setParameter("bid", bucketId).setParameter("did", dataInstId);
            List<DataInst> list = query.list();
            transaction.commit();
            session.close();
            if (0 != list.size()) {
                DataInst data = list.get(0);
                String filePath = data.getFilePath();
                // 如果有文件者进行提取
                if (filePath.length() > 0) {
                    data.setFileString(dataInstDao.getFileString(filePath));
                }
                // data.setAuthoritySet(authorityDao.getDataInstAuthority(dataInstId));
                return data;
            }
        }
        throw new DataInstNotInThisBucketException("this dataInst that id is " +
                dataInstId + " is not in the bucket where the bucketId is " + bucketId);
    }

    /**
     * @param userId
     * @param bucketId
     * @param dataInst
     */
    @Override
    public void addDataInst(long userId, long bucketId, DataInst dataInst) {
        Session session = openSession();
        Transaction ts = session.beginTransaction();
        long dataInstId = dataInst.getDataInstId();
        // 判断是否有这个 user 和 bucket
        if (userId > 0
                && bucketId > 0
                && isExist(BucketInst.class.getName(), bucketId)) {
            if (isExist(DataInst.class.getName(), dataInstId)) {
                throw new CreateDataInstValidationException(
                        "this dataInstId " + dataInstId + " is exist");
            } else {
                String fileString = dataInst.getFileString();
                if (fileString.length() > 0) {
                    // 构建path TODO
                    String path = builderFilePath(userId, bucketId, dataInstId);
                    dataInst.setFilePath(path);
                    dataInstDao.saveFileString(path, fileString);
                }
                // authorityDao.saveDataInstAuthority(dataInst, AuthorityType.WRITE.getTypeId());
                session.save(dataInst);
                ts.commit();
                session.close();
            }
        } else {
            throw new CreateDataInstValidationException("a bucketId or a userId is necessary!");
        }
    }

    @Override
    public String builderFilePath(long userId, long bucketId, long dataInstId) {
        Properties properties = resourceInst.getProperties();
        StringBuilder builder = new StringBuilder();
        builder.append(properties.getProperty("hadoop-url"))
                .append("/" + userId)
                .append("/" + bucketId)
                .append("/" + dataInstId)
                .append("/" + System.currentTimeMillis());
        return builder.toString();
    }

    /**
     * @param userId
     * @param bucketId
     * @param dataInstId
     */
    @Override
    public void deleteDataInst(long userId, long bucketId, long dataInstId) {
        Session session = openSession();
        DataInst dataInst = getDataInst(userId, bucketId, dataInstId);
        // 确定是创建者，才允许删除
        if (userId == dataInst.getUserId() && bucketId == dataInst.getBucketId()) {
            Transaction transaction = session.beginTransaction();
            dataInstDao.deleteFileString(dataInst.getFilePath());
            session.delete(dataInst);
            transaction.commit();
            // authorityDao.deleteDataInstAuthority(dataInstId);
            session.close();
        } else {
            throw new UserNotMatchException("this DataInst witch dataInstId is " +
                    dataInstId + " is not created by User that userId is " + userId);
        }
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
