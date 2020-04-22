package com.c0ying.framework.exceldata.imparse;

import com.c0ying.framework.exceldata.delegate.ExcelFileReadDelegate;
import com.c0ying.framework.exceldata.imparse.bean.ParseExcelException;

import java.util.List;
import java.util.concurrent.Callable;

public class DataImportExcelHandler<T> extends AbstractImportExcelHandler<T> implements Callable<List<T>>{

	private ExcelFileReadDelegate excelFileReadDelegate;

	public DataImportExcelHandler(SimpleExcelParser<T> parser, String fileUrl) {
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
			List<List<String>> contentStrList = parse(this.fileUrl);
			if (contentStrList == null) {
				throw new ParseExcelException("导入文件解析异常");
			}
			return processInternal(contentStrList);
		} catch (Exception e) {
			throw new ParseExcelException(e.getMessage(), e);
		}finally {
			parser.destroy();
		}
	}

	public List<List<String>> parse(String fileUrl) {
		return excelFileReadDelegate.readExcel(fileUrl, true);
	}

	@Override
	public List<T> call() throws Exception {
		return handler();
	}

}
