package com.c0ying.framework.exceldata.export.dataproducer;

/**
 * 数据格式化为字符串
 * @author Cyh
 *
 * @param <T>
 */
public interface FormateExportData<T> {

	String[] formate(T data);
	
	String delimiterFlag();
}
