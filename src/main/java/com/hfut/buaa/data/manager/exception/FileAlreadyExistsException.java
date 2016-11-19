package com.hfut.buaa.data.manager.exception;

/**
 * Created by tanweihan on 16/11/18.
 */
public class FileAlreadyExistsException extends RuntimeException{
    public FileAlreadyExistsException(String message) {
        super(message);
    }
}
