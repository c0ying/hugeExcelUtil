package com.c0ying.framework.exceldata;

public interface DealStatusMonitor {

	void setExportStatus(String taskUid, String status);

	String getExportStatus(String taskUid);

	void clearExportStatus(String taskUid);

}