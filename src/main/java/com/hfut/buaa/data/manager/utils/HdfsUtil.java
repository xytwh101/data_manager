package com.hfut.buaa.data.manager.utils;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * Created by tanweihan on 16/11/11.
 */
public class HdfsUtil {

    public static void saveFileToHdfs(String url, String fileName, FileInputStream inputStream) {
        try {
            FileSystem hdfs = FileSystem.get(new java.net.URI(url),
                    new org.apache.hadoop.conf.Configuration());
            Path path = new Path(url);
            String fileLocation = url + "/" + fileName;
            if (!hdfs.exists(path)) {
                byte[] bytes = new byte[1024 * 512];
                DataOutputStream writer =
                        new DataOutputStream(hdfs.create(new Path(fileLocation)));
                while (inputStream.read(bytes) != -1) {
                    writer.write(bytes);
                }
                writer.close();
            } else {
                // TODO
            }
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }
}
