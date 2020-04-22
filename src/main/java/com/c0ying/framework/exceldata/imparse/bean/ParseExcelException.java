package com.c0ying.framework.exceldata.imparse.bean;

public class ParseExcelException extends Exception{

	private static final long serialVersionUID = 3454488971380183641L;

	public ParseExcelException(String message) {
        super(message);
    }
	
	public ParseExcelException(String message, Throwable cause) {
		super(message, cause);
	}
}
