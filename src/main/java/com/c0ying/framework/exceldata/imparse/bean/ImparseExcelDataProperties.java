package com.c0ying.framework.exceldata.imparse.bean;

public class ImparseExcelDataProperties {

    private int runTask = 10;
    private int queueTask = 5;

    public int getRunTask() {
        return runTask;
    }

    public void setRunTask(int runTask) {
        this.runTask = runTask;
    }

    public int getQueueTask() {
        return queueTask;
    }

    public void setQueueTask(int queueTask) {
        this.queueTask = queueTask;
    }
}
