package com.hfut.buaa.data.manager.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by tanweihan on 16/11/11.
 */
public class BucketInst extends Instance {
    private Set<DataInst> dataInsts = new HashSet<DataInst>();

    public Set<DataInst> getDataInsts() {
        return dataInsts;
    }

    public void setDataInsts(Set<DataInst> dataInsts) {
        this.dataInsts = dataInsts;
    }
}
