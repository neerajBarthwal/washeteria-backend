package com.iiith.washeteria.businessentities;

import java.time.Instant;
import java.util.Comparator;

public class Slot {
	
	private Instant startTime;
	private Instant endTime;
	private long machineId;
	public Instant getStartTime() {
		return startTime;
	}
	public void setStartTime(Instant startTime) {
		this.startTime = startTime;
	}
	public Instant getEndTime() {
		return endTime;
	}
	public void setEndTime(Instant endTime) {
		this.endTime = endTime;
	}
	public long getMachineId() {
		return machineId;
	}
	public void setMachineId(long machineId) {
		this.machineId = machineId;
	}
	
	public static class SlotComparator implements Comparator<Slot>{

		@Override
		public int compare(Slot first, Slot second) {
			if(first.getStartTime().isAfter(second.getStartTime()))
				return 1;
			else if(first.getStartTime().isBefore(second.getStartTime()))
				return -1;
			return 0;
		}
		
	}
}


