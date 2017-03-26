package com.hfut.buaa.data.manager.exception;

/**
 * Created by tanweihan on 2017/3/19.
 */
public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
