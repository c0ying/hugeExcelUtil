package com.c0ying.framework.exceldata.imparse;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.c0ying.framework.exceldata.DealStatusDefaultMonitor;
import com.c0ying.framework.exceldata.DealStatusMonitor;
import com.c0ying.framework.exceldata.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * EasyExcel 解析事件器与Parser 业务逻辑器结合适配器
 * @param <T>
 */
public class StreamParseDataListener<T> extends AnalysisEventListener<Map<Integer, String>> {

    private List<List<String>> list = new ArrayList<>();

    protected SimpleExcelParser simpleExcelParser;
    protected DealStatusMonitor dealStatusMonitor;
    private boolean skipHead = true;

    public StreamParseDataListener(SimpleExcelParser<T> simpleExcelParser, boolean skipHead){
        this.simpleExcelParser = simpleExcelParser;
        this.skipHead = (boolean) simpleExcelParser.getContext().getOrDefault(Constants.PARSE_SKIP_HEAD,true);
        this.dealStatusMonitor = new DealStatusDefaultMonitor();
    }

    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        if (list.size() <= (int) simpleExcelParser.getContext().get(Constants.PARSE_BATCH_COUNT)) {
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
        } finally {
            simpleExcelParser.destroy();
        }
        list.clear();
    }

}
