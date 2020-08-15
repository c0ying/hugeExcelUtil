package com.c0ying.framework.exceldata.delegate;

import com.c0ying.framework.exceldata.imparse.ImportProcesser;
import com.c0ying.framework.exceldata.imparse.SimpleExcelParser;

/**
 * Excel 读取解析入口策略委托者
 */
public interface ExcelFileReadDelegater {

    <T> void readExcel(String fileUrl, SimpleExcelParser<T> simpleExcelParser) throws Exception;

    <T> void readExcel(String fileUrl, ImportProcesser<T> importProcesser) throws Exception;
}
