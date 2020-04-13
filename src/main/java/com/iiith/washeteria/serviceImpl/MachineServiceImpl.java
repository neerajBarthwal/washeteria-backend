package com.iiith.washeteria.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iiith.washeteria.businessentities.MachineBE;
import com.iiith.washeteria.dao.MachineDAO;
import com.iiith.washeteria.dataentities.Machine;
import com.iiith.washeteria.service.MachineService;
import com.iiith.washeteria.translator.MachineTranslator;

@Service
public class MachineServiceImpl implements MachineService {
	
	@Autowired
	private MachineDAO machineDAO;
	
	@Autowired
	private MachineTranslator translate;

	@Override
	public List<MachineBE> getMachines() {
		
		List<Machine> machines = machineDAO.findAll();
		List<MachineBE> machineBEs = translate.toBE(machines);
		return machineBEs;
	}
	
	@Override
	public MachineBE getMachine(String machineId) {
		Machine machine = null;
		
		if(machineId!=null && machineId!="") {
			machine = machineDAO.getOne(machineId);
		}
		MachineBE machineBE = translate.toBE(machine);
		return machineBE;
	}

	@Override
	public List<MachineBE> getMachinesAt(String location) {
		
		List<Machine> machines = machineDAO.findByLocationId(location);
		List<MachineBE> machineBEs = translate.toBE(machines);
		return machineBEs;
	}

	@Override
	public void addMachine(List<MachineBE> machineBE) {
		
		if(machineBE!=null) {
			
			List<Machine> machines = translate.toDE(machineBE);
			for(Machine machine:machines)
				machineDAO.save(machine);
		}

	}

	

}
