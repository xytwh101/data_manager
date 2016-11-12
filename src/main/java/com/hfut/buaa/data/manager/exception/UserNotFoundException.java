package com.hfut.buaa.data.manager.exception;

/**
 * Created by tanweihan on 16/11/11.
 */
public class UserNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -4372776309073614775L;

    public UserNotFoundException(String string) {
        super(string);
    }
}
