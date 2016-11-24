package com.hfut.buaa.data.manager.controller;

import com.hfut.buaa.data.manager.repository.DataInstDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by tanweihan on 16/11/17.
 */
@Controller
public class DataInstController {
    @Autowired(required = true)
    private DataInstDao dataInstDao;

    @RequestMapping(value = "/save/{fileName}", method = RequestMethod.POST)
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

    @RequestMapping(value = "/user/{userId}/bucket/{bucketInstId}/dataInst/{dataInstId}/addFile", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void addFileToHdfs(@RequestBody String fileString, @PathVariable long userId,
                              @PathVariable long bucketId, @PathVariable long dataInstId,
                              HttpServletRequest request, HttpServletResponse response) {
        dataInstDao.updateFileString(userId, bucketId, dataInstId, fileString, false);
    }

    @RequestMapping(value = "/user/{userId}/bucket/{bucketInstId}/dataInst/{dataInstId}/updateFile", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateFileToHdfs(@RequestBody String fileString, @PathVariable long userId,
                                 @PathVariable long bucketId, @PathVariable long dataInstId,
                                 HttpServletRequest request, HttpServletResponse response) {
        dataInstDao.updateFileString(userId, bucketId, dataInstId, fileString, true);
    }

}
