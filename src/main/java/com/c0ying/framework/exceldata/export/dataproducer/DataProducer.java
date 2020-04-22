package com.c0ying.framework.exceldata.export.dataproducer;

import java.util.Map;

/**
 * 数据导出生产者
 * @author Cyh
 *
 * @param <T>
 */
public interface DataProducer<T> {

	boolean hasMore();
	
	long getTotal();
	
	long getCurrent();
	
	T next();
	
	T data();

	void init(Map<String, Object> param);

	void destroy();
}
