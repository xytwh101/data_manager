package com.hfut.buaa.data.test;

import com.hfut.buaa.data.manager.model.BucketInst;
import com.hfut.buaa.data.manager.model.User;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    }

    @Test
    public void getBucketsTest() {
        long userId = 111;
        String uri = "http://localhost:8080/data_manager/spring/Users/{userId}/buckets";
        Set<BucketInst> bucketInst = restTemplate.getForObject(uri, Set.class, userId);
        assertEquals(2, bucketInst.size());
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
            assertEquals("this bucketId " + bucketId + " is exist",
                    e.getResponseHeaders().get("Message").get(0));
            assertEquals(HttpStatus.NOT_ACCEPTABLE, e.getStatusCode());
        }
        try {
            restTemplate.put(uri, bucketInst, warnUserId);
        } catch (HttpClientErrorException e) {
            assertEquals("userId is not equal between " +
                            "parameter userId and Instance BucketInst",
                    e.getResponseHeaders().get("Message").get(0));
            assertEquals(HttpStatus.NOT_ACCEPTABLE, e.getStatusCode());
        }
    }
}
