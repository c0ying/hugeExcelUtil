package com.c0ying.framework.exceldata.imparse;

import com.c0ying.framework.exceldata.delegate.ExcelFileReadDelegate;
import com.c0ying.framework.exceldata.imparse.bean.ParseExcelException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaxDataImportExcelHandler<T> extends AbstractImportExcelHandler<T>{

	private Map<String, Object> context = new HashMap<>();

	private ExcelFileReadDelegate excelFileReadDelegate;

	public SaxDataImportExcelHandler(SimpleExcelParser<T> parser, String fileUrl) {
		super(parser, fileUrl);
		this.excelFileReadDelegate = new ExcelFileReadDelegate();
	}

	@Override
	public List<T> handler() throws Exception {
		try {
			parser.setContext(context);
			if (context != null && context.containsKey("fieldsMapping")) {
				parser.setFieldsMapping((List<String>) context.get("fieldsMapping"));
			}
			excelFileReadDelegate.saxReadExcel(fileUrl, parser);
		} catch (Exception e) {
			throw new ParseExcelException(e.getMessage(), e);
		}finally {
			parser.destroy();
		}
		return null;
	}

}
