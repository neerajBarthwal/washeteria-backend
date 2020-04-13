package com.iiith.washeteria.translator;

import java.util.ArrayList;
import java.util.Date;
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
			machineBE.setRemainingTime(machine.getRemainingTime().getTime()/1000); //convert to unix-time(in seconds)
		}
		return machineBE;
	}

	public List<Machine> toDE(List<MachineBE> machineBEs) {
		
		List<Machine> machines = null;
		if(machineBEs!=null) {
			machines = new ArrayList<Machine>();
			for(MachineBE machineBE: machineBEs) {
				Machine machine = new Machine();
				machine.setMachineId(machineBE.getId());
				machine.setLocationId(machineBE.getLocationId());
				machine.setMachineName(machineBE.getName());
				if(machineBE.getRemainingTime()!=0) {
					Date remainingTime = new Date(machineBE.getRemainingTime()*1000);//convert to milliseconds
					machine.setRemainingTime(remainingTime);
				} 	
				machine.setStatus(machineBE.getStatus());
				machines.add(machine);
			}
		}
		return machines;
	}
	
}
