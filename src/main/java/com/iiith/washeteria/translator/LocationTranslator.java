package com.iiith.washeteria.translator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.iiith.washeteria.businessentities.LocationBE;
import com.iiith.washeteria.dataentities.Location;

@Component
public class LocationTranslator {

	public List<LocationBE> toBE(List<Location> locations){
		
		List<LocationBE> locationsBE=null;
		
		if(locations!=null) {
			locationsBE = new ArrayList<LocationBE>();
			for(Location location:locations) {
				LocationBE locationBE = new LocationBE();
				locationBE.setId(location.getLocationId());
				locationBE.setName(location.getName());
				locationsBE.add(locationBE);
			}
		}
		
		return locationsBE;
	}
	
	
	public Location toDE(LocationBE locationBE) {
		Location location = null;
		if(locationBE!=null) {
			location = new Location();
			location.setLocationId(locationBE.getId());
			location.setName(locationBE.getName());
		}
		return location;
	}
}
