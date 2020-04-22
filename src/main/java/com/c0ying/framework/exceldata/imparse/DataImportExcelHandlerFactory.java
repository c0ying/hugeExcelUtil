package com.c0ying.framework.exceldata.imparse;

public class DataImportExcelHandlerFactory {

	public static void build(String runType, SimpleExcelParser simpleExcelParser, String fileUrl) throws Exception {
		if ("normal".equalsIgnoreCase(runType)){
			DataImportExcelHandler dataImportExcelHandler = new DataImportExcelHandler<>(simpleExcelParser, fileUrl);
			dataImportExcelHandler.handler();
		}else if ("sax".equalsIgnoreCase(runType)){
			SaxDataImportExcelHandler dataImportExcelHandler = new SaxDataImportExcelHandler(simpleExcelParser, fileUrl);
			dataImportExcelHandler.handler();
		}else{

		}
	}
}
