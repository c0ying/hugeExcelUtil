package com.c0ying.framework.exceldata.export.bean;

public class ExportExcelDataProperties {

	private String tmpFilePath = System.getProperty("java.io.tmpdir");
	private int runTask = 10;
	private int queueTask = 5;

	public int getRunTask() {
		return runTask;
	}

	public void setRunTask(int runTask) {
		this.runTask = runTask;
	}

	public int getQueueTask() {
		return queueTask;
	}

	public void setQueueTask(int queueTask) {
		this.queueTask = queueTask;
	}

	public String getTmpFilePath() {
		return tmpFilePath;
	}

	public void setTmpFilePath(String tmpFilePath) {
		this.tmpFilePath = tmpFilePath;
	}

}
