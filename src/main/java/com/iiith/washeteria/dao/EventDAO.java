package com.iiith.washeteria.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iiith.washeteria.dataentities.Event;

public interface EventDAO extends JpaRepository<Event, String> {
	
	List<Event> findByModifiedTimeGreaterThanEqualOrderByModifiedTime(Date modifiedAt);
}
