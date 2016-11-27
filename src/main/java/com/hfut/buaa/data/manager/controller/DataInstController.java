package com.hfut.buaa.data.manager.controller;

import com.hfut.buaa.data.manager.repository.DataInstDao;
import com.hfut.buaa.data.manager.utils.FileUtils;
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
        FileUtils.writeFile(fileString,
                "/Users/tanweihan/twhGit/data_manager/src/test/java/com/hfut/buaa/data/test/files/test3.txt");
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
