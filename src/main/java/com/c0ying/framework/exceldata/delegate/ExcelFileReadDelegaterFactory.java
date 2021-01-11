package com.c0ying.framework.exceldata.delegate;

import com.c0ying.framework.exceldata.utils.Constants;

/**
 * Excel 读取解析模式
 * NORMAL: POI 基本解析方式
 * SAX: EasyExcel Sax读取解析方式
 * @author c0ying
 */
public final class ExcelFileReadDelegaterFactory {

    public static ExcelFileReadDelegater build(String runType){
        if (Constants.RUN_TYPE_NORMAL.equalsIgnoreCase(runType)){
            return new POIExcelFileReadDelegate();
        }else{
            return new SaxExcelFileReadDelegate();
        }
    }
}
