package com.hfut.buaa.data.test;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;

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
        String filePath = "/Users/tanweihan/twhGit/data_manager/src/test/java/com/hfut/buaa/data/test/files/test.txt";
        File file = new File(filePath);
        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream = null;
        try {
            byte[] bytes = new byte[1024 * 512];
            inputStream = new FileInputStream(file);
            while (inputStream.read(bytes) != -1) {
                stringBuilder.append(new String(bytes, "utf-8"));
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String string = stringBuilder.toString();
        if (string.length() > 0) {
            restTemplate.postForLocation(uri, string, "test");
        }
    }
}
