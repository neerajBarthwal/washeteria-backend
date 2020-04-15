package com.iiith.washeteria.service;

import java.time.Instant;
import java.util.List;

import com.iiith.washeteria.businessentities.MachineBE;

public interface MachineService {
	
	public List<MachineBE> getMachines();
	public MachineBE getMachine(long machineId);
	public List<MachineBE> getMachinesAt(long location);
	public void addMachine(List<MachineBE> machine);
	public void updateMachineAvailability(long machineId, Instant availableAt);
}
