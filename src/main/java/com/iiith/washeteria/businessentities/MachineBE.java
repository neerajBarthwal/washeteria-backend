package com.iiith.washeteria.businessentities;

public class MachineBE {
	
	private String id;
	private String name;
	private String locationId;
	private String status;
	private long remainingTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public long getRemainingTime() {
		return remainingTime;
	}
	
	public void setRemainingTime(long remainingTime) {
		this.remainingTime = remainingTime;
	}
	
}
