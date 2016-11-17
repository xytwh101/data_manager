package com.hfut.buaa.data.manager.utils;

/**
 * Created by tanweihan on 16/11/17.
 */
public enum InstanceType {
    DATAINST("dataInstId"),
    DATAINSTAUTHORITY("instId"),
    BUCKETINST("bucketId"),
    BUCKETINSTAUTHORITY("instId"),
    USER("userId");

    String idName;

    InstanceType(String idName) {
        this.idName = idName;
    }

    public static String getIdName(String type) {
        return InstanceType.valueOf(type.toUpperCase()).getIdName();
    }

    public String getIdName() {
        return idName;
    }
}
