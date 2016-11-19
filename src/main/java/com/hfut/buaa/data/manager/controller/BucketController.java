package com.hfut.buaa.data.manager.controller;

import com.hfut.buaa.data.manager.exception.CreateDataInstValidationException;
import com.hfut.buaa.data.manager.exception.DataInstNotInThisBucketException;
import com.hfut.buaa.data.manager.exception.DataInstsNotFoundException;
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

    @RequestMapping(value = "/Users/{userId}/bucketInst/{bucketInstId}/dataInst/{dataInstId}",
            method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteDataInst(@PathVariable long userId,
                               @PathVariable long bucketInstId, @PathVariable long dataInstId,
                               HttpServletRequest request, HttpServletResponse response) {
        bucketInstDao.deleteDataInst(userId, bucketInstId, dataInstId);
    }

    @RequestMapping(value = "/Users/{userId}/bucket/{bucketInstId}/dataInsts",
            method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    public void addDataInst(@PathVariable("userId") long userId, @PathVariable("bucketInstId") long bucketInstId,
                            @RequestBody String json, HttpServletRequest request, HttpServletResponse response) {
        bucketInstDao.addDataInst(userId, bucketInstId, new DataInst(json));
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

