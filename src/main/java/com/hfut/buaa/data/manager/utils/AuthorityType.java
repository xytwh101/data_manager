package com.hfut.buaa.data.manager.utils;

/**
 * Created by tanweihan on 16/11/11.
 */
public enum AuthorityType {
    READ(1),
    WRITE(2);

    int typeId;

    AuthorityType(int i) {
        this.typeId = i;
    }

    public int getTypeId() {
        return typeId;
    }
}
