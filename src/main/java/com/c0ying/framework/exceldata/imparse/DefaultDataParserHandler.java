package com.c0ying.framework.exceldata.imparse;

import com.c0ying.framework.exceldata.delegate.ExcelFileReadDelegater;
import com.c0ying.framework.exceldata.delegate.ExcelFileReadDelegaterFactory;
import com.c0ying.framework.exceldata.imparse.bean.ImparseExcelDataProperties;
import com.c0ying.framework.exceldata.imparse.dataparser.MonitorExcelParser;
import com.c0ying.framework.exceldata.imparse.dataparser.SimpleExcelParser;
import com.c0ying.framework.exceldata.monitor.DealStatusDefaultMonitor;
import com.c0ying.framework.exceldata.monitor.DealStatusMonitor;
import com.c0ying.framework.exceldata.utils.Constants;
import com.c0ying.framework.exceldata.utils.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;

public class DefaultDataParserHandler implements DataParserExcelHandler {

    protected Logger logger  = LoggerFactory.getLogger(DefaultDataParserHandler.class);

    public DefaultDataParserHandler(){
        this(new ImparseExcelDataProperties(), new DealStatusDefaultMonitor());
    }

    public DefaultDataParserHandler(ImparseExcelDataProperties properties, DealStatusMonitor dealStatusMonitor){
        this.dealStatusMonitor = dealStatusMonitor;
        this.properties = properties;
    }

    public DefaultDataParserHandler(ImparseExcelDataProperties properties){
        this(properties, new DealStatusDefaultMonitor());
    }

    @Override
    public void readExcel(String taskId, String fileUrl, SimpleExcelParser<?> simpleExcelParser, Map<String, Object> taskContext) {
        MonitorExcelParser<?> monitorExcelParser = new MonitorExcelParser<>(taskId, simpleExcelParser, dealStatusMonitor);
        monitorExcelParser.setContext(taskContext);
        Object runType = monitorExcelParser.getContext().getOrDefault(Constants.RUN_TYPE, Constants.RUN_TYPE_NORMAL);
        logger.info("taskId:{} parse excel in mode {}", taskId, runType);
        ExcelFileReadDelegater excelFileReadDelegater = ExcelFileReadDelegaterFactory.build((String) runType);
        try {
            excelFileReadDelegater.readExcel(fileUrl, monitorExcelParser);
            dealStatusMonitor.markFinished(taskId);
        } catch (Exception e) {
            dealStatusMonitor.setExportStatus(taskId, "-100");
            dealStatusMonitor.setExportErrorMsg(taskId,e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void readExcel(String taskId, String fileUrl, SimpleExcelParser<?> simpleExcelParser) {
        readExcel(taskId, fileUrl, simpleExcelParser, Collections.emptyMap());
    }

    @Override
    public void readExcel(String fileUrl, SimpleExcelParser<?> simpleExcelParser, Map<String, Object> taskContext) {
        readExcel(TokenUtil.getToken(), fileUrl, simpleExcelParser, taskContext);
    }

    @Override
    public void readExcel(String fileUrl, SimpleExcelParser<?> simpleExcelParser) {
        readExcel(TokenUtil.getToken(), fileUrl, simpleExcelParser, Collections.emptyMap());
    }


    @Override
    public DealStatus getDealStatus(String taskId) {
        DealStatus dealStatus = new DealStatus();
        dealStatus.status = dealStatusMonitor.getExportStatus(taskId);
        dealStatus.finished = dealStatusMonitor.isFinished(taskId);
        dealStatus.exception = dealStatusMonitor.isException(taskId);
        dealStatus.errorMsg = dealStatusMonitor.getErrorMsg(taskId);
        return dealStatus;
    }

    protected ImparseExcelDataProperties properties;
    protected DealStatusMonitor dealStatusMonitor;

    public class DealStatus{
        String status;
        boolean finished;
        boolean exception;
        String errorMsg;

        public String getStatus() {
            return status;
        }

        public boolean isFinished() {
            return finished;
        }

        public boolean isException() {
            return exception;
        }

        public String getErrorMsg() {
            return errorMsg;
        }
    }

}
