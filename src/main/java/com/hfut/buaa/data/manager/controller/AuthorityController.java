package com.hfut.buaa.data.manager.controller;

import com.hfut.buaa.data.manager.repository.AuthorityDao;
import com.hfut.buaa.data.manager.repository.DataInstDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.*;

/**
 * Created by tanweihan on 16/11/17.
 */
@Controller
public class AuthorityController {
    @Autowired(required = true)
    private AuthorityDao authorityDao;
    
}
