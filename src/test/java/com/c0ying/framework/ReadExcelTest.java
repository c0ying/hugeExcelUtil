package com.c0ying.framework;

import com.c0ying.framework.exceldata.imparse.AsyncDataParserExcelHandler;
import com.c0ying.framework.exceldata.imparse.DataParserExcelHandler;
import com.c0ying.framework.exceldata.imparse.DefaultDataParserHandler.DealStatus;
import com.c0ying.framework.exceldata.imparse.SyncDataParserExcelHandler;
import com.c0ying.framework.exceldata.utils.Constants;
import com.c0ying.framework.mock.TestReadDataParser;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ReadExcelTest {

    @Test
    public void readExcelTest() {
        DataParserExcelHandler dataParserExcelHandler = new SyncDataParserExcelHandler();
        Map<String,Object> params = new HashMap<>();
        params.put(Constants.PARSE_BATCH_COUNT, Constants.PARSE_BATCH_DEFAULT_COUNT);
        dataParserExcelHandler.readExcel("123", getResource("file/py-ts.xlsx"), new TestReadDataParser(), params);
        System.out.println(dataParserExcelHandler.getDealStatus("123"));
    }

    @Test
    public void saxReadExcelTest(){
        DataParserExcelHandler dataParserExcelHandler = new SyncDataParserExcelHandler();
        Map<String,Object> params = new HashMap<>();
        params.put(Constants.RUN_TYPE, Constants.RUN_TYPE_SAX);
        dataParserExcelHandler.readExcel("123", getResource("file/py-ts.xlsx"), new TestReadDataParser(), params);
        System.out.println(dataParserExcelHandler.getDealStatus("123"));
    }

    @Test
    public void asyncSaxReadExcelTest() throws InterruptedException {
        AsyncDataParserExcelHandler asyncDataParserExcelHandler = new AsyncDataParserExcelHandler();
        Map<String,Object> params = new HashMap<>();
        params.put(Constants.RUN_TYPE, Constants.RUN_TYPE_SAX);
        String taskId = asyncDataParserExcelHandler.startTask(getResource("file/py-ts.xlsx"), new TestReadDataParser(), params);
        DealStatus exportStatus = asyncDataParserExcelHandler.getDealStatus(taskId);
        while (!exportStatus.isFinished() && !exportStatus.isException()){
            System.out.printf("process-status:%s \n", exportStatus.getStatus());
            Thread.sleep(1000L);
            exportStatus = asyncDataParserExcelHandler.getDealStatus(taskId);
        }
        System.out.printf("process-status:%s \n", exportStatus.getStatus());
    }

    private String getResource(String fileUrl){
        return Thread.currentThread().getContextClassLoader().getResource(fileUrl).getFile();
    }
};
