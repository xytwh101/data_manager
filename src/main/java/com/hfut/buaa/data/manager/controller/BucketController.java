package com.hfut.buaa.data.manager.controller;

import com.hfut.buaa.data.manager.exception.CreateDataInstValidationException;
import com.hfut.buaa.data.manager.exception.DataInstNotInThisBucketException;
import com.hfut.buaa.data.manager.exception.DataInstsNotFoundException;
import com.hfut.buaa.data.manager.exception.UserNotMatchException;
import com.hfut.buaa.data.manager.model.DataInst;
import com.hfut.buaa.data.manager.model.ResourceInst;
import com.hfut.buaa.data.manager.repository.BucketInstDao;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by tanweihan on 16/11/14.
 */
@Controller
public class BucketController {
    @Autowired(required = true)
    private BucketInstDao bucketInstDao;
    @Autowired
    private ResourceInst resourceInst;

    @RequestMapping(value = "/user/{userId}/bucket/{bucketInstId}/dataInst/{dataInstId}",
            method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteDataInst(@PathVariable long userId,
                               @PathVariable long bucketInstId, @PathVariable long dataInstId,
                               HttpServletRequest request, HttpServletResponse response) {
        bucketInstDao.deleteDataInst(userId, bucketInstId, dataInstId);
    }

    @RequestMapping(value = "/user/{userId}/bucket/{bucketInstId}/dataInst/{dataInstId}",
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void addDataInst(@PathVariable("userId") long userId, @PathVariable("bucketInstId") long bucketInstId,
                            @PathVariable("dataInstId") long dataInstId, @RequestBody String json,
                            HttpServletRequest request, HttpServletResponse response) {
        DataInst dataInst = new DataInst(json);
        if (userId == dataInst.getUserId()
                && bucketInstId == dataInst.getBucketId()
                && dataInstId == dataInst.getDataInstId())
            bucketInstDao.addDataInst(userId, bucketInstId, dataInst);
        else
            throw new UserNotMatchException("");
    }

    @RequestMapping(value = "/user/{userId}/bucket/{bucketInstId}/dataInst/{dataInstId}",
            method = RequestMethod.GET)
    @ResponseBody
    public DataInst getDataInst(@PathVariable("userId") long userId,
                                @PathVariable("bucketInstId") long bucketInstId,
                                @PathVariable("dataInstId") long dataInstId,
                                HttpServletRequest request, HttpServletResponse response) {
        DataInst dataInst = bucketInstDao.getDataInst(userId, bucketInstId, dataInstId);
        return dataInst;
    }

    @RequestMapping(value = "/user/{userId}/bucket/{bucketInstId}/dataInst/{dataInstId}",
            method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateDataInst(@PathVariable("userId") long userId,
                                   @PathVariable("bucketInstId") long bucketInstId,
                                   @PathVariable("dataInstId") long dataInstId,
                                   @RequestBody String jsonString,
                                   HttpServletRequest request, HttpServletResponse response) {
        DataInst dataInst = new DataInst(jsonString);
        if (userId == dataInst.getUserId()
                && bucketInstId == dataInst.getBucketId()
                && dataInstId == dataInst.getDataInstId()) {
            bucketInstDao.updateDataInst(userId, bucketInstId, dataInst);
        } else {
            throw new UserNotMatchException("");
        }
    }

    /**
     * 处理添加Bucket，userId、bucketId等参数的验证异常
     *
     * @param ex
     */
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(CreateDataInstValidationException.class)
    public void handleCreateDataInstValidation(CreateDataInstValidationException ex,
                                               HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Message", ex.getMessage());
    }

    /**
     * 处理未找到此DataInst异常
     *
     * @param ex
     */
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(DataInstNotInThisBucketException.class)
    public void handleDataInstNotInThisBucket(DataInstNotInThisBucketException ex,
                                              HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Message", ex.getMessage());
    }

    /**
     * 处理未找到此Authority异常
     *
     * @param ex
     */
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(DataInstsNotFoundException.class)
    public void handleDataInstsNotFoundException(DataInstNotInThisBucketException ex,
                                                 HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Message", ex.getMessage());
    }
}

