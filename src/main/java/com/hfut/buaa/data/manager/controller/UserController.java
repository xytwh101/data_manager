package com.hfut.buaa.data.manager.controller;

import com.hfut.buaa.data.manager.exception.UserNotFoundException;
import com.hfut.buaa.data.manager.model.BucketInst;
import com.hfut.buaa.data.manager.model.User;
import com.hfut.buaa.data.manager.repository.UserDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by tanweihan on 16/11/11.
 */
@Controller
public class UserController {
    private static Logger logger = Logger.getLogger("main");

    @Autowired(required = true)
    private UserDao userDao;

    /**
     * 获取User实例，用于测试功能
     *
     * @param userId
     * @param password
     * @return
     */
    @RequestMapping(value = "/Users/{userId}/password/{password}", method = RequestMethod.GET)
    @ResponseBody
    public User getUser(@PathVariable long userId, @PathVariable String password) {
        return userDao.getUserByPassword(userId, password);
    }

    /**
     * 创建user
     *
     * @param userId
     * @param userName
     * @param request
     * @param response
     */
    @RequestMapping(value = "/Users/{userId}/userName/{userName}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@PathVariable long userId, @PathVariable String userName,
                           HttpServletRequest request, HttpServletResponse response) {
        userDao.createUser(new User(userId, userName));
        response.setHeader("Location", request.getRequestURL().append("/").append(userId).toString());
    }

    /**
     * 返回用户自己创建的bucket以及有权限读取的bucket
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/Users/{userId}/buckets", method = RequestMethod.GET)
    @ResponseBody
    public Set<BucketInst> getBuckets(@PathVariable long userId) {
        return userDao.getBuckets(userId);
    }


    /**
     * 处理未找到user的异常
     *
     * @param pe
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({UserNotFoundException.class})
    public void handleUserNotFound(UserNotFoundException pe) {
        logger.warn(pe.getMessage());
    }

    @InitBinder
    public void testBinder(WebDataBinder binder, WebRequest req) {
        System.out.println();
    }
}