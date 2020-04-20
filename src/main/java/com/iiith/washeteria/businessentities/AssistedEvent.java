package com.iiith.washeteria.businessentities;

import java.util.List;

public class AssistedEvent {
	
	private String userId;
	private long machineId;
	private long locationId;
	List<List<Long>> preferences;
	private boolean ignorePreference;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public List<List<Long>> getPreferences() {
		return preferences;
	}
	public void setPreferences(List<List<Long>> preferences) {
		this.preferences = preferences;
	}
	public boolean isIgnorePreference() {
		return ignorePreference;
	}
	public void setIgnorePreference(boolean ignorePreference) {
		this.ignorePreference = ignorePreference;
	}
	
}
