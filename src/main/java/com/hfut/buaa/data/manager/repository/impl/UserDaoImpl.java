package com.hfut.buaa.data.manager.repository.impl;

import com.hfut.buaa.data.manager.exception.*;
import com.hfut.buaa.data.manager.model.BucketInst;
import com.hfut.buaa.data.manager.model.BucketInstAuthority;
import com.hfut.buaa.data.manager.model.DataInst;
import com.hfut.buaa.data.manager.model.User;
import com.hfut.buaa.data.manager.repository.AuthorityDao;
import com.hfut.buaa.data.manager.repository.BucketInstDao;
import com.hfut.buaa.data.manager.repository.DaoInst;
import com.hfut.buaa.data.manager.repository.UserDao;
import com.hfut.buaa.data.manager.utils.AuthorityType;
import com.hfut.buaa.data.manager.utils.UpdateType;
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
        Transaction ts = session.beginTransaction();
        if (isExistUser(user.getUserId())) {
            throw new UserAlreadyExistsException("user id " + user.getUserId() + "is already exists.");
        } else {
            session.save(user);
        }
        ts.commit();
        session.close();
    }

    @Override
    public boolean isExistUser(long userId) {
        Session session = openSession();
        Transaction ts = session.beginTransaction();
        Query query = session.createQuery("from User where userId = :para");
        query.setParameter("para", userId);
        List result = query.list();
        return result.size() == 0 ? false : true;
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
        Query query = session.createQuery("from BucketInst a, BucketInstAuthority b " +
                "where b.userId = :para and a.bucketId = b.instId ");
        query.setParameter("para", userId);
        List<Object[]> list = query.list();
        Set<BucketInst> set = new HashSet<BucketInst>();
        if (0 != list.size()) {
            for (Object[] objects : list) {
                BucketInst bucketInst = (BucketInst) objects[0];
                BucketInstAuthority bucketInstAuthority
                        = (BucketInstAuthority) objects[1];
                bucketInst.setAuth(bucketInstAuthority.getAuthority());
                bucketInst.setDataInsts(
                        bucketInstDao.getDataInsts(bucketInst.getBucketId()));
                set.add(bucketInst);
            }
        }
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
        // 验证userId，bucketId是否有效，是否已经存在此bucketId的bucket
        long bucketId = bucketInst.getBucketId();
        if (bucketInst.getUserId() > 0) {
            if (bucketId > 0) {
                if (isExist(BucketInst.class.getName(), bucketId)) {
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
        // 只能删除自己的Bucket
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
        Transaction transaction = session.beginTransaction();
        int authorityType = 0;
        if ((authorityType =
                authorityDao.getBucketAuthorityInst(userId, bucketInstId).getAuthority()) > 0) {
            // 拥有权限
            Query query = session.createQuery("from BucketInst " +
                    "where bucketId = :para");
            query.setParameter("para", bucketInstId);
            List<BucketInst> list = query.list();
            transaction.commit();
            session.close();
            if (0 != list.size()) {
                BucketInst bucketInst = list.get(0);
                bucketInst.setAuthoritySet(authorityDao.getBucketInstAuthority(bucketInstId));
                bucketInst.setDataInsts(bucketInstDao.getDataInsts(bucketInst.getBucketId()));
                return bucketInst;
            }
        }
        throw new BucketInstNotFoundException("bucketInst is not found when give userId = "
                + userId + " and bucketId = " + bucketInstId);
    }

    @Override
    public void updateBucket(long userId, BucketInst bucketInst) {
        if (userId == bucketInst.getUserId()) {
            Session session = openSession();
            Transaction transaction = session.beginTransaction();
            // 需要先查询出来，再对比，将不同的切不为空的进行更新
            BucketInst old = getBucket(userId, bucketInst.getBucketId());
            String name = bucketInst.getBucketName();
            if (name != old.getBucketName()) {
                old.setBucketName(name);
            }
            session.update(old);
            // 更改所有dataInst的bucketName
            for (DataInst dataInst : old.getDataInsts()) {
                dataInst.setBucketName(name);
                session.update(dataInst);
            }
            transaction.commit();
            session.close();
        } else {
            throw new UserNotMatchException("");
        }
    }

    /**
     * 给其他用户添加读取权限
     *
     * @param userId
     * @param bucketInstId
     * @param authorityUserId
     */
    @Override
    public void updateAuthority(long userId, long bucketInstId, long authorityUserId, UpdateType type) {
        int authorityId = authorityDao.
                getBucketAuthorityInst(authorityUserId, bucketInstId).getAuthority();
        BucketInst bucket = getBucket(userId, bucketInstId);
        if (bucket != null) {
            Session session = openSession();
            Transaction transaction = session.beginTransaction();
            BucketInstAuthority autho = new BucketInstAuthority();
            if (authorityId == AuthorityType.NO.getTypeId()
                    && type.equals(UpdateType.CREATE)) {
                autho.buildAuthority(bucket, authorityUserId, AuthorityType.READ.getTypeId());
                session.save(autho);
            } else if (authorityId == AuthorityType.READ.getTypeId()
                    && type.equals(UpdateType.DELETE)) {
                autho = authorityDao.getBucketAuthorityInst(authorityUserId, bucketInstId);
                session.delete(autho);
            }
            transaction.commit();
            session.close();
        }
    }


}