package com.c0ying.framework.exceldata.export;

import com.c0ying.framework.exceldata.export.dataproducer.SimpleDataProducer;

import java.util.Map;


public interface DataExportExcelHandler {

	void handler(String taskId, SimpleDataProducer<?> data, Map<String, Object> taskContext);

	void handler(String taskId, SimpleDataProducer<?> data);

	void handler(SimpleDataProducer<?> data, Map<String, Object> taskContext);

	void handler(SimpleDataProducer<?> data);

	String getExportStatus(String taskId);
}
