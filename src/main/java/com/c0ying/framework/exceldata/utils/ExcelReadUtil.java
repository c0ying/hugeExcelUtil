package com.c0ying.framework.exceldata.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 读取Excel工具类，只支持5MB以下文件
 * 由于没有做优化，处理过大Excel会导致OOM
 * 暂时使用限制文件大小方式
 * @author Cyh
 *
 */
public class ExcelReadUtil {
	
	private static Logger logger = LoggerFactory.getLogger(ExcelReadUtil.class);
	
	private static final int MB5_BYTE = 5*1024*1024;
	
    public static List<List<String>> readExcel(File file, boolean skipHead) {
    	Assert.notNull(file,"excel file can not be null");
    	List<List<String>> rowDatas =new ArrayList<List<String>>(0);  
        if (!file.isFile() || file.length() > MB5_BYTE) {
        	throw new RuntimeException("sorry, unsupport handle excel greater than 5MB");
		}
        logger.info("parsing excel file:{}", file.getName());
        Workbook workBook = null;
        try {
        	String fileName=file.getName();
        	String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
            if("xlsx".equalsIgnoreCase(ext)) {
            	workBook = new XSSFWorkbook(new FileInputStream(file));
            }else {
            	workBook = new HSSFWorkbook(new FileInputStream(file)); 
            }
              
            Sheet sheet = workBook.getSheetAt(0);  
            if (sheet == null) {  
                throw new RuntimeException("first sheet is not exist, is this possible?");
            }
            int lastRowNum = sheet.getLastRowNum();
            int start = 0;
            short lastCellNum = -1;
            if (skipHead) {//如果有表头 请按照表头的最大列数计算
                lastCellNum = sheet.getRow(start).getLastCellNum();//抛异常
                start += 1;
            }
            for (int rowNum = start; rowNum <= lastRowNum; rowNum++) {
                Row row = sheet.getRow(rowNum);  
                if (row == null) continue;   
                // 循环列Cell
                ArrayList<String> arrCell = new ArrayList<String>();
                for (int cellNum = 0; cellNum <= ( lastCellNum >= 0 ? lastCellNum : row.getLastCellNum() )  ; cellNum++) {
                    Cell cell = row.getCell(cellNum);
                    //如果是为null 添加为""
                    arrCell.add(getValue(cell));
                }
                rowDatas.add(arrCell);  
            }
            logger.info("complete parsing excel file:{}", file.getName());
        } catch (IOException e) {
        	logger.error("parse excel occur error, exception:{}", e);
        } finally {
        	if (workBook != null) {
				try {
					workBook.close();
				} catch (IOException e) {
					logger.error("closing excel occur error, exception:{}", e);
				}
			}
        }
        return rowDatas;  
    }
    
    private static String getValue(Cell cell) {
        if( cell == null ){
            return "";
        }
        if (cell.getCellTypeEnum().equals(CellType.BOOLEAN)) {  
        	return String.valueOf(cell.getBooleanCellValue());  
	    } else if (cell.getCellTypeEnum().equals(CellType.NUMERIC)) { 
	    	DataFormatter dataFormatter = new DataFormatter();
	    	dataFormatter.setDefaultNumberFormat(new DecimalFormat("#.###############"));
	        return dataFormatter.formatCellValue(cell);  
	    } else {
	    	try {
				RichTextString rStr = cell.getRichStringCellValue();
				String str = rStr.getString();
				return str;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
	    }  
    }  
}