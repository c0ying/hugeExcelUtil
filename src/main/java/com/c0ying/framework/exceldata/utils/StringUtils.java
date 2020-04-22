package com.c0ying.framework.exceldata.utils;

public class StringUtils {

    public static boolean isBlank(String content){
        if (content == null || content.trim().length() <= 0){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isNotBlank(String content){
        return !isBlank(content);
    }

    public static String[] tokenizer(String string, String delimiter){
        if (isNotBlank(string)) {
            return string.split(delimiter);
        }else{
            return new String[0];
        }
    }
}
