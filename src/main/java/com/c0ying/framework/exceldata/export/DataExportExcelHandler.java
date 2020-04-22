package com.c0ying.framework.exceldata.export;

import com.c0ying.framework.exceldata.export.dataproducer.SimpleDataAsyncProducer;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.Map;


public interface DataExportExcelHandler {

	void handler(String taskId, SimpleDataAsyncProducer<?> data);

	void write(String taskId, SXSSFWorkbook wb) throws Exception;

	String startTask(SimpleDataAsyncProducer<?> dataProducer, Map<String, Object> params);
}