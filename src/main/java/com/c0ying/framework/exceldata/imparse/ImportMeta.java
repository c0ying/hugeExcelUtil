package com.c0ying.framework.exceldata.imparse;

import java.util.List;
import java.util.Map;

public interface ImportMeta {

	void setFieldsMapping(List<String> mappings);
	
	Map<String, Object> getContext();

	void registerCustomTransform(String key, CustomTransformBean customTransformBean);
}
