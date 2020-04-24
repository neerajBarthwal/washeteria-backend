package com.iiith.washeteria.businessentities;

import java.util.List;

public class AssistedEvent {
	
	private String userId;
	private long locationId;
	private List<Preference> preferences;
	private long duration; // in minutes
	private boolean ignorePreference;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public long getLocationId() {
		return locationId;
	}
	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}
	public List<Preference> getPreferences() {
		return preferences;
	}
	public void setPreferences(List<Preference> preferences) {
		this.preferences = preferences;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public boolean isIgnorePreference() {
		return ignorePreference;
	}
	public void setIgnorePreference(boolean ignorePreference) {
		this.ignorePreference = ignorePreference;
	}
	
}
