package com.iiith.washeteria.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iiith.washeteria.dataentities.Event;

public interface EventDAO extends JpaRepository<Event, String> {

}
