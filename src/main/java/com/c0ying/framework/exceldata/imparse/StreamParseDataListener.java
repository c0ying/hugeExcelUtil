package com.c0ying.framework.exceldata.imparse;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StreamParseDataListener<T> extends AnalysisEventListener<Map<Integer, String>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StreamParseDataListener.class);

    private static final int BATCH_COUNT = 1000;
    private List<List<String>> list = new ArrayList<>();

    protected SimpleExcelParser simpleExcelParser;
    private boolean skipHead = true;

    public StreamParseDataListener(SimpleExcelParser<T> simpleExcelParser, boolean skipHead){
        this.simpleExcelParser = simpleExcelParser;
        this.skipHead = skipHead;
    }

    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        if (list.size() <= BATCH_COUNT) {
            if (skipHead){
                if(context.readRowHolder().getRowIndex() == 0){
                    return;
                }
            }
            list.add(data.values().stream().collect(Collectors.toList()));
        }else{
            try {
                List<T> mT = simpleExcelParser.parse(list);
                simpleExcelParser.validate(mT);
                simpleExcelParser.process(mT);
            } catch (Exception e) {
                simpleExcelParser.handleException(e);
            }
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        try {
            List<T> mT = simpleExcelParser.parse(list);
            simpleExcelParser.validate(mT);
            simpleExcelParser.process(mT);
        } catch (Exception e) {
            simpleExcelParser.handleException(e);
        }
        list.clear();
    }

}
