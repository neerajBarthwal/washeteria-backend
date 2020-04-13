package com.iiith.washeteria.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iiith.washeteria.businessentities.MachineBE;
import com.iiith.washeteria.service.MachineService;

@RestController
public class MachineController {

	@Autowired
	private MachineService machineService;
	
	@RequestMapping(value = "/machines",
					method = RequestMethod.GET, 
					produces = MediaType.APPLICATION_JSON_VALUE)
	public List<MachineBE> getMachines(){
		return machineService.getMachines();
	}
	
	@RequestMapping(value = "/machines/{machineId}", 
					method = RequestMethod.GET, 
					produces = MediaType.APPLICATION_JSON_VALUE)
	public MachineBE getMachine(@PathVariable("machineId") String machineId) {
		return machineService.getMachine(machineId);
	}
	
	@RequestMapping(value = "/machines/location/{locationId}", 
					method = RequestMethod.GET,  
					produces = MediaType.APPLICATION_JSON_VALUE)
	public List<MachineBE> getMachinesAt(@PathVariable("locationId") String location){
		return machineService.getMachinesAt(location);
	}
	
	@RequestMapping(value = "/machines",
					method = RequestMethod.POST,
					consumes = MediaType.APPLICATION_JSON_VALUE,
					produces = MediaType.APPLICATION_JSON_VALUE)
	public void addMachine(@RequestBody List<MachineBE> machines) {
		machineService.addMachine(machines);
	}
}
