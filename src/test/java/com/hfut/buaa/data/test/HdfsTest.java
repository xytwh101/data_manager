package com.hfut.buaa.data.test;

import com.alibaba.fastjson.JSON;
import com.hfut.buaa.data.manager.model.Authority;
import com.hfut.buaa.data.manager.model.DataInst;
import com.hfut.buaa.data.manager.model.DataInstAuthority;
import com.hfut.buaa.data.manager.utils.AuthorityType;
import com.hfut.buaa.data.manager.utils.FileUtils;
import junit.framework.TestCase;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by tanweihan on 16/11/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:root-context.xml",
        "classpath:app-context.xml"})
public class HdfsTest extends TestCase {
    private RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        String filePath = "/Users/tanweihan/workspace/data_manager/data_manager/src/test/java/com/hfut/buaa/data/test/files/test.txt";
        String url = "hdfs://192.168.0.112:9000/";
        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileSystem hdfs = FileSystem.get(new java.net.URI(url),
                    new org.apache.hadoop.conf.Configuration());
            Path path = new Path("hdfs://192.168.0.112:9000/user/tanweihan");
            hdfs.mkdirs(path);
            boolean b = hdfs.exists(path);
            // FSDataInputStream inputStream = hdfs.open(path);
            hdfs.copyFromLocalFile(new Path("/Users/tanweihan/workspace/data_manager/data_manager/src/test/java/com/hfut/buaa/data/test/files/test.txt"),
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

    @Test
    public void imageTest() {
        long userId = 888l;
        long bucketInstId = 8888l;
        long dataInstId = 88888l;
        String filePath = "/Users/tanweihan/workspace/data_manager/data_manager/src/test/java/com/hfut/buaa/data/test/files/image.jpg";
        DataInst dataInst = new DataInst();
        dataInst.setDataInstName("testImage");
        dataInst.setFileString(FileUtils.covFile2String(filePath));
        dataInst.setUserId(userId);
        dataInst.setBucketId(bucketInstId);
        dataInst.setDataInstId(dataInstId);
        Set<Authority> set = new HashSet<Authority>();
        Authority authority = new DataInstAuthority();
        authority.setUserId(userId);
        authority.setInstId(dataInstId);
        authority.setAuthority(AuthorityType.WRITE.getTypeId());
        set.add(authority);
        dataInst.setAuthoritySet(set);
        String uri = "http://localhost:8080/data_manager/spring/user/{userId}/bucket/{bucketInstId}/dataInst/{dataInstId}";
        try {
            restTemplate.postForLocation(uri, JSON.toJSONString(dataInst), userId, bucketInstId, dataInstId);
        } catch (HttpClientErrorException e) {
            assertEquals("this dataInstId " + dataInstId + " is exist",
                    e.getResponseHeaders().get("Message").get(0));
            assertEquals(HttpStatus.NOT_ACCEPTABLE, e.getStatusCode());
        }

        String imageString = restTemplate.getForObject(uri, String.class, userId, bucketInstId, dataInstId);
        FileUtils.writeFile(imageString,
                "/Users/tanweihan/workspace/data_manager/data_manager/src/test/java/com/hfut/buaa/data/test/files/imageCopy.jpg");
    }


    @Test
    public void fileCopyTest() {
        String filePath = "/Users/tanweihan/workspace/data_manager/data_manager/src/test/java/com/hfut/buaa/data/test/files/image.jpg";
        String copyPath = "/Users/tanweihan/workspace/data_manager/data_manager/src/test/java/com/hfut/buaa/data/test/files/imageCopy.jpg";
        FileUtils.fileCopy(filePath, copyPath);
    }
}
