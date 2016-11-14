package com.hfut.buaa.data.test;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

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
        String uri = "http://localhost:8080/data_manager/spring/Users/{userId}/" +
                "bucketInst/{bucketInstId}/dataInst/{dataInstId}";
        restTemplate.delete(uri, userId, bucketInstId, dataInstId);
    }

}
