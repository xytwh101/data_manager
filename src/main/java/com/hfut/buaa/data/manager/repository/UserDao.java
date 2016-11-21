package com.hfut.buaa.data.manager.repository;

import com.hfut.buaa.data.manager.model.BucketInst;
import com.hfut.buaa.data.manager.model.User;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by tanweihan on 16/11/11.
 */
@Repository
public interface UserDao {

    /**
     * use userId and password to get User
     *
     * @param userId
     * @param password
     * @return User
     */
    public User getUserByPassword(long userId, String password);

    /**
     * only usr userId to get User
     *
     * @param userId
     * @return User
     */
    public User getUSerByUserId(long userId);

    /**
     * create a User through userId(not null), password, userName
     *
     * @param user
     */
    public void createUser(User user);

    /**
     * delete User through userId
     *
     * @param userId
     */
    public void deleteUser(long userId, String password);

    /**
     * get Buckets Set through userId
     *
     * @param userId
     * @return Set<BucketInst>
     */
    public Set<BucketInst> getBuckets(long userId);

    /**
     * @param user
     */
    public void updateUserPwdAndName(User user);

    /**
     * @param userId
     * @param bucketInst
     */
    public void createBucket(long userId, BucketInst bucketInst);

    /**
     * @param userId
     * @param bucketInstId
     */
    public void deleteBucket(long userId, long bucketInstId);

    /**
     * @param userId
     * @param bucketInstId
     */
    public BucketInst getBucket(long userId, long bucketInstId);


    /**
     * @param userId
     * @param bucketId
     * @return
     */
    public BucketInst getOthersBucket(long userId, long bucketId);

}
