package com.c0ying.framework.exceldata.export;

import com.c0ying.framework.exceldata.export.dataproducer.SimpleDataAsyncProducer;


public interface DataExportExcelHandler {

	void handler(String taskId, SimpleDataAsyncProducer<?> data);

	String getExportStatus(String taskId);
}