package com.c0ying.framework.exceldata.utils;

public class Assert {

    public static void notNull(Object object, String message){
        if (object == null) {
            if (StringUtils.isBlank(message)){
                throw new RuntimeException();
            }else{
                throw new RuntimeException(message);
            }
        }
    }
}
