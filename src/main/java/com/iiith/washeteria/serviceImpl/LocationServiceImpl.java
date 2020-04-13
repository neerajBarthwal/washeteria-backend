package com.iiith.washeteria.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iiith.washeteria.businessentities.LocationBE;
import com.iiith.washeteria.dao.LocationDAO;
import com.iiith.washeteria.dataentities.Location;
import com.iiith.washeteria.service.LocationService;
import com.iiith.washeteria.translator.LocationTranslator;
@Service
public class LocationServiceImpl implements LocationService {

	@Autowired
	private LocationDAO locationDAO;
	
	@Autowired
	private LocationTranslator translator;
	
    public LocationServiceImpl() {
	}
	
	@Override
	public List<LocationBE> getLocations() {
		
		List<Location> locations = locationDAO.findAll();
		List<LocationBE> locationsBE = translator.toBE(locations);
		return locationsBE;
	}

	@Override
	public void addLocation(LocationBE locationBE) {
		// TODO Auto-generated method stub
		
	}

}
