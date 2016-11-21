package com.hfut.buaa.data.manager.repository;

import com.hfut.buaa.data.manager.model.BucketInstAuthority;
import com.hfut.buaa.data.manager.utils.InstanceType;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

/**
 * Created by tanweihan on 16/11/12.
 */
@Repository
public class DaoInst {

    @Autowired(required = true)
    private SessionFactory sessionFactory;

    /**
     * 获取session的公共方法
     *
     * @return Session
     */
    public Session openSession() {
        return sessionFactory.openSession();
    }

    /**
     * 判断数据库中是否存在
     *
     * @param insType
     * @param id
     * @return
     */
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

    /**
     * 关闭所有可关闭的类
     *
     * @param es
     * @param <E>
     */
    public <E extends Closeable> void clossAll(E... es) {
        for (E e : es) {
            if (null != e) {
                try {
                    e.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
