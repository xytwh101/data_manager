package com.hfut.buaa.data.manager.controller;

import com.hfut.buaa.data.manager.repository.BucketInstDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by tanweihan on 16/11/14.
 */
@Controller
public class BucketController {
    @Autowired(required = true)
    private BucketInstDao bucketInstDao;

    @RequestMapping(value = "/Users/{userId}/bucketInst/{bucketInstId}/dataInst/{dataInstId}",
            method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteDataInst(@PathVariable long userId,
                               @PathVariable long bucketInstId, @PathVariable long dataInstId,
                               HttpServletRequest request, HttpServletResponse response) {
        bucketInstDao.deleteDataInst(userId, bucketInstId, dataInstId);
    }
}

