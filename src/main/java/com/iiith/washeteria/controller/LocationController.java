package com.iiith.washeteria.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iiith.washeteria.businessentities.LocationBE;
import com.iiith.washeteria.service.LocationService;

@RestController
public class LocationController {
	
	@Autowired
	private LocationService locationService;
	
	
	@RequestMapping(value="/locations",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<LocationBE> getLocations(){
		
		return locationService.getLocations();
	}
}
