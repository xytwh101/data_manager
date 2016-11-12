package com.hfut.buaa.data.manager.model;

import java.util.HashMap;
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
