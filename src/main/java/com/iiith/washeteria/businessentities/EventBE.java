package com.iiith.washeteria.businessentities;

public class EventBE {

	private long eventId;
	private long startsAt;
	private long endsAt;
	private long modifiedAt;
	private long machineId;
	private long locationId;
	private String userId;
	private boolean isCancelled;

	public long getEventId() {
		return eventId;
	}
	public void setEventId(long eventId) {
		this.eventId = eventId;
	}
	public long getStartsAt() {
		return startsAt;
	}
	public void setStartsAt(long startsAt) {
		this.startsAt = startsAt;
	}
	public long getEndsAt() {
		return endsAt;
	}
	public void setEndsAt(long endsAt) {
		this.endsAt = endsAt;
	}
	public long getModifiedAt() {
		return modifiedAt;
	}
	public void setModifiedAt(long modifiedAt) {
		this.modifiedAt = modifiedAt;
	}
	
	public long getMachineId() {
		return machineId;
	}
	public void setMachineId(long machineId) {
		this.machineId = machineId;
	}
	public long getLocationId() {
		return locationId;
	}
	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public boolean isCancelled() {
		return isCancelled;
	}
	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}
	

}
