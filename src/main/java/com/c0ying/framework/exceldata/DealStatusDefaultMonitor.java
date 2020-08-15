package com.c0ying.framework.exceldata;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class DealStatusDefaultMonitor implements DealStatusMonitor {

	private static ConcurrentHashMap<String, StatusData> statusMap = new ConcurrentHashMap<>();

	public void setExportStatus(String taskUid, String status){
		statusMap.put("deal_status_".concat(taskUid), new StatusData(status, addTime(TimeUnit.HOURS.toMillis(1))));
		clearData();
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

		StatusData(Object data, Long expireTime){
			this.data = data;
			this.expireTime = expireTime;
		}

		public Object getData() {
			return data;
		}

		public Long getExpireTime() {
			return expireTime;
		}
	}
}
