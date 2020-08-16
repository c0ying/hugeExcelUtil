package com.c0ying.framework.exceldata.export;

import com.c0ying.framework.exceldata.DealStatusMonitor;
import com.c0ying.framework.exceldata.export.bean.ExportExcelDataProperties;
import com.c0ying.framework.exceldata.export.dataproducer.SimpleDataAsyncProducer;
import com.c0ying.framework.exceldata.utils.TokenUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncDataExportExcelHandler extends DefaultDataExportExcelHandler {

    public AsyncDataExportExcelHandler() {
        super();
        this.taskExecutor = Executors.newCachedThreadPool();
    }

    public AsyncDataExportExcelHandler(ExportExcelDataProperties properties, DealStatusMonitor dealStatusMonitor) {
        super(properties, dealStatusMonitor);
        this.taskExecutor = Executors.newCachedThreadPool();
    }

    public AsyncDataExportExcelHandler(ExportExcelDataProperties properties) {
        super(properties);
        this.taskExecutor = Executors.newCachedThreadPool();
    }

    public AsyncDataExportExcelHandler(ExportExcelDataProperties properties, DealStatusMonitor dealStatusMonitor,ExecutorService taskExecutor) {
        super(properties, dealStatusMonitor);
        this.taskExecutor = taskExecutor;
    }

    /**
     * 启动导出任务；
     * @param data
     * @return
     */
    public String startTask(SimpleDataAsyncProducer<?> data, Map<String, Object> params) {
        ExportExcelTask task = new ExportExcelTask();
        task.handler = this;
        task.taskId = TokenUtil.getToken();
        task.dataProducer = data;
        task.taskContext.put("producer_context", params);
        logger.debug("taskId:{} task thread start...", task.taskId);
        taskExecutor.submit(new Thread(task));
        return task.taskId;
    }

    class ExportExcelTask implements Runnable{
        DefaultDataExportExcelHandler handler;
        SimpleDataAsyncProducer<?> dataProducer;
        String taskId;
        Map<String, Object> taskContext = new HashMap<>();

        public void run() {
            try {
                handler(taskId, dataProducer, (Map<String, Object>) taskContext.get("producer_context"));
            } catch (Exception e) {
                exportStatusMonitor.setExportStatus(taskId, "-100");
                e.printStackTrace();
                throw e;
            }
        }
    }

    private ExecutorService taskExecutor;
}
