package com.iiith.washeteria.service;

import java.util.List;

import com.iiith.washeteria.businessentities.AssistedEvent;
import com.iiith.washeteria.businessentities.EventBE;
import com.iiith.washeteria.exceptions.ErrorMessage;

public interface EventService {
	
	public EventBE getEvent(long eventId);
	public EventBE addEvents(EventBE event) throws ErrorMessage;
	public EventBE updateEvent(EventBE event);
	public void deleteEvent(long eventId);
	public List<EventBE> getModifiedEvents(long modifiedTime);
	public List<EventBE> getEvents();
	public EventBE createAssistedEvent(AssistedEvent assistedEvent);
	
}
