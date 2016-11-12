package com.hfut.buaa.data.test;

import com.hfut.buaa.data.manager.model.BucketInst;
import com.hfut.buaa.data.manager.model.User;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
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
    public void getBucketsTest() {
        long userId = 111;
        String uri = "http://localhost:8080/data_manager/spring/Users/{userId}/buckets";
        Set<BucketInst> bucketInst = restTemplate.getForObject(uri, Set.class, userId);
        assertEquals(2, bucketInst.size());
    }
}
