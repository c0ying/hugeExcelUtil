package com.c0ying.framework;

import com.c0ying.framework.exceldata.export.AsyncDataExportExcelHandler;
import com.c0ying.framework.exceldata.export.DefaultDataExportExcelHandler;
import com.c0ying.framework.exceldata.export.bean.ExportExcelDataProperties;
import com.c0ying.framework.exceldata.utils.Constants;
import com.c0ying.framework.mock.TestWriteDataProducer;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class WriteExcelTest {

    @Test
    public void testWriteExcel() throws InterruptedException {

        ExportExcelDataProperties properties = new ExportExcelDataProperties();
        properties.setTmpFilePath("D:\\");
        AsyncDataExportExcelHandler exportExcelHandler = new AsyncDataExportExcelHandler(properties);
        Map<String, Object> params = new HashMap<>();
        params.put(Constants.RUN_TYPE, Constants.RUN_TYPE_SAX);
        String taskId = exportExcelHandler.startTask(new TestWriteDataProducer(), params);
        String exportStatus = exportExcelHandler.getExportStatus(taskId);
        while (!exportStatus.equals("-100") && Float.parseFloat(exportStatus) < 1){
            System.out.printf("process-status:%s \n", exportStatus);
            Thread.sleep(1000L);
            exportStatus = exportExcelHandler.getExportStatus(taskId);
        }
        System.out.printf("process-status:%s \n", exportStatus);
    }


}
