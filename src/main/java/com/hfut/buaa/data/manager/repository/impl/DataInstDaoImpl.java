package com.hfut.buaa.data.manager.repository.impl;

import com.hfut.buaa.data.manager.repository.DaoInst;
import com.hfut.buaa.data.manager.repository.DataInstDao;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileAlreadyExistsException;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.stereotype.Repository;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by tanweihan on 16/11/12.
 */
@Repository
public class DataInstDaoImpl extends DaoInst implements DataInstDao {
    /**
     * @param path
     * @return
     */
    @Override
    public String getFileString(String path) {
        Configuration conf = new Configuration();
        FileSystem fs = null;
        StringBuilder stringBuilder = new StringBuilder();
        FSDataInputStream hdfsInStream = null;
        try {
            byte[] bytes = new byte[1024 * 512];
            fs = FileSystem.get(URI.create(path), conf);
            hdfsInStream = fs.open(new Path(path));
            int len = 0;
            while ((len = hdfsInStream.read(bytes)) != -1) {
                stringBuilder.append(new String(bytes, "utf-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            clossAll(hdfsInStream, fs);
        }
        return stringBuilder.toString();
    }

    /**
     * @param url
     */
    @Override
    public void deleteFileString(String url) {
        try {
            FileSystem hdfs = FileSystem.get(new java.net.URI(url),
                    new org.apache.hadoop.conf.Configuration());
            Path path = new Path(url);
            if (hdfs.exists(path)) {
                hdfs.delete(path, true);
            } else {
                throw new FileNotFoundException("hdfs path " + url + " have not found file!");
            }
            hdfs.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param url
     * @param fileString
     */
    @Override
    public void saveFileString(String url, String fileString) {
        byte[] bytes = new byte[1024 * 512];
        DataOutputStream writer = null;
        try {
            FileSystem hdfs = FileSystem.get(new java.net.URI(url),
                    new org.apache.hadoop.conf.Configuration());
            Path path = new Path(url);
            if (!hdfs.exists(path)) {
                writer = new DataOutputStream(hdfs.create(path));
                bytes = fileString.getBytes("utf-8");
                writer.write(bytes);
                writer.flush();
            } else {
                // TODO
                throw new FileAlreadyExistsException("hdfs file " + url + " is alread exists!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } finally {
            clossAll(writer);
        }
    }

    private <E extends Closeable> void clossAll(E... es) {
        for (E e : es) {
            if (null != e) {
                try {
                    e.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
