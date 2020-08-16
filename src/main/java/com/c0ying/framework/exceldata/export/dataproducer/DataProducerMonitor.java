package com.c0ying.framework.exceldata.export.dataproducer;

public interface DataProducerMonitor {

    long getTotal();

    void setTotal(long total);

    long getCurrent();

    void setCurrent(long current);
}
