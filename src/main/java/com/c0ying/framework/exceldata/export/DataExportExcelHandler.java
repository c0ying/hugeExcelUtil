package com.c0ying.framework.exceldata.export;

import com.c0ying.framework.exceldata.export.dataproducer.SimpleDataAsyncProducer;

import java.util.Map;


public interface DataExportExcelHandler {

	void handler(String taskId, SimpleDataAsyncProducer<?> data, Map<String, Object> taskContext);

	String getExportStatus(String taskId);
}
