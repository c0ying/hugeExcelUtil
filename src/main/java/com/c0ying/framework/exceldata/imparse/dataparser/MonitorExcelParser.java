package com.c0ying.framework.exceldata.imparse.dataparser;

import com.c0ying.framework.exceldata.imparse.ParseExcelException;
import com.c0ying.framework.exceldata.monitor.DealStatusDefaultMonitor;
import com.c0ying.framework.exceldata.monitor.DealStatusMonitor;

import java.util.List;
import java.util.Map;

public class MonitorExcelParser<T> extends SimpleExcelParser<T> {

    private String taskId;
    private SimpleExcelParser<T> simpleExcelParser;

    public MonitorExcelParser(String taskId, SimpleExcelParser<T> simpleExcelParser) {
        this(taskId, simpleExcelParser, new DealStatusDefaultMonitor());
    }

    public MonitorExcelParser(String taskId, SimpleExcelParser<T> simpleExcelParser, DealStatusMonitor dealStatusMonitor) {
        super(simpleExcelParser.getDefaultMappings());
        this.taskId = taskId;
        this.dealStatusMonitor = dealStatusMonitor;
        this.simpleExcelParser = simpleExcelParser;
    }

    public List<T> process(List<T> mT) throws ParseExcelException {
        List<T> result = simpleExcelParser.process(mT);
        if (taskId != null){
            String exportStatus = dealStatusMonitor.getExportStatus(taskId);
            dealStatusMonitor.setExportStatus(taskId, String.valueOf(Integer.parseInt(exportStatus)+mT.size()));
        }
        return result;
    }

    @Override
    public void setFieldsMapping(List<String> mappings) {
        simpleExcelParser.setFieldsMapping(mappings);
    }

    @Override
    public List<T> parse(List<List<String>> datas) throws Exception{
        return simpleExcelParser.parse(datas);
    }

    @Override
    public void setContext(Map<String, Object> context) {
        simpleExcelParser.setContext(context);
    }

    @Override
    public Map<String, Object> getContext() {
        return simpleExcelParser.getContext();
    }

    @Override
    public void destroy() {
        simpleExcelParser.destroy();
    }

    @Override
    public void registerCustomTransform(String key, CustomTransformBean customTransformBean) {
        simpleExcelParser.registerCustomTransform(key, customTransformBean);
    }

    @Override
    public void preHandler(List<List<String>> datas, Map<String, Object> context) {
        simpleExcelParser.preHandler(datas, context);
    }

    @Override
    public void validate(List<T> mT) throws ParseExcelException {
        simpleExcelParser.validate(mT);
    }

    @Override
    public void handleException(Exception e) {
        simpleExcelParser.handleException(e);
    }

    private DealStatusMonitor dealStatusMonitor;
}
