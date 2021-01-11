package com.c0ying.framework.exceldata.export;

import com.c0ying.framework.exceldata.monitor.DealStatusMonitor;
import com.c0ying.framework.exceldata.export.bean.ExportExcelDataProperties;

public class SyncDataExportExcelHandler extends DefaultDataExportExcelHandler {

    public SyncDataExportExcelHandler() {
        super();
    }

    public SyncDataExportExcelHandler(ExportExcelDataProperties properties) {
        super(properties);
    }

    public SyncDataExportExcelHandler(ExportExcelDataProperties properties, DealStatusMonitor dealStatusMonitor) {
        super(properties, dealStatusMonitor);
    }
}
