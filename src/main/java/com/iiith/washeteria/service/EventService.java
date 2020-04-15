package com.iiith.washeteria.service;

import java.util.List;

import com.iiith.washeteria.businessentities.EventBE;

public interface EventService {
	
	public EventBE getEvent(long eventId);
	public void addEvents(EventBE event);
	public void updateEvent(EventBE event);
	public void deleteEvent(long eventId);
	public List<EventBE> getModifiedEvents(long modifiedTime);
	public List<EventBE> getEvents();
	
}
