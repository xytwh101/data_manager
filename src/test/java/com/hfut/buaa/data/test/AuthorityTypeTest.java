package com.hfut.buaa.data.test;

import com.hfut.buaa.data.manager.utils.AuthorityType;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tanweihan on 16/11/11.
 */
public class AuthorityTypeTest extends TestCase {
    @Test
    public void testId() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(0, "NO");
        map.put(1, "READ");
        map.put(2, "WRITE");
        AuthorityType[] arr = AuthorityType.values();
        for (AuthorityType type : arr) {
            int id = type.getTypeId();
            assertEquals(type.name(), map.get(id));
        }
    }

    @Test
    public void addAuthorityTest() {
        String uri = "http://localhost:8080/data_manager/spring" +
                "/Users/{userId}/bucket/{bucketInstId}/authority/{authorityUserId}";

    }

    @Test
    public void deleteAuthorityTest() {
        String uri = "http://localhost:8080/data_manager/spring" +
                "/Users/{userId}/bucket/{bucketInstId}/authority/{authorityUserId}";

    }
}
