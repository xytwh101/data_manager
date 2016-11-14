package com.hfut.buaa.data.manager.repository;

import org.springframework.stereotype.Repository;

/**
 * Created by tanweihan on 16/11/11.
 */
@Repository
public interface DataInstDao {
    public String getFileString(String path);

    public void deleteFileString(String path);

    public void saveFileString(String path, String file);
}
