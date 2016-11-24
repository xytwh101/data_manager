package com.hfut.buaa.data.test;

import com.hfut.buaa.data.manager.model.Authority;
import com.hfut.buaa.data.manager.model.BucketInst;
import com.hfut.buaa.data.manager.model.DataInst;
import com.hfut.buaa.data.manager.model.User;
import com.hfut.buaa.data.manager.utils.AuthorityType;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.alibaba.fastjson.JSON;

import java.net.URI;
import java.util.*;

/**
 * Created by tanweihan on 16/11/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:root-context.xml",
        "classpath:app-context.xml"})
public class UserTest extends TestCase {
    private RestTemplate restTemplate = new RestTemplate();
    private final long TEST_USER_ID = 123456;
    private final String TEST_PASSWORD = "123";

    @Test
    public void loginTest() {
        long userId = 111;
        String password = "111";
        String uri = "http://localhost:8080/data_manager/spring/user/{userId}/password/{password}";
        User user = restTemplate.getForObject(uri, User.class, userId, password);
        assertEquals(user.getUserId(), userId);
        assertEquals(user.getPassword(), password);
        assertEquals(user.getUserName(), "twh");
    }

    @Test
    public void getBucketsTest() {
        long userId = 111;
        String uri = "http://localhost:8080/data_manager/spring/user/{userId}/buckets";
        String string = restTemplate.getForObject(uri, String.class, userId);
        Set<BucketInst> bucketInsts = restTemplate.getForObject(uri, HashSet.class, userId);
        assertEquals(2, bucketInsts.size());
    }

    @Test
    public void getBucket() {
        String uri = "http://localhost:8080/data_manager/spring/user/{userId}/bucket/{bucketInstId}";
        long userId = 111;
        Map<Long, String> bucketMap = new HashMap<Long, String>();
        bucketMap.put(1l, "buck1");
        bucketMap.put(2l, "buck2");
        Map<Long, String> dataMap = new HashMap<Long, String>();
        dataMap.put(1l, "aaaaaa");
        dataMap.put(2l, "bbbbbb");
        dataMap.put(3l, "cccccc");
        dataMap.put(4l, "dddddd");
        for (Map.Entry<Long, String> entry : bucketMap.entrySet()) {
            String jsonString = restTemplate.getForObject(uri, String.class, userId, entry.getKey());
            BucketInst bucketInst = new BucketInst(jsonString);
            Set<DataInst> dataInsts = bucketInst.getDataInsts();
            assertEquals(2, dataInsts.size());
            assertEquals(bucketInst.getBucketName(), bucketMap.get(bucketInst.getBucketId()));
            for (DataInst dataInst : dataInsts) {
                assertEquals(dataInst.getFilePath(), dataMap.get(dataInst.getDataInstId()));
            }
            Set<Authority> bautho = bucketInst.getAuthoritySet();
            assertEquals(bautho.size(), 2);
            for (Authority authority : bautho) {
                if (authority.getUserId() == 555l || authority.getUserId() == 666l) {
                    continue;
                } else {
                    assertEquals(userId, authority.getUserId());
                    assertEquals(authority.getInstId(), bucketInst.getBucketId());
                    assertEquals(authority.getAuthority(), AuthorityType.WRITE.getTypeId());
                }
            }
        }
        // 测试获取权限为读取的Bucket获取功能
        long[][] arr = {{555l, 1l}, {666l, 2l}};
        for (long[] entry : arr) {
            String json = restTemplate.getForObject(uri, String.class, entry[0], entry[1]);
            BucketInst bucketInst = new BucketInst(json);
            Set<DataInst> dataInsts = bucketInst.getDataInsts();
            assertEquals(2, dataInsts.size());
            assertEquals(bucketInst.getBucketName(), bucketMap.get(bucketInst.getBucketId()));
            for (DataInst dataInst : dataInsts) {
                assertEquals(dataInst.getFilePath(), dataMap.get(dataInst.getDataInstId()));
            }
            Set<Authority> bautho = bucketInst.getAuthoritySet();
            assertEquals(bautho.size(), 2);
            for (Authority authority : bautho) {
                if (authority.getUserId() == 555l || authority.getUserId() == 666l) {
                    assertEquals(entry[0], authority.getUserId());
                    assertEquals(authority.getInstId(), entry[1]);
                    assertEquals(authority.getAuthority(), AuthorityType.READ.getTypeId());
                } else {
                    continue;
                }
            }
        }
    }

    @Test
    public void createBucketTest() {
        long userId = 222l;
        long warnUserId = 333l;
        long bucketId = 123l;
        BucketInst bucketInst = new BucketInst();
        bucketInst.setUserId(userId);
        bucketInst.setBucketId(bucketId);
        bucketInst.setBucketName("bucketTest");
        String uri = "http://localhost:8080/data_manager/spring/user/{userId}/bucket/{bucketInstId}";
        try {
            restTemplate.postForLocation(uri, bucketInst, userId, bucketId);
        } catch (HttpClientErrorException e) {
            System.out.println("a");
            assertEquals("this bucketId " + bucketId + " is exist",
                    e.getResponseHeaders().get("Message").get(0));
            assertEquals(HttpStatus.NOT_ACCEPTABLE, e.getStatusCode());
        }
        try {
            restTemplate.postForLocation(uri, bucketInst, warnUserId, bucketId);
        } catch (HttpClientErrorException e) {
            System.out.println("b");
            assertEquals("userId is not equal between " +
                            "parameter userId and Instance BucketInst",
                    e.getResponseHeaders().get("Message").get(0));
            assertEquals(HttpStatus.NOT_ACCEPTABLE, e.getStatusCode());
        }
    }

    @Test
    public void deleteBucketInst() {
        createBucketTest();
        long userId = 222l;
        long warnUserId = 333l;
        long bucketId = 123l;
        String uri = "http://localhost:8080/data_manager/spring/user/{userId}/bucket/{bucketInstId}";
        restTemplate.delete(uri, userId, bucketId);
        try {
            restTemplate.getForObject(uri, String.class, userId, bucketId);
        } catch (HttpClientErrorException e) {
            assertEquals("bucketInst is not found when give userId = "
                    + userId + " and bucketId = " + bucketId, e.getResponseHeaders().get("Message").get(0));
        }
    }

    @Test
    public void updateBucketInst() {
        String uri = "http://localhost:8080/data_manager/spring/user/{userId}/bucket/{bucketInstId}";
        long userId = 111l;
        long bucketInstId = 1l;
        String bucketName = "buck";
        BucketInst bucketInst = new BucketInst();
        bucketInst.setUserId(userId);
        bucketInst.setBucketName(bucketName);
        bucketInst.setBucketId(bucketInstId);
        restTemplate.put(uri, bucketInst, userId, bucketInstId);
        bucketInst = new BucketInst(restTemplate.getForObject(uri, String.class, userId, bucketInstId));
        assertEquals(bucketName, bucketInst.getBucketName());
        for (DataInst dataInst : bucketInst.getDataInsts()) {
            assertEquals(bucketName, dataInst.getBucketName());
        }
        bucketName = "buck1";
        bucketInst.setBucketName(bucketName);
        bucketInst.setDataInsts(new HashSet<DataInst>());
        bucketInst.setAuthoritySet(new HashSet<Authority>());
        restTemplate.put(uri, bucketInst, userId, bucketInstId);
    }

    @Test
    public void updateBucketAuthority() {
        String uri = "http://localhost:8080/data_manager/spring/" +
                "user/{userId}/bucket/{bucketInstId}/user/{authorityUserId}";
        long userId = 111l;
        long authorityId = 666l;
        long bucketId = 1l;
        restTemplate.postForLocation(uri, null, userId, bucketId, authorityId);
        String uri2 = "http://localhost:8080/data_manager/spring/" +
                "user/{userId}/bucket/{bucketInstId}";
        BucketInst bucketInst = new BucketInst(
                restTemplate.getForObject(uri2, String.class, authorityId, bucketId));
        assertEquals(userId, bucketInst.getUserId());
        assertEquals(AuthorityType.READ.getTypeId(),
                bucketInst.getAuthorityMap().get(authorityId).intValue());

        restTemplate.delete(uri, userId, bucketId, authorityId);
//        try {
//            bucketInst = new BucketInst(
//                    restTemplate.getForObject(uri2, String.class, authorityId, bucketId));
//        } catch (HttpClientErrorException e) {
//            assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
//        }
    }
}
