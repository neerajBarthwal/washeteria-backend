package com.iiith.washeteria.businessentities;

public class MachineBE {
	
	private long id;
	private String name;
	private long locationId;
	private String status;
	private long nextAvailableAt;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getLocationId() {
		return locationId;
	}
	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public long getNextAvailableAt() {
		return nextAvailableAt;
	}
	public void setNextAvailableAt(long nextAvailableAt) {
		this.nextAvailableAt = nextAvailableAt;
	}
	
}
