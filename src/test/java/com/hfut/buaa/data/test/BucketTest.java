package com.hfut.buaa.data.test;

import com.hfut.buaa.data.manager.model.Authority;
import com.hfut.buaa.data.manager.model.DataInst;
import com.hfut.buaa.data.manager.model.DataInstAuthority;
import com.hfut.buaa.data.manager.utils.AuthorityType;
import com.hfut.buaa.data.manager.utils.FileUtils;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.alibaba.fastjson.*;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by tanweihan on 16/11/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:root-context.xml",
        "classpath:app-context.xml"})
public class BucketTest extends TestCase {
    private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void deleteDataInstTest() {
        long userId = 444l;
        long bucketInstId = 4444l;
        long dataInstId = 44444l;
        addDataInstTest();
        String uri = "http://localhost:8080/data_manager/spring/user/{userId}/" +
                "bucket/{bucketInstId}/dataInst/{dataInstId}";
        try {
            restTemplate.delete(uri, userId, bucketInstId, dataInstId);
            restTemplate.delete(uri, userId, bucketInstId, dataInstId);
        } catch (HttpClientErrorException e) {
            assertEquals("this dataInst that id is " + dataInstId +
                            " is not in the bucket where the bucketId is " + bucketInstId,
                    e.getResponseHeaders().get("Message").get(0));
            assertEquals(HttpStatus.NOT_ACCEPTABLE, e.getStatusCode());
        }
    }

    @Test
    public void addDataInstTest() {
        long userId = 444l;
        long bucketInstId = 4444l;
        long dataInstId = 44444l;
        String filePath = "/Users/tanweihan/twhGit/data_manager/src/test/java/com/hfut/buaa/data/test/files/test.txt";
        DataInst dataInst = new DataInst();
        dataInst.setDataInstName("testDelete");
        dataInst.setFilePath("test");
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
            // restTemplate.put(uri, JSON.toJSONString(dataInst), userId, bucketInstId);
        } catch (HttpClientErrorException e) {
            assertEquals("this dataInstId " + dataInstId + " is exist",
                    e.getResponseHeaders().get("Message").get(0));
            assertEquals(HttpStatus.NOT_ACCEPTABLE, e.getStatusCode());
        }
    }

    @Test
    public void updateDataInstTest() {
        String uri = "http://localhost:8080/data_manager/spring/user/{userId}/bucket/{bucketInstId}/dataInst/{dataInstId}";
        long userId = 444l;
        long bucketInstId = 4444l;
        long dataInstId = 44444l;
        String filePathNew = "/Users/tanweihan/twhGit/data_manager/src/test/java/com/hfut/buaa/data/test/files/test3.txt";
        DataInst dataInst = new DataInst();
        String fileString = FileUtils.covFile2String(filePathNew);
        dataInst.setFileString(fileString);
        dataInst.setUserId(userId);
        dataInst.setBucketId(bucketInstId);
        dataInst.setDataInstId(dataInstId);
        dataInst.setDataInstName("testUpdate");
        restTemplate.put(uri, dataInst, userId, bucketInstId, dataInstId);

        DataInst dataInstNew = new DataInst(
                restTemplate.getForObject(uri, String.class, userId, bucketInstId, dataInstId));

        assertEquals(dataInst.getDataInstName(), dataInstNew.getDataInstName());
        // assertEquals(dataInstNew.getFileString().equals(fileString), true);
        String filePathOld = "/Users/tanweihan/twhGit/data_manager/src/test/java/com/hfut/buaa/data/test/files/test.txt";
        dataInst.setFileString(FileUtils.covFile2String(filePathOld));
        dataInst.setDataInstName("testDelete");
        restTemplate.put(uri, dataInst, userId, bucketInstId, dataInstId);
    }

    @Test
    public void getDataInstTest() {
        String uri = "http://localhost:8080/data_manager/spring/user/{userId}/bucket/{bucketInstId}/dataInst/{dataInstId}";
        long userId = 444l;
        long bucketInstId = 4444l;
        long dataInstId = 44444l;
        String filePath = "/Users/tanweihan/twhGit/data_manager/src/test/java/com/hfut/buaa/data/test/files/test.txt";
        String jsonString = restTemplate.
                getForObject(uri, String.class, userId, bucketInstId, dataInstId);
        DataInst dataInst = new DataInst(jsonString);
        assertEquals(userId, dataInst.getUserId());
        assertEquals(bucketInstId, dataInst.getBucketId());
        assertEquals(dataInstId, dataInst.getDataInstId());
        assertEquals("testDelete", dataInst.getDataInstName());
        assertEquals(FileUtils.covFile2String(filePath), dataInst.getFileString());
    }

}
