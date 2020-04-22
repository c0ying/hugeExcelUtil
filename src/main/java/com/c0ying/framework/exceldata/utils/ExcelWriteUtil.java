package com.c0ying.framework.exceldata.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class ExcelWriteUtil {

	
	/**
	 * 初始化EXCEL头信息
	 * @return
	 */
	private static String[] initTitle(String headTitle, String delimiter){
		String allTitile = headTitle;
		if (StringUtils.isNotBlank(allTitile)){
			return allTitile.split(delimiter);
		}else{
			return new String[0];
		}
	}
	
	/**
	 * 初始化EXCEL生成类
	 * @return
	 */
	public static SXSSFWorkbook initExcel(String[] headTitle){
		SXSSFWorkbook wb = new SXSSFWorkbook(1000);//使用POI中的流式生成EXCEL，可以提高大数据的EXCEL的生成效率和防止过多的数据保存内存导致内存溢出
		Sheet sh = wb.createSheet("sheet1");
		Row row = sh.createRow(0);
		for (int i = 0; i < headTitle.length; i++) {
			Cell cell = row.createCell(i);
			cell.setCellType(CellType.STRING);//文本格
			cell.setCellValue(headTitle[i]);
			cell.setCellStyle(styleExcel(wb));
			sh.setColumnWidth(i, 5000);//设置单元格宽度
		}
		return wb;
	}

	public static SXSSFWorkbook initExcel(String headTitle){
		return initExcel(headTitle, ",");
	}
	
	public static SXSSFWorkbook initExcel(String headTitle, String delimiter){
		String[] title = initTitle(headTitle, delimiter);
		return initExcel(title);
	}
	
    protected static CellStyle styleExcel(SXSSFWorkbook wb){
    	CellStyle cs=wb.createCellStyle();
    	cs.setAlignment(HorizontalAlignment.CENTER);
        cs.setWrapText(true);//自动换行
        //设置单元格字体
    	Font font=wb.createFont();
    	font.setFontName("宋体");
    	font.setFontHeight((short)250);
    	cs.setFont(font);
    	
    	return cs;
    }
	
    public static <T> SXSSFWorkbook exportData(SXSSFWorkbook wb, String[] dataStrings){
		return exportData(wb, dataStrings, ",");
    }
    public static <T> SXSSFWorkbook exportData(SXSSFWorkbook wb, String[] dataStrings, String delimiter){
    	if (wb == null) return null;
    	
    	if (dataStrings != null) {
    		for (String dataString : dataStrings) {
    			if(StringUtils.isNotBlank(dataString)) {
    				Sheet sh = wb.getSheet("sheet1");
    				int z = sh.getLastRowNum()+1;
    				String[] data = dataString.split(delimiter);
    				Row row = sh.createRow(z);
    				for (int j = 0; j < data.length; j++) {
    					Cell cell = row.createCell(j);
    					cell.setCellValue(data[j]);
    				}
    			}
    		}
    		return wb;
    	}
    	return null;
    }
	
}
