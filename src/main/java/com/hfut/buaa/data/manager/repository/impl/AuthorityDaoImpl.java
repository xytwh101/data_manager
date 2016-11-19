package com.hfut.buaa.data.manager.repository.impl;

import com.hfut.buaa.data.manager.exception.BucketInstNotFoundException;
import com.hfut.buaa.data.manager.exception.DataInstsNotFoundException;
import com.hfut.buaa.data.manager.model.*;
import com.hfut.buaa.data.manager.repository.AuthorityDao;
import com.hfut.buaa.data.manager.repository.DaoInst;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tanweihan on 16/11/12.
 */
public class AuthorityDaoImpl extends DaoInst implements AuthorityDao {

    /**
     * @param bucketInst
     */
    @Override
    public void saveBucketInstAuthority(BucketInst bucketInst, int authId) {
        Session session = openSession();
        try {
            Transaction ts = session.beginTransaction();
            BucketInstAuthority bucketInstAuthority = new BucketInstAuthority();
            bucketInstAuthority.initAuthority(bucketInst, authId);
            session.save(bucketInstAuthority);
            ts.commit();
        } catch (Exception ex) {
            deleteBucketInstAuthority(bucketInst.getBucketId());
        } finally {
            session.close();
        }
    }

    /**
     * @param dataInst
     */
    @Override
    public void saveDataInstAuthority(DataInst dataInst, int authId) {
        Session session = openSession();
        try {
            Transaction ts = session.beginTransaction();
            DataInstAuthority dataInstAuthority = new DataInstAuthority();
            dataInstAuthority.initAuthority(dataInst, authId);
            session.save(dataInstAuthority);
            ts.commit();
        } catch (Exception ex) {
            deleteDataInstAuthority(dataInst.getDataInstId());
        } finally {
            session.close();
        }
    }

    @Override
    public void deleteBucketInstAuthority(long bucketId) {
        Set<Authority> set = getBucketInstAuthority(bucketId);
        if (0 != set.size()) {
            Session session = openSession();
            Transaction ts = session.beginTransaction();
            for (Authority bucketInstAuthority : set) {
                session.delete((BucketInstAuthority) bucketInstAuthority);
            }
            ts.commit();
            session.close();
        }
    }

    @Override
    public Set<Authority> getBucketInstAuthority(long bucketId) {
        Session session = openSession();
        Set<Authority> set = new HashSet<Authority>();
        try {
            Transaction ts = session.beginTransaction();
            Query query = session.createQuery("from BucketInstAuthority where instId = :bid");
            query.setParameter("bid", bucketId);
            List<BucketInstAuthority> list = query.list();
            ts.commit();
            set.addAll(list);
        } catch (Exception e) {

        } finally {
            session.close();
        }
        return set;
    }

    @Override
    public void deleteDataInstAuthority(long dataInstId) {
        Set<Authority> set = getDataInstAuthority(dataInstId);
        if (0 != set.size()) {
            Session session = openSession();
            Transaction ts = session.beginTransaction();
            for (Authority dataInstAuthority : set) {
                session.delete((DataInstAuthority) dataInstAuthority);
            }
            ts.commit();
            session.close();
        }
        throw new DataInstsNotFoundException("dataInst is not found when give and dataInstId = " + dataInstId);
    }

    @Override
    public Set<Authority> getDataInstAuthority(long dataInstId) {
        Session session = openSession();
        Set<Authority> set = new HashSet<Authority>();
        try {
            Transaction ts = session.beginTransaction();
            Query query = session.createQuery("from DataInstAuthority where instId = :did");
            query.setParameter("did", dataInstId);
            List<DataInstAuthority> list = query.list();
            ts.commit();
            set.addAll(list);
        } catch (Exception e) {

        } finally {
            session.close();
        }
        return set;
    }


}
