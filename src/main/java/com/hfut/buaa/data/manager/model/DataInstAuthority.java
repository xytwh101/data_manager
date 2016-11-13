package com.hfut.buaa.data.manager.model;

/**
 * Created by tanweihan on 16/11/11.
 */
public class DataInstAuthority extends Authority {
    @Override
    public void initAuthority(Instance instance, int authorityId) {
        DataInst dataInst = (DataInst) instance;
        setUserId(dataInst.getUserId());
        setAuthority(authorityId);
        setInstId(dataInst.getDataInstId());
    }
}
