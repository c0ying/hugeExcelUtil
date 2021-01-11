package com.c0ying.framework.exceldata.imparse;

import com.c0ying.framework.exceldata.imparse.DefaultDataParserHandler.DealStatus;
import com.c0ying.framework.exceldata.imparse.dataparser.SimpleExcelParser;

import java.util.Map;

public interface DataParserExcelHandler {

    void readExcel(String taskId, String fileUrl, SimpleExcelParser<?> simpleExcelParser, Map<String, Object> taskContext);

    void readExcel(String taskId, String fileUrl, SimpleExcelParser<?> simpleExcelParser);

    void readExcel(String fileUrl, SimpleExcelParser<?> simpleExcelParser, Map<String, Object> taskContext);

    void readExcel(String fileUrl, SimpleExcelParser<?> simpleExcelParser);

    DealStatus getDealStatus(String taskId);
}
