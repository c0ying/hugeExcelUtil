package com.c0ying.framework.exceldata.imparse;

import com.c0ying.framework.exceldata.imparse.bean.ImparseExcelDataProperties;
import com.c0ying.framework.exceldata.imparse.dataparser.SimpleExcelParser;
import com.c0ying.framework.exceldata.monitor.DealStatusDefaultMonitor;
import com.c0ying.framework.exceldata.monitor.DealStatusMonitor;
import com.c0ying.framework.exceldata.utils.TokenUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AsyncDataParserExcelHandler extends DefaultDataParserHandler{

    public AsyncDataParserExcelHandler() {
        this(new ImparseExcelDataProperties());
    }

    public AsyncDataParserExcelHandler(ImparseExcelDataProperties properties, DealStatusMonitor dealStatusMonitor) {
        super(properties, dealStatusMonitor);
        this.taskExecutor = new ThreadPoolExecutor(properties.getRunTask(), properties.getRunTask()+properties.getQueueTask(),
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                r -> {
                    Thread thread = new Thread(r);
                    thread.setName("async-data-parser");
                    return thread;
                });
    }

    public AsyncDataParserExcelHandler(ImparseExcelDataProperties properties) {
        this(properties, new DealStatusDefaultMonitor());
    }

    public String startTask(String fileUrl, SimpleExcelParser<?> simpleExcelParser, Map<String, Object> params) {
        ExcelParserTask task = new ExcelParserTask();
        task.handler = this;
        task.simpleExcelParser = simpleExcelParser;
        task.fileUrl = fileUrl;
        task.taskId = TokenUtil.getToken();
        task.taskContext.put("task_params", params);
        logger.debug("taskId:{} async imparse task thread start...", task.taskId);
        taskExecutor.submit(task);
        return task.taskId;
    }

    class ExcelParserTask implements Runnable{
        DefaultDataParserHandler handler;
        SimpleExcelParser<?> simpleExcelParser;
        String fileUrl;
        String taskId;
        Map<String, Object> taskContext = new HashMap<>();

        @Override
        public void run() {
            try {
                handler.readExcel(taskId, fileUrl, simpleExcelParser, (Map<String, Object>) taskContext.get("task_params"));
            } catch (Exception e) {
                dealStatusMonitor.setExportStatus(taskId, "-100");
                dealStatusMonitor.setExportErrorMsg(taskId,e.getMessage());
                e.printStackTrace();
            }

        }
    }

    private ExecutorService taskExecutor;
}
