package com.hfut.buaa.data.test;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by tanweihan on 16/11/25.
 */
public class HdfsTest {
    public static void main(String[] args) {
        String filePath = "/Users/tanweihan/twhGit/data_manager/src/test/java/com/hfut/buaa/data/test/files/test.txt";
        String url = "hdfs://192.168.0.112:9000/";
        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileSystem hdfs = FileSystem.get(new java.net.URI(url),
                    new org.apache.hadoop.conf.Configuration());
            Path path = new Path("hdfs://192.168.0.112:9000/user/tanweihan");
            hdfs.mkdirs(path);
            boolean b = hdfs.exists(path);
            // FSDataInputStream inputStream = hdfs.open(path);
            hdfs.copyFromLocalFile(new Path("/Users/tanweihan/twhGit/data_manager/src/test/java/com/hfut/buaa/data/test/files/test.txt"),
                    new Path("hdfs://192.168.0.112:9000/user/tanweihan/sss"));
////            byte[] bytes = new byte[100];
////            while (inputStream.read(bytes) != -1) {
////                stringBuilder.append(new String(bytes, "utf-8"));
////            }
//            System.out.println(stringBuilder.toString());
            System.out.println(b);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
