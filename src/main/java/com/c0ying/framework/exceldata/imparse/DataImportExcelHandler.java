package com.c0ying.framework.exceldata.imparse;

import com.c0ying.framework.exceldata.delegate.ExcelFileReadDelegate;
import com.c0ying.framework.exceldata.imparse.bean.ParseExcelException;

import java.util.List;

public class DataImportExcelHandler<T> extends AbstractImportExcelHandler<T>{

	private static ExcelFileReadDelegate excelFileReadDelegate = new ExcelFileReadDelegate();

	@Override
	public void handler(String fileUrl, SimpleExcelParser<T> parser) throws Exception {
		try {
			Object runType = context.getOrDefault("runType", "normal");
			if (runType.equals("normal")){
				List<List<String>> contentStrList = excelFileReadDelegate.readExcel(fileUrl, true);
				if (contentStrList == null) {
					throw new ParseExcelException("导入文件解析异常");
				}
				parser.setContext(context);
				if (context != null && context.containsKey("fieldsMapping")) {
					parser.setFieldsMapping((List<String>) context.get("fieldsMapping"));
				}
				processInternal(contentStrList);
			}else{
				excelFileReadDelegate.readExcel(fileUrl, parser);
			}

		} catch (Exception e) {
			throw new ParseExcelException(e.getMessage(), e);
		}finally {
			parser.destroy();
		}
	}

}
