package com.hfut.buaa.data.manager.repository.impl;

import com.hfut.buaa.data.manager.exception.CreateBucketValidationException;
import com.hfut.buaa.data.manager.exception.UserNotFoundException;
import com.hfut.buaa.data.manager.model.BucketInst;
import com.hfut.buaa.data.manager.model.DataInst;
import com.hfut.buaa.data.manager.model.User;
import com.hfut.buaa.data.manager.repository.BucketInstDao;
import com.hfut.buaa.data.manager.repository.DaoInst;
import com.hfut.buaa.data.manager.repository.UserDao;
import org.hibernate.Query;
import org.hibernate.Session;
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
    public void deleteUser(long userId) {

    }

    /**
     * 通过userId，连接User、BucketInst、BucketInstAuthority，得到所有有读取权限的BucketInst
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

    @Override
    public void createBucket(long userId, BucketInst bucketInst) {
        Session session = openSession();
        long bucketId = bucketInst.getBucketId();
        if (bucketInst.getUserId() > 0) {
            if (bucketId > 0) {
                // TODO 验证bucketId 是否存在
                if (isExists(bucketId)) {
                    throw new CreateBucketValidationException(
                            "this bucketId " + bucketId + " is exist");
                } else {
                    session.save(bucketInst);
                    Set<DataInst> set = bucketInst.getDataInsts();
                    session.close();
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


    @Override
    public void deleteBucket(long userId, long bucketInstId) {

    }

    @Override
    public BucketInst getBucket(long userId, long bucketInstId) {

        return null;
    }

    @Override
    public boolean isExists(long bucketInstId) {
        Session session = openSession();
        Query query = session.createQuery("from BucketInst where bucketId = :id");
        query.setParameter("id", bucketInstId);
        return 0 != query.list().size() ? true : false;
    }

}
