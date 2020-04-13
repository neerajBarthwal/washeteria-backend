package com.iiith.washeteria.service;

import java.util.List;

import com.iiith.washeteria.businessentities.LocationBE;

public interface LocationService {
	
   public List<LocationBE> getLocations();
   public void addLocation(LocationBE locationBE);
   
}
