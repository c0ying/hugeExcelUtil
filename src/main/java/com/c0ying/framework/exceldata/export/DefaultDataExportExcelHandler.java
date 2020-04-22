package com.c0ying.framework.exceldata.export;

import com.c0ying.framework.exceldata.DealStatusMonitor;
import com.c0ying.framework.exceldata.delegate.SaxExcelFileWriteDelegate;
import com.c0ying.framework.exceldata.export.bean.ExportExcelDataProperties;
import com.c0ying.framework.exceldata.export.dataproducer.SimpleDataAsyncProducer;
import com.c0ying.framework.exceldata.utils.TokenUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * 导出任务流程主控制
 * @author Cyh
 *
 */
public class DefaultDataExportExcelHandler implements DataExportExcelHandler{

	protected Logger logger  = LoggerFactory.getLogger(DefaultDataExportExcelHandler.class);

	public void handler(String taskId, SimpleDataAsyncProducer<?> data) {
		SaxExcelFileWriteDelegate writeDelegateHandler = new SaxExcelFileWriteDelegate();
		try {
			logger.info("taskId:{} start to handle export", taskId);
			exportStatusMonitor.setExportStatus(taskId, "0");
			boolean noDataFlag = true;
			boolean init = false;
			while (data.hasMore()) {
				if (!init){
					if (noDataFlag) noDataFlag = false;
					writeDelegateHandler.init(new File(properties.getTmpFilePath(), taskId.concat(".xlsx")).getAbsolutePath(), data.getHeadTitle(), data.delimiterFlag());
					init = true;
				}
				writeDelegateHandler.writeData(data.stringData(), data.delimiterFlag());
				exportStatusMonitor.setExportStatus(taskId, String.format("%.2f", ((float)data.getCurrent())/data.getTotal()));
			}
			if (!noDataFlag) {
				writeDelegateHandler.finish();
			}else{
				exportStatusMonitor.setExportStatus(taskId, "-100");
			}
		} catch (Exception e) {
			logger.error("taskId:{} export handle occur error! exception:{}", taskId, e.getMessage());
			throw new RuntimeException(e);
		}
	}
	@Override
	public void write(String taskId, SXSSFWorkbook wb) throws Exception {
		File wbFile = new File(properties.getTmpFilePath(), taskId.concat(".xlsx"));
		if (!wbFile.getParentFile().exists()) wbFile.getParentFile().mkdirs();
		try (FileOutputStream outputStream = new FileOutputStream(wbFile)){
			wb.write(outputStream);
		}catch (Exception e) {
			throw new Exception(e);
		}
	}

	public String getExportStatus(String taskId) {
		return exportStatusMonitor.getExportStatus(taskId);
	}
	
	/**
	 * 启动导出任务；
	 * 单线程处理;
	 * 貌似 SXSSFWorkbook 不支持多线程处理
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
				dataProducer.init((Map<String, Object>) taskContext.get("producer_context"));
				handler(taskId, dataProducer);
			} catch (Exception e) {
				exportStatusMonitor.setExportStatus(taskId, "-100");
				e.printStackTrace();
				throw e;
			}finally {
				dataProducer.destroy();
			}
		}
	}
	
	private ExportExcelDataProperties properties = new ExportExcelDataProperties();
	private DealStatusMonitor exportStatusMonitor;
	private ExecutorService taskExecutor = Executors.newCachedThreadPool();

}
