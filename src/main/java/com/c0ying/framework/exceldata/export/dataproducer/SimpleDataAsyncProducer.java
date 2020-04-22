package com.c0ying.framework.exceldata.export.dataproducer;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据导出处理者
 * @author Cyh
 *
 * @param <T>
 */
public abstract class SimpleDataAsyncProducer<T> implements DataProducer<T>, FormateExportData<T>{

	private String headTitle;
	//数据处理者使用单例多线程，相关属性需使用ThreadLocal避免并发
	protected ThreadLocal<Map<String, Object>> run_context = new ThreadLocal<>();//运行时上下文信息

	public String getHeadTitle() {
		return headTitle;
	}

	public void setHeadTitle(String headTitle) {
		this.headTitle = headTitle;
	}

	public String[] stringData() {
		if (hasMore()) {
			next();
			T t = data();
			return formate(t);
		}
		return null;
	}
	
	public String delimiterFlag() {
		return ",";
	}
	
	public Map<String,Object> getContext() {
	    return run_context.get();
	}

	public void init(Map<String, Object> param) {
		if (getContext()==null) {
			run_context.set(new HashMap<>());
		}
		run_context.get().put("param",param);
	}
	
	public void destroy() {
		run_context.remove();
	}
}
