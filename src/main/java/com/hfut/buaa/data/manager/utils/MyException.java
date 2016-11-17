package com.hfut.buaa.data.manager.utils;
public class MyException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MyException(String msg){
		super(msg);
	}
	public MyException(Exception e,String msg){
		super(msg);
		e.printStackTrace();
	}
}
