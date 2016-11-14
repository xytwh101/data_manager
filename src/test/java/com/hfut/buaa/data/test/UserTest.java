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
    public void createUserTest() {
        // TODO
    }

    @Test
    public void loginTest() {
        long userId = 111;
        String password = "111";
        String uri = "http://localhost:8080/data_manager/spring/Users/{userId}/password/{password}";
        User user = restTemplate.getForObject(uri, User.class, userId, password);
        assertEquals(user.getUserId(), userId);
        assertEquals(user.getPassword(), password);
        assertEquals(user.getUserName(), "twh");
    }

    @Test
    public void deleteUserTest() {
        long userId = 333;
        String password = "123";
        String uri = "http://localhost:8080/data_manager/spring/Users/{userId}/password/{password}";
        restTemplate.put(uri, null, userId, password);
        // TODO

    }

    @Test
    public void getBucketsTest() {
        long userId = 111;
        String uri = "http://localhost:8080/data_manager/spring/Users/{userId}/buckets";
        String string = restTemplate.getForObject(uri, String.class, userId);
        Set<BucketInst> bucketInsts = restTemplate.getForObject(uri, HashSet.class, userId);
        assertEquals(2, bucketInsts.size());
    }

    @Test
    public void getBucket() {
        String uri = "http://localhost:8080/data_manager/spring/Users/{userId}/bucket/{bucketInstId}";
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
                Set<Authority> dautho = dataInst.getAuthoritySet();
                assertEquals(dautho.size(), 1);
                for (Authority authority : dautho) {
                    assertEquals(userId, authority.getUserId());
                    assertEquals(authority.getInstId(), dataInst.getDataInstId());
                    assertEquals(authority.getAuthority(), AuthorityType.WRITE.getTypeId());
                }
            }

            Set<Authority> bautho = bucketInst.getAuthoritySet();
            assertEquals(bautho.size(), 1);
            for (Authority authority : bautho) {
                assertEquals(userId, authority.getUserId());
                assertEquals(authority.getInstId(), bucketInst.getBucketId());
                assertEquals(authority.getAuthority(), AuthorityType.WRITE.getTypeId());
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
        String uri = "http://localhost:8080/data_manager/spring/Users/{userId}/bucketInst";
        try {
            restTemplate.put(uri, bucketInst, userId);
        } catch (HttpClientErrorException e) {
            System.out.println("a");
            assertEquals("this bucketId " + bucketId + " is exist",
                    e.getResponseHeaders().get("Message").get(0));
            assertEquals(HttpStatus.NOT_ACCEPTABLE, e.getStatusCode());
        }
        try {
            restTemplate.put(uri, bucketInst, warnUserId);
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
        // TODO


    }
}
