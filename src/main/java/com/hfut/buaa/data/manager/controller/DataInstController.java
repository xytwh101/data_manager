package com.hfut.buaa.data.manager.controller;

import com.hfut.buaa.data.manager.repository.DataInstDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * Created by tanweihan on 16/11/17.
 */
@Controller
public class DataInstController {
    @Autowired(required = true)
    private DataInstDao dataInstDao;

    @RequestMapping(value = "/save/{fileName}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    public void saveFileToHdfsTest(@RequestBody String fileString, @PathVariable String fileName) {
        byte[] bytes = new byte[1024 * 512];
        File file = new File("/Users/tanweihan/twhGit/data_manager/src/test/java/com/hfut/buaa/data/test/files/test2.txt");
        OutputStream outputStream;
        try {
            bytes = fileString.getBytes("utf-8");
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
