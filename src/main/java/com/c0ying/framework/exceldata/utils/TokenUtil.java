package com.c0ying.framework.exceldata.utils;

import java.util.UUID;

public class TokenUtil {

	public static String getToken(){
		return MD5.toHex(uuid());
	}
	
	public static String uuid(){
		return UUID.randomUUID().toString();
	}
}
