package com.c0ying.framework.exceldata.imparse;

import com.c0ying.framework.exceldata.imparse.bean.ParseExcelException;

import java.util.List;
import java.util.Map;

public interface ImportProcesser<T> extends ImportMeta{

	void preHandler(List<List<String>> datas, Map<String,Object> context);

	/**
	 * 处理转换后数据
	 * @param mT
	 * @return
	 */
	List<T> process(List<T> mT) throws ParseExcelException;
	
	/*
	 * 校验转换后的数据
	 */
	void validate(List<T> mT) throws ParseExcelException;
	
	/**
	 * 处理异常
	 * @param t
	 */
	void handleException(Throwable t);

	void destroy();
}
