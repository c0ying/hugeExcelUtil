package com.c0ying.framework.exceldata.export;

import com.c0ying.framework.exceldata.DealStatusDefaultMonitor;
import com.c0ying.framework.exceldata.DealStatusMonitor;
import com.c0ying.framework.exceldata.delegate.ExcelFileWriteDelegater;
import com.c0ying.framework.exceldata.delegate.POIExcelFileWriteDelegate;
import com.c0ying.framework.exceldata.delegate.SaxExcelFileWriteDelegate;
import com.c0ying.framework.exceldata.export.bean.ExportExcelDataProperties;
import com.c0ying.framework.exceldata.export.dataproducer.SimpleDataAsyncProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 
 * 导出任务流程主控制
 * @author Cyh
 *
 */
public class DefaultDataExportExcelHandler implements DataExportExcelHandler{

	protected Logger logger  = LoggerFactory.getLogger(DefaultDataExportExcelHandler.class);

    public DefaultDataExportExcelHandler(){
        this.properties = new ExportExcelDataProperties();
        this.exportStatusMonitor = new DealStatusDefaultMonitor();
    }

    public DefaultDataExportExcelHandler(ExportExcelDataProperties properties, DealStatusMonitor dealStatusMonitor){
        this.properties = properties;
        this.exportStatusMonitor = dealStatusMonitor;
    }

	public void handler(String taskId, SimpleDataAsyncProducer<?> data) {
		Object runType = data.getContext().getOrDefault("runType", "normal");
		ExcelFileWriteDelegater excelFileWriteDelegater = null;
		if (runType.equals("normal")){
			excelFileWriteDelegater = new POIExcelFileWriteDelegate();
		}else{
			excelFileWriteDelegater = new SaxExcelFileWriteDelegate();
		}
		try {
			logger.info("taskId:{} start to handle export", taskId);
			exportStatusMonitor.setExportStatus(taskId, "0");
			boolean noDataFlag = true;
			boolean init = false;
			while (data.hasMore()) {
				if (!init){
					if (noDataFlag) noDataFlag = false;
					excelFileWriteDelegater.init(new File(properties.getTmpFilePath(), taskId.concat(".xlsx")).getAbsolutePath(), data.getHeadTitle(), data.delimiterFlag());
					init = true;
				}
				excelFileWriteDelegater.writeData(data.stringData(), data.delimiterFlag());
				exportStatusMonitor.setExportStatus(taskId, String.format("%.2f", ((float)data.getCurrent())/data.getTotal()));
			}
			if (!noDataFlag) {
				excelFileWriteDelegater.finish();
			}else{
				exportStatusMonitor.setExportStatus(taskId, "-100");
			}
		} catch (Exception e) {
			logger.error("taskId:{} export handle occur error! exception:{}", taskId, e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public String getExportStatus(String taskId) {
		return exportStatusMonitor.getExportStatus(taskId);
	}
	

	protected ExportExcelDataProperties properties;
	protected DealStatusMonitor exportStatusMonitor;

}
