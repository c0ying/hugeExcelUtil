package com.c0ying.framework;

import com.c0ying.framework.exceldata.imparse.DataImportExcelHandler;
import com.c0ying.framework.exceldata.imparse.SaxDataImportExcelHandler;
import com.c0ying.framework.mock.Data;
import com.c0ying.framework.mock.TestReadDataParser;
import org.junit.Test;

public class ReadExcelTest {

    @Test
    public void readExcelTest(){
        DataImportExcelHandler<Data> importExcelHandler = new DataImportExcelHandler<Data>(new TestReadDataParser(), "E:\\1.xlsx");
        try {
            importExcelHandler.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void saxReadExcelTest(){
        SaxDataImportExcelHandler<Data> importExcelHandler = new SaxDataImportExcelHandler<>(new TestReadDataParser(),"E:\\1.xlsx");
        try {
            importExcelHandler.handler();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
