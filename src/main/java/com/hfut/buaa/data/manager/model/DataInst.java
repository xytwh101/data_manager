package com.hfut.buaa.data.manager.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by tanweihan on 16/11/11.
 */
public class DataInst extends Instance {
    private long dataInstId;
    private String dataInstName;
    private String fileString;
    private String filePath;

    public DataInst() {
    }

    public DataInst(String json) {
        JSONObject jsonObj = JSON.parseObject(json);
        setId(jsonObj.getIntValue("id"));
        setDataInstId(jsonObj.getLongValue("dataInstId"));
        setBucketId(jsonObj.getLongValue("bucketId"));
        setUserId(jsonObj.getLongValue("userId"));
        setDataInstName(jsonObj.getString("dataInstName"));
        setBucketName(jsonObj.getString("bucketName"));
        setFileString(jsonObj.getString("fileString"));
        setFilePath(jsonObj.getString("filePath"));
        JSONArray arrAut = jsonObj.getJSONArray("authoritySet");
        Set<Authority> auSet = new HashSet<Authority>();
        for (int i = 0; i < arrAut.size(); i++) {
            Authority authority = new DataInstAuthority(arrAut.getJSONObject(i).toJSONString());
            auSet.add(authority);
        }
        setAuthoritySet(auSet);
    }

    public long getDataInstId() {
        return dataInstId;
    }

    public void setDataInstId(long dataInstId) {
        this.dataInstId = dataInstId;
    }

    public String getDataInstName() {
        return dataInstName;
    }

    public void setDataInstName(String dataInstName) {
        this.dataInstName = dataInstName;
    }

    public String getFileString() {
        return fileString;
    }

    public void setFileString(String fileString) {
        this.fileString = fileString;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
