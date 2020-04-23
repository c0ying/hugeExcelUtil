package com.c0ying.framework.exceldata.export.bean;

public class ExportExcelDataProperties {

	private String tmpFilePath = System.getProperty("java.io.tmpdir");

	public String getTmpFilePath() {
		return tmpFilePath;
	}

	public void setTmpFilePath(String tmpFilePath) {
		this.tmpFilePath = tmpFilePath;
	}

}
