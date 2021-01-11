package com.c0ying.framework.exceldata.export;

import com.c0ying.framework.exceldata.export.bean.ExportExcelDataProperties;
import com.c0ying.framework.exceldata.export.dataproducer.SimpleDataProducer;
import com.c0ying.framework.exceldata.monitor.DealStatusDefaultMonitor;
import com.c0ying.framework.exceldata.monitor.DealStatusMonitor;
import com.c0ying.framework.exceldata.utils.TokenUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AsyncDataExportExcelHandler extends DefaultDataExportExcelHandler {

    public AsyncDataExportExcelHandler() {
        this(new ExportExcelDataProperties());
    }

    public AsyncDataExportExcelHandler(ExportExcelDataProperties properties, DealStatusMonitor dealStatusMonitor) {
        this(properties, dealStatusMonitor,
                new ThreadPoolExecutor(properties.getRunTask(), properties.getRunTask()+properties.getQueueTask(),
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                r -> {
                    Thread thread = new Thread(r);
                    thread.setName("async-data-exporter");
                    return thread;
                }));
    }

    public AsyncDataExportExcelHandler(ExportExcelDataProperties properties) {
        this(properties, new DealStatusDefaultMonitor(),
                new ThreadPoolExecutor(properties.getRunTask(), properties.getRunTask()+properties.getQueueTask(),
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                r -> {
                    Thread thread = new Thread(r);
                    thread.setName("async-data-exporter");
                    return thread;
                }));
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
    public String startTask(SimpleDataProducer<?> data, Map<String, Object> params) {
        ExportExcelTask task = new ExportExcelTask();
        task.handler = this;
        task.taskId = TokenUtil.getToken();
        task.dataProducer = data;
        task.taskContext.put("task_params", params);
        logger.debug("taskId:{} async export task thread start...", task.taskId);
        taskExecutor.submit(new Thread(task));
        return task.taskId;
    }

    class ExportExcelTask implements Runnable{
        DefaultDataExportExcelHandler handler;
        SimpleDataProducer<?> dataProducer;
        String taskId;
        Map<String, Object> taskContext = new HashMap<>();

        public void run() {
            try {
                handler(taskId, dataProducer, (Map<String, Object>) taskContext.get("task_params"));
            } catch (Exception e) {
                exportStatusMonitor.setExportStatus(taskId, "-100");
                exportStatusMonitor.setExportErrorMsg(taskId, e.getMessage());
                e.printStackTrace();
                throw e;
            }
        }
    }

    private ExecutorService taskExecutor;
}
