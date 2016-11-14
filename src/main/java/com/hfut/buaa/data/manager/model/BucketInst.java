package com.hfut.buaa.data.manager.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.*;

/**
 * Created by tanweihan on 16/11/11.
 */
public class BucketInst extends Instance {
    private Set<DataInst> dataInsts = new HashSet<DataInst>();

    public BucketInst() {
    }

    public BucketInst(String json) {
        JSONObject jsonObj = JSON.parseObject(json);
        setId(jsonObj.getIntValue("id"));
        setBucketId(jsonObj.getLongValue("bucketId"));
        setUserId(jsonObj.getLongValue("userId"));
        setBucketName(jsonObj.getString("bucketName"));
        JSONArray arr = jsonObj.getJSONArray("dataInsts");
        Set<DataInst> set = new HashSet<DataInst>();
        for (int i = 0; i < arr.size(); i++) {
            String ss = arr.getJSONObject(i).toJSONString();
            DataInst dataInst = new DataInst(arr.getJSONObject(i).toJSONString());
            set.add(dataInst);
        }
        setDataInsts(set);
        JSONArray arrAut = jsonObj.getJSONArray("authoritySet");
        Set<Authority> auSet = new HashSet<Authority>();
        for (int i = 0; i < arrAut.size(); i++) {
            Authority authority = new BucketInstAuthority(arrAut.getJSONObject(i).toJSONString());
            auSet.add(authority);
        }
        setAuthoritySet(auSet);
    }


    public Set<DataInst> getDataInsts() {
        return dataInsts;
    }

    public void setDataInsts(Set<DataInst> dataInsts) {
        this.dataInsts = dataInsts;
    }
}
