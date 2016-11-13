package com.hfut.buaa.data.manager.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
}
