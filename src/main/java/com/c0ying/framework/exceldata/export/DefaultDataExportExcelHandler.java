package com.c0ying.framework.exceldata.export;

import com.c0ying.framework.exceldata.delegate.ExcelFileWriteDelegater;
import com.c0ying.framework.exceldata.delegate.ExcelFileWriteDelegaterFactory;
import com.c0ying.framework.exceldata.export.bean.ExportExcelDataProperties;
import com.c0ying.framework.exceldata.export.dataproducer.SimpleDataProducer;
import com.c0ying.framework.exceldata.monitor.DealStatusDefaultMonitor;
import com.c0ying.framework.exceldata.monitor.DealStatusMonitor;
import com.c0ying.framework.exceldata.utils.Constants;
import com.c0ying.framework.exceldata.utils.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;

/**
 * 
 * 导出任务流程主控制
 * @author c0ying
 *
 */
public class DefaultDataExportExcelHandler implements DataExportExcelHandler{

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

	public void handler(String taskId, SimpleDataProducer<?> data, Map<String, Object> taskContext) {
		data.init(taskContext);
		Object runType = data.getContext().getOrDefault(Constants.RUN_TYPE, Constants.RUN_TYPE_NORMAL);
		ExcelFileWriteDelegater excelFileWriteDelegater = ExcelFileWriteDelegaterFactory.build((String) runType);
		try {
			logger.info("taskId:{} start to handle async export", taskId);
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
				exportStatusMonitor.markFinished(taskId);
			}else{
				exportStatusMonitor.setExportStatus(taskId, "-100");
				exportStatusMonitor.setExportErrorMsg(taskId,"no data to be exported");
			}
		} catch (Exception e) {
			logger.error("taskId:{} export handle occur error! exception:{}", taskId, e.getMessage());
			exportStatusMonitor.setExportStatus(taskId, "-100");
			exportStatusMonitor.setExportErrorMsg(taskId,e.getMessage());
		}finally {
			data.destroy();
		}
	}

	@Override
	public void handler(String taskId, SimpleDataProducer<?> data) {
		handler(taskId, data, Collections.emptyMap());
	}

	@Override
	public void handler(SimpleDataProducer<?> data, Map<String, Object> taskContext) {
		handler(TokenUtil.getToken(), data, taskContext);
	}

	@Override
	public void handler(SimpleDataProducer<?> data) {
		handler(TokenUtil.getToken(), data, Collections.emptyMap());
	}

	public String getExportStatus(String taskId) {
		return exportStatusMonitor.getExportStatus(taskId);
	}
	

	protected ExportExcelDataProperties properties;
	protected DealStatusMonitor exportStatusMonitor;

}
