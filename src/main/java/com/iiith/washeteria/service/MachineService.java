package com.iiith.washeteria.service;

import java.util.List;

import com.iiith.washeteria.businessentities.MachineBE;

public interface MachineService {
	
	public List<MachineBE> getMachines();
	public MachineBE getMachine(String machineId);
	public List<MachineBE> getMachinesAt(String location);
	public void addMachine(List<MachineBE> machine);
}
