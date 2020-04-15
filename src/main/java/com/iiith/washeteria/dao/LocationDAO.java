package com.iiith.washeteria.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iiith.washeteria.dataentities.Location;


public interface LocationDAO extends JpaRepository<Location, Long>{
	
}
