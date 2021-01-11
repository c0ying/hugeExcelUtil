package com.c0ying.framework.exceldata.imparse.dataparser;

import com.c0ying.framework.exceldata.utils.Assert;
import net.sf.cglib.beans.BeanMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生命周期：
 * construct() 构建实例
 * |
 * init() 初始化
 * |
 * preHandler() 前期准备
 * |
 * parse() 解析Excel
 * |
 * transformBean() 转换字符串为对应Bean
 * |
 * validate() 校验转换后Bean
 * |
 * process() 后处理
 * |
 * destroy() 资源销毁
 * 
 * @author c0ying
 *
 * @param <T>
 */
public abstract class SimpleExcelParser<T> implements ImportProcesser<T>{
	
	private Logger logger = LoggerFactory.getLogger(SimpleExcelParser.class);

	private String clz;
	private ThreadLocal<Map<String,Object>> context = new ThreadLocal<>();//运行上下文
	private List<String> defaultMappings;//默认映射字段
	private Map<String, CustomTransformBean> customTransformBeanMap = new HashMap<>(0);

	public List<String> getDefaultMappings() {
		return defaultMappings;
	}

	public SimpleExcelParser(List<String> mappings) {
		Assert.notNull(mappings, "mapping can not be null");
		Type type = this.getClass().getGenericSuperclass();
		if (type instanceof ParameterizedType){
			this.clz = (((ParameterizedType) type).getActualTypeArguments()[0]).getTypeName();
		}else{
			throw new IllegalArgumentException("simpleExcelParser must has one parameterizedType");
		}
		this.defaultMappings = mappings;
		this.context.set(new HashMap<>());
	}

	/**
	 * 设置临时字段映射
	 */
	public void setFieldsMapping(List<String> mappings) {
		Assert.notNull(mappings, "mapping can not be null");
		this.getContext().put("fieldsMapping", mappings);
	}
	
	private List<T> transformBean(List<List<String>> datas) throws Exception {
		//
		Assert.notNull(datas,"excel data list can not be null");
		//
		List<T> reDatas = new ArrayList<>(datas.size());
		List<String> fieldsMappings = (List<String>) this.getContext().get("fieldsMapping");
		if (fieldsMappings == null) fieldsMappings = defaultMappings;

		Class<T> obj = (Class<T>) Class.forName(clz);
		for (List<String> list : datas) {
			T instance = (T) obj.newInstance();
			BeanMap beanMap = BeanMap.create(instance);
			if (list.size() <= 0 || fieldsMappings.size() <= 0) {
				continue;
			}

			for (int i = 0; i < fieldsMappings.size(); i++) {
				if (list.size() <= i) {
					break;
				}
				String value = list.get(i);
				String key = fieldsMappings.get(i);
				if (customTransformBeanMap.containsKey(key)) {
					customTransformBeanMap.get(key).customTransform(instance, value, i, fieldsMappings, list);
				}else {
					beanMap.put(key, value);
				}
			}

			reDatas.add(instance);
		}
		return reDatas;
	}

	public List<T> parse(List<List<String>> datas) throws Exception{
		List<T> mT = null;
		try {
			logger.info("preHandler transforming Excel data");
			preHandler(datas, getContext());
			logger.info("start transforming Excel data to Bean list");
			mT = transformBean(datas);
			logger.info("process after transforming");
		} catch (Exception e) {
			logger.info("transform error ");
			handleException(e);
		}
		return mT;
	}

	public void setContext(Map<String, Object> context) {
		this.context.set(context);
		init(context);
	}
	
	public Map<String, Object> getContext() {
		return this.context.get();
	};
	
	protected void init(Map<String, Object> context) {
	}

	public void destroy(){
		context.remove();
	}

	public void registerCustomTransform(String key, CustomTransformBean customTransformBean){
		customTransformBeanMap.put(key, customTransformBean);
	}

}
