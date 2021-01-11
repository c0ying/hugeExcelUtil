package com.c0ying.framework.exceldata.export.dataproducer;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据导出处理者
 * @author Cyh
 *
 * @param <T>
 */
public abstract class SimpleDataProducer<T> implements DataProducer<T>, FormateExportData<T>{

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

	public boolean hasMore() {
		return getCurrent() <= getTotal();
	}

	public long getTotal() {
		return (long) getContext().get("T_TOTAL");
	}

	public long getCurrent() {
		return (long) getContext().get("T_CURRENT");
	}

	public void setTotal(long total){
		getContext().put("T_TOTAL", total);
	}

	public void setCurrent(long current){
		getContext().put("T_CURRENT", current);
	}

	public T next() {
		setCurrent(getCurrent()+1);
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
		setCurrent(1);
	}
	
	public void destroy() {
		run_context.remove();
	}
}
