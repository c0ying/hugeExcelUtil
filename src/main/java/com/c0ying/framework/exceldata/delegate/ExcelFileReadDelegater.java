package com.c0ying.framework.exceldata.delegate;

import com.c0ying.framework.exceldata.imparse.SimpleExcelParser;

import java.util.List;

public interface ExcelFileReadDelegater {

    List<List<String>> readExcel(String fileUrl, boolean skipHead);

    void saxReadExcel(String fileUrl, SimpleExcelParser simpleExcelParser);
}
