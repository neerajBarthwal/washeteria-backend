package com.iiith.washeteria.translator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.iiith.washeteria.businessentities.MachineBE;
import com.iiith.washeteria.dataentities.Machine;


@Component
public class MachineTranslator {

	public List<MachineBE> toBE(List<Machine> machines){
		
		List<MachineBE> machineBEs = null;
		if(machines!=null) {
			machineBEs = new ArrayList<MachineBE>();
			for(Machine machine:machines) {
				MachineBE machineBE = toBE(machine);
				machineBEs.add(machineBE);
			}
		}
		return machineBEs;
	}
	
	public MachineBE toBE(Machine machine) {
		
		MachineBE machineBE = null;
		if(machine!=null) {
			
			machineBE = new MachineBE();
			machineBE.setId(machine.getMachineId());
			machineBE.setLocationId(machine.getLocationId());
			machineBE.setName(machine.getMachineName());
			machineBE.setStatus(machine.getStatus());
			
			if(machine.getAvailableAt()!=null)
				machineBE.setNextAvailableAt(machine.getAvailableAt().getEpochSecond()); //convert to unix-time(in seconds)
		}
		return machineBE;
	}

	public List<Machine> toDE(List<MachineBE> machineBEs) {
		
		List<Machine> machines = null;
		if(machineBEs!=null) {
			machines = new ArrayList<Machine>();
			for(MachineBE machineBE: machineBEs) {
				Machine machine = new Machine();
				if(machineBE.getId()>0)
					machine.setMachineId(machineBE.getId());
				machine.setLocationId(machineBE.getLocationId());
				machine.setMachineName(machineBE.getName());
				machine.setStatus(machineBE.getStatus());
				machines.add(machine);
			}
		}
		return machines;
	}
	
}
