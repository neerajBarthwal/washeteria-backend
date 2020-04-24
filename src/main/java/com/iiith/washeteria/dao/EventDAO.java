package com.iiith.washeteria.dao;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iiith.washeteria.dataentities.Event;

public interface EventDAO extends JpaRepository<Event, Long> {
	
	List<Event> findByModifiedTimeGreaterThanEqualOrderByModifiedTime(Instant modifiedAt);
	List<Event> findByEndTimeBetweenOrderByStartTime(Instant start, Instant end);
	List<Event> findByLocationIdAndStartTimeBetweenOrEndTimeBetween(long locationId, Instant start, Instant end,Instant start2, Instant end2);
	List<Event> findByUserIdAndEndTimeBetweenOrderByStartTime(String userId, Instant start, Instant end);
	List<Event> findByMachineIdAndEndTimeBetween(long machineId, Instant start, Instant end);
	List<Event> findByLocationId(long locationId);
	List<Event> findByLocationIdAndMachineIdAndStartTimeBetween(long locationId, long machineId, Instant start, Instant end);
	List<Event> findByLocationIdAndMachineIdAndEndTimeBetween(long locationId, long machineId, Instant start, Instant end);

}
