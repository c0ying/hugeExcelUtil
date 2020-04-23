package com.c0ying.framework;

import com.c0ying.framework.exceldata.delegate.ExcelFileReadDelegate;
import com.c0ying.framework.mock.TestReadDataParser;
import org.junit.Test;

public class ReadExcelTest {

    @Test
    public void readExcelTest(){
        ExcelFileReadDelegate excelFileReadDelegate = new ExcelFileReadDelegate();
        try {
            excelFileReadDelegate.readExcel(getResource("file/10.xlsx"), new TestReadDataParser());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void saxReadExcelTest(){
        ExcelFileReadDelegate excelFileReadDelegate = new ExcelFileReadDelegate();
        try {
            TestReadDataParser testReadDataParser = new TestReadDataParser();
            testReadDataParser.getContext().put("runType", "sax");
            excelFileReadDelegate.readExcel(getResource("file/10.xlsx"), testReadDataParser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String  getResource(String fileUrl){
        return Thread.currentThread().getContextClassLoader().getResource("file/10.xlsx").getFile();
    }
}
