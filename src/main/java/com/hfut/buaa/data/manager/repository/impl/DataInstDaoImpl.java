package com.hfut.buaa.data.manager.repository.impl;

import com.hfut.buaa.data.manager.repository.DaoInst;
import com.hfut.buaa.data.manager.repository.DataInstDao;
import org.springframework.stereotype.Repository;

/**
 * Created by tanweihan on 16/11/12.
 */
@Repository
public class DataInstDaoImpl extends DaoInst implements DataInstDao {
    @Override
    public String getFileString(String path) {
        return null;
    }
}
