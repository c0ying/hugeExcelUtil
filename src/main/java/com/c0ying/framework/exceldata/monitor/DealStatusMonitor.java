package com.c0ying.framework.exceldata.monitor;

public interface DealStatusMonitor {

	void setExportStatus(String taskUid, String status);

	void setExportErrorMsg(String taskUid, String errorMsg);

	String getErrorMsg(String taskUid);

	void markFinished(String taskUid);

	String getExportStatus(String taskUid);

	boolean isFinished(String taskUid);

	boolean isException(String taskUid);

	void clearExportStatus(String taskUid);

}