package com.c0ying.framework.exceldata.monitor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class DealStatusDefaultMonitor implements DealStatusMonitor {

	private static ConcurrentHashMap<String, StatusData> statusMap = new ConcurrentHashMap<>();

	public void setExportStatus(String taskUid, String status){
		statusMap.put("deal_status_".concat(taskUid), new StatusData(status, addTime(TimeUnit.HOURS.toMillis(1))));
		clearData();
	}

	public void setExportErrorMsg(String taskUid, String errorMsg){
		try {
			if (statusMap.containsKey(("deal_status_".concat(taskUid)))){
				statusMap.get("deal_status_".concat(taskUid)).setExceptionMsg(errorMsg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getErrorMsg(String taskUid) {
		try {
			if (statusMap.containsKey(("deal_status_".concat(taskUid)))){
				return statusMap.get("deal_status_".concat(taskUid)).ext.getOrDefault("errorMsg","").toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public void markFinished(String taskUid){
		try {
			if (statusMap.containsKey(("deal_status_".concat(taskUid)))){
				statusMap.get("deal_status_".concat(taskUid)).finish = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getExportStatus(String taskUid){
		try {
			if (statusMap.containsKey(("deal_status_".concat(taskUid)))){
				return statusMap.get("deal_status_".concat(taskUid)).data.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "0";
	}

	@Override
	public boolean isFinished(String taskUid) {
		try {
			if (statusMap.containsKey(("deal_status_".concat(taskUid)))){
				return statusMap.get("deal_status_".concat(taskUid)).finish;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean isException(String taskUid) {
		try {
			if (statusMap.containsKey(("deal_status_".concat(taskUid)))){
				return statusMap.get("deal_status_".concat(taskUid)).exception;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void clearExportStatus(String taskUid){
		StatusData statusData = statusMap.get("deal_status_".concat(taskUid));
		if (statusData != null){
			statusMap.put("deal_status_".concat(taskUid), new StatusData(statusData.data, addTime(TimeUnit.MINUTES.toMillis(5))));
		}
	}

	protected void clearData(){
		Iterator<Entry<String, StatusData>> iterator = statusMap.entrySet().iterator();
		while (iterator.hasNext()){
			if (System.currentTimeMillis() >= iterator.next().getValue().expireTime){
				iterator.remove();
			}
		}
	}

	protected Long addTime(long subTime){
		return System.currentTimeMillis() + subTime;
	}

	class StatusData{
		private Object data;
		private Long expireTime;
		private boolean exception = false;
		private boolean finish = false;
		private Map<String, Object> ext = new HashMap<>(1);

		StatusData(Object data, Long expireTime){
			this.data = data;
			this.expireTime = expireTime;
		}

		public void setExceptionMsg(String msg){
			exception = true;
			ext.put("errorMsg", msg);
		}
	}
}
