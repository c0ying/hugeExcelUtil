package com.c0ying.framework;

import com.c0ying.framework.exceldata.export.DefaultDataExportExcelHandler;
import com.c0ying.framework.mock.TestWriteDataProducer;
import org.junit.Test;

public class WriteExcelTest {

    @Test
    public void testWriteExcel(){

        DefaultDataExportExcelHandler exportExcelHandler = new DefaultDataExportExcelHandler();
        exportExcelHandler.handler("10", new TestWriteDataProducer());
    }


}
