package com.hfut.buaa.data.manager.repository.impl;

import com.hfut.buaa.data.manager.exception.CreateDataInstValidationException;
import com.hfut.buaa.data.manager.exception.DataInstNotInThisBucketException;
import com.hfut.buaa.data.manager.exception.DataInstsNotFoundException;
import com.hfut.buaa.data.manager.exception.UserNotMatchException;
import com.hfut.buaa.data.manager.model.BucketInst;
import com.hfut.buaa.data.manager.model.DataInst;
import com.hfut.buaa.data.manager.model.User;
import com.hfut.buaa.data.manager.repository.AuthorityDao;
import com.hfut.buaa.data.manager.repository.BucketInstDao;
import com.hfut.buaa.data.manager.repository.DaoInst;
import com.hfut.buaa.data.manager.repository.DataInstDao;
import com.hfut.buaa.data.manager.utils.AuthorityType;
import com.hfut.buaa.data.manager.utils.InstanceType;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
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
                data.setAuthoritySet(authorityDao.getDataInstAuthority(data.getDataInstId()));
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
        // TODO 检查
        Session session = openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("select  inst from DataInst inst ,DataInstAuthority au " +
                "where au.userId = :para1 and au.instId = inst.dataInstId and " +
                "inst.bucketId = :para2 and au.instId = :para3");
        query.setParameter("para1", userId);
        query.setParameter("para2", bucketId);
        query.setParameter("para3", dataInstId);
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
            data.setAuthoritySet(authorityDao.getDataInstAuthority(dataInstId));
            return data;
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
                && isExist(User.class.getName(), userId)
                && isExist(BucketInst.class.getName(), bucketId)) {
            if (isExist(DataInst.class.getName(), dataInstId)) {
                throw new CreateDataInstValidationException(
                        "this dataInstId " + dataInstId + " is exist");
            } else {
                session.save(dataInst);
                ts.commit();
                session.close();
                try {
                    authorityDao.saveDataInstAuthority(dataInst, AuthorityType.WRITE.getTypeId());
                    String fileString = dataInst.getFileString();
                    if (fileString.length() > 0) {
                        // 构建path
                        String path = builderFilePath(userId, bucketId, dataInstId);
                        dataInst.setFilePath(path);
                        dataInstDao.saveFileString(path, fileString);
                    }
                } catch (Exception ex) {
                    // 如果出现任何问题，就删除
                    deleteDataInst(userId, bucketId, dataInstId);
                }
            }
        } else {
            throw new CreateDataInstValidationException("a bucketId or a userId is necessary!");
        }
    }

    private String builderFilePath(long userId, long bucketId, long dataInstId) {
        InputStream resourceStream = Class.class.getResourceAsStream("/hadoop-connect.properties");
        Properties properties = new Properties();
        try {
            properties.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            try {
                Transaction transaction = session.beginTransaction();
                dataInstDao.deleteFileString(dataInst.getFilePath());
                session.delete(dataInst);
                transaction.commit();
                authorityDao.deleteDataInstAuthority(dataInstId);
            } catch (Exception ex) {

            } finally {
                session.close();
            }
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

    @Override
    public boolean isExist(String insType, long id) {
        String[] arr = insType.split("\\.");
        String className = arr[arr.length - 1];
        Session session = openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("from " + className + " where " +
                InstanceType.getIdName(className) + " = :id");
        query.setParameter("id", id);
        List list = query.list();
        transaction.commit();
        session.close();
        return 0 != list.size() ? true : false;
    }

}
