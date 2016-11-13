package com.hfut.buaa.data.manager.repository.impl;

import com.hfut.buaa.data.manager.exception.BucketInstNotFoundException;
import com.hfut.buaa.data.manager.exception.CreateBucketValidationException;
import com.hfut.buaa.data.manager.exception.UserNotFoundException;
import com.hfut.buaa.data.manager.model.BucketInst;
import com.hfut.buaa.data.manager.model.DataInst;
import com.hfut.buaa.data.manager.model.User;
import com.hfut.buaa.data.manager.repository.AuthorityDao;
import com.hfut.buaa.data.manager.repository.BucketInstDao;
import com.hfut.buaa.data.manager.repository.DaoInst;
import com.hfut.buaa.data.manager.repository.UserDao;
import com.hfut.buaa.data.manager.utils.AuthorityType;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tanweihan on 16/11/11.
 */
@Repository
public class UserDaoImpl extends DaoInst implements UserDao {

    @Autowired(required = true)
    private BucketInstDao bucketInstDao;
    @Autowired(required = true)
    private AuthorityDao authorityDao;

    @Override
    public User getUserByPassword(long userId, String password) {
        Session session = openSession();
        Query query = session.createQuery("from User where userId = :para1 and password =:para2");
        query.setParameter("para1", userId).setParameter("para2", password);
        List result = query.list();
        session.close();
        if (result.size() != 0) {
            return (User) result.get(0);
        }
        throw new UserNotFoundException(String.valueOf(userId));
    }

    @Override
    public User getUSerByUserId(long userId) {
        return null;
    }

    @Override
    public void createUser(User user) {
        Session session = openSession();
        session.save(user);
        session.close();
    }

    @Override
    public void deleteUser(long userId, String password) {
        Session session = openSession();
        User user = getUserByPassword(userId, password);
        if (userId == user.getUserId() && password.equals(user.getPassword())) {
            Transaction ts = session.beginTransaction();
            session.delete(user);
            ts.commit();
            session.close();
        } else {
            throw new UserNotFoundException(String.valueOf(userId));
        }
    }

    /**
     * 通过userId，得到所有有读取权限的BucketInst
     *
     * @param userId
     * @return
     */
    @Override
    public Set<BucketInst> getBuckets(long userId) {
        Session session = openSession();
        Query query = session.createQuery("from BucketInst where userId = :para ");
        query.setParameter("para", userId);
        List<BucketInst> list = query.list();
        if (0 != list.size()) {
            for (BucketInst bucketInst : list) {
                bucketInst.setDataInsts(
                        bucketInstDao.getDataInsts(bucketInst.getBucketId()));
            }
        }
        Set<BucketInst> set = new HashSet<BucketInst>();
        set.addAll(query.list());
        return set;
    }

    @Override
    public void updateUserPwdAndName(User user) {

    }

    /**
     * 添加bucket
     *
     * @param userId
     * @param bucketInst
     */
    @Override
    public void createBucket(long userId, BucketInst bucketInst) {
        Session session = openSession();
        Transaction ts = session.beginTransaction();
        long bucketId = bucketInst.getBucketId();
        if (bucketInst.getUserId() > 0) {
            if (bucketId > 0) {
                if (isExists(bucketId)) {
                    throw new CreateBucketValidationException(
                            "this bucketId " + bucketId + " is exist");
                } else {
                    // 储存对象
                    session.save(bucketInst);
                    ts.commit();
                    session.close();
                    // 权限表添加
                    authorityDao.saveBucketInstAuthority(
                            bucketInst, AuthorityType.WRITE.getTypeId());
                    Set<DataInst> set = bucketInst.getDataInsts();
                    // 如果有DataInst
                    if (set.size() > 0) {
                        for (DataInst dataInst : set) {
                            // TODO
                            bucketInstDao.addDataInst(userId, bucketId, dataInst);
                        }
                    }
                }
            } else {
                throw new CreateBucketValidationException("a bucketId is necessary!");
            }
        } else {
            throw new CreateBucketValidationException("a userId is necessary!");
        }
    }


    /**
     * 删除自己创建的bucket
     *
     * @param userId
     * @param bucketInstId
     */
    @Override
    public void deleteBucket(long userId, long bucketInstId) {
        Session session = openSession();
        BucketInst bucketInst = getBucket(userId, bucketInstId);
        if (userId == bucketInst.getUserId()) {
            Transaction ts = session.beginTransaction();
            session.delete(bucketInst);
            ts.commit();
            session.close();
            // 删除权限表中的内容
            authorityDao.deleteBucketInstAuthority(bucketInst.getBucketId());
            Set<DataInst> dataInsts = bucketInst.getDataInsts();
            if (0 != dataInsts.size()) {
                for (DataInst dataInst : dataInsts) {
                    bucketInstDao.deleteDataInst(userId, bucketInstId, dataInst.getDataInstId());
                }
            }
        } else {
            throw new BucketInstNotFoundException("bucketInst is not found when give userId = "
                    + userId + " and bucketId = " + bucketInstId);
        }
    }

    /**
     * 获取自己创建的Bucket
     *
     * @param userId
     * @param bucketInstId
     * @return
     */
    @Override
    public BucketInst getBucket(long userId, long bucketInstId) {
        Session session = openSession();
        Query query = session.createQuery("from BucketInst where  userId = :para1 and bucketId = :para2");
        query.setParameter("para1", userId);
        query.setParameter("para2", bucketInstId);
        List<BucketInst> list = query.list();
        session.close();
        if (0 != list.size()) {
            return list.get(0);
        }
        throw new BucketInstNotFoundException("bucketInst is not found when give userId = "
                + userId + " and bucketId = " + bucketInstId);
    }

    @Override
    public boolean isExists(long bucketInstId) {
        Session session = openSession();
        Query query = session.createQuery("from BucketInst where bucketId = :id");
        query.setParameter("id", bucketInstId);
        session.close();
        return 0 != query.list().size() ? true : false;
    }

}
