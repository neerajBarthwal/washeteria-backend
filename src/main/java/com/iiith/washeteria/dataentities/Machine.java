package com.iiith.washeteria.dataentities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="machine")
public class Machine {
	
	@Id
	@Column
	private String machineId;
	
	@Column
	private String machineName;
	
	@Column
	private String locationId;
	
	@Column
	private String status;
	
	@Column
	private Date remainingTime;

	public String getMachineId() {
		return machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
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

	public Date getRemainingTime() {
		return remainingTime;
	}

	public void setRemainingTime(Date remainingTime) {
		this.remainingTime = remainingTime;
	}
	
}
