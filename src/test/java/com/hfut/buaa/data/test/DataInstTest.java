package com.hfut.buaa.data.test;

import com.hfut.buaa.data.manager.utils.FileUtils;
import junit.framework.TestCase;
import org.apache.hadoop.fs.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.specs2.io.fs;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URISyntaxException;

/**
 * Created by tanweihan on 16/11/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:root-context.xml", "classpath:app-context.xml"})
public class DataInstTest extends TestCase {
    private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void saveFileToHdfsTest() {
        String uri = "http://localhost:8080/data_manager/spring/save/{fileName}";
        String filePath = "/Users/tanweihan/workspace/data_manager/data_manager/src/test/java/com/hfut/buaa/data/test/files/test.txt";
        File file = new File(filePath);
        String string = FileUtils.covFile2String(filePath);
        if (string.length() > 0) {
            restTemplate.postForLocation(uri, string, "test");
        }

    }

    @Test
    public void testHdfs() {
//        String filePath = "/Users/tanweihan/twhGit/data_manager/src/test/java/com/hfut/buaa/data/test/files/test.txt";
//        String url = "hdfs://114.213.234.15:8020/";
//        StringBuilder stringBuilder = new StringBuilder();
//        try {
//            FileSystem hdfs = FileSystem.get(new java.net.URI(url),
//                    new org.apache.hadoop.conf.Configuration());
//            Path path = new Path("hdfs://114.213.234.15:8020/output2/part-r-00000");
//            // hdfs.mkdirs(path);
//            boolean b = hdfs.exists(path);
//            FSDataInputStream inputStream = hdfs.open(path);
//            hdfs.copyFromLocalFile(new Path("/Users/tanweihan/twhGit/data_manager/src/test/java/com/hfut/buaa/data/test/files/test.txt"),
//                    new Path("hdfs://114.213.234.15:8020/output2/test"));
////            byte[] bytes = new byte[100];
////            while (inputStream.read(bytes) != -1) {
////                stringBuilder.append(new String(bytes, "utf-8"));
////            }
//            System.out.println(stringBuilder.toString());
//            System.out.println(b);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }


    }
}
