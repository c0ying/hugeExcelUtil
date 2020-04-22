package com.c0ying.framework.exceldata.delegate;

import com.c0ying.framework.exceldata.imparse.SimpleExcelParser;
import com.c0ying.framework.exceldata.imparse.bean.ParseExcelException;

import java.util.List;

public interface ExcelFileReadDelegater {

    <T> void readExcel(String fileUrl, SimpleExcelParser<T> simpleExcelParser) throws Exception;
}
