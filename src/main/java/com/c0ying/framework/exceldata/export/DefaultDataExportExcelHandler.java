package com.c0ying.framework.exceldata.export;

import com.c0ying.framework.exceldata.DealStatusDefaultMonitor;
import com.c0ying.framework.exceldata.DealStatusMonitor;
import com.c0ying.framework.exceldata.delegate.ExcelFileWriteDelegate;
import com.c0ying.framework.exceldata.delegate.ExcelFileWriteDelegater;
import com.c0ying.framework.exceldata.delegate.POIExcelFileWriteDelegate;
import com.c0ying.framework.exceldata.delegate.SaxExcelFileWriteDelegate;
import com.c0ying.framework.exceldata.export.bean.ExportExcelDataProperties;
import com.c0ying.framework.exceldata.export.dataproducer.SimpleDataAsyncProducer;
import com.c0ying.framework.exceldata.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;

/**
 * 
 * 导出任务流程主控制
 * @author Cyh
 *
 */
public abstract class DefaultDataExportExcelHandler implements DataExportExcelHandler{

	protected Logger logger  = LoggerFactory.getLogger(DefaultDataExportExcelHandler.class);

    public DefaultDataExportExcelHandler(){
        this.properties = new ExportExcelDataProperties();
        this.exportStatusMonitor = new DealStatusDefaultMonitor();
    }

    public DefaultDataExportExcelHandler(ExportExcelDataProperties properties){
        this.properties = properties;
        this.exportStatusMonitor = new DealStatusDefaultMonitor();
    }

    public DefaultDataExportExcelHandler(ExportExcelDataProperties properties, DealStatusMonitor dealStatusMonitor){
        this.properties = properties;
        this.exportStatusMonitor = dealStatusMonitor;
    }

	public void handler(String taskId, SimpleDataAsyncProducer<?> data, Map<String, Object> taskContext) {
		data.init((Map<String, Object>) taskContext.get("producer_context"));
		Object runType = data.getContext().getOrDefault(Constants.RUN_TYPE, Constants.RUN_TYPE_NORMAL);
		ExcelFileWriteDelegater excelFileWriteDelegater = ExcelFileWriteDelegate.build((String) runType);
		try {
			logger.info("taskId:{} start to handle export", taskId);
			exportStatusMonitor.setExportStatus(taskId, "0");
			boolean noDataFlag = true;//防止无数据导出也生成文件
			boolean init = false;
			while (data.hasMore()) {
				if (!init){
					if (noDataFlag) noDataFlag = false;
					excelFileWriteDelegater.init(properties.getTmpFilePath().concat(taskId.concat(".xlsx")), data.getHeadTitle(), data.delimiterFlag());
					init = true;
				}
				excelFileWriteDelegater.writeData(data.stringData(), data.delimiterFlag());
				if (data.getCurrent() <= data.getTotal())
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
		}finally {
			data.destroy();
		}
	}

	public String getExportStatus(String taskId) {
		return exportStatusMonitor.getExportStatus(taskId);
	}
	

	protected ExportExcelDataProperties properties;
	protected DealStatusMonitor exportStatusMonitor;

}
