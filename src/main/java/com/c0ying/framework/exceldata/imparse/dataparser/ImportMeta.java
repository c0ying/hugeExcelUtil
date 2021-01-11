package com.c0ying.framework.exceldata.imparse.dataparser;

import java.util.List;

public interface ImportMeta {

	void setFieldsMapping(List<String> mappings);
	
	void registerCustomTransform(String key, CustomTransformBean customTransformBean);
}
