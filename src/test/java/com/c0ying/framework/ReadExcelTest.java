package com.c0ying.framework;

import com.c0ying.framework.exceldata.imparse.DataImportExcelHandler;
import com.c0ying.framework.exceldata.imparse.SaxDataImportExcelHandler;
import com.c0ying.framework.mock.Data;
import com.c0ying.framework.mock.TestReadDataParser;
import org.junit.Test;

public class ReadExcelTest {

    @Test
    public void readExcelTest(){
        DataImportExcelHandler<Data> importExcelHandler = new DataImportExcelHandler<Data>(new TestReadDataParser(), "file/10.xlsx");
        try {
            importExcelHandler.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void saxReadExcelTest(){
        SaxDataImportExcelHandler<Data> importExcelHandler = new SaxDataImportExcelHandler<>(new TestReadDataParser(),getResource("file/10.xlsx"));
        try {
            importExcelHandler.handler();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String  getResource(String fileUrl){
        return Thread.currentThread().getContextClassLoader().getResource("file/10.xlsx").getFile();
    }
}
