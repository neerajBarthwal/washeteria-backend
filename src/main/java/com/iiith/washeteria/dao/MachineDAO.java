package com.iiith.washeteria.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iiith.washeteria.dataentities.Machine;

public interface MachineDAO extends JpaRepository<Machine, Long>{
	List<Machine> findByLocationId(long locationId);
}
