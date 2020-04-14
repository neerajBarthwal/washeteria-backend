package com.iiith.washeteria.service;

import java.util.List;

import com.iiith.washeteria.businessentities.EventBE;

public interface EventService {
	
	public EventBE getEvent(String eventId);
	public void addEvents(List<EventBE> events);
	public void updateEvent(EventBE event);
	public void deleteEvent(String eventId);
	public List<EventBE> getModifiedEvents(long modifiedTime);
	
}
