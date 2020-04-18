package com.iiith.washeteria.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iiith.washeteria.businessentities.EventBE;
import com.iiith.washeteria.exceptions.ErrorMessage;
import com.iiith.washeteria.service.EventService;

@RestController
public class EventController {

	@Autowired
	private EventService eventService;

	@RequestMapping(value = "/events",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public List<EventBE> getEvents() {
		return eventService.getEvents();
	}

	@RequestMapping(value = "/events/{eventId}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public EventBE getEvent(@PathVariable("eventId") long eventId) {
		return eventService.getEvent(eventId);
	}

	@RequestMapping(value = "/events",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addEvents(@RequestBody EventBE event) {
		EventBE createdEvent = null;
		try {
			createdEvent = eventService.addEvents(event);
		} catch (ErrorMessage errorMessage) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorMessage);
		}
		return ResponseEntity.ok().body(createdEvent);
	}

	@RequestMapping(value = "/events",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public EventBE updateEvent(@RequestBody EventBE event) {
		return eventService.updateEvent(event);
	}

	@RequestMapping(value = "/events/{eventId}",
			method = RequestMethod.DELETE)
	public void deleteEvent(@PathVariable("eventId") long eventId) {
		eventService.deleteEvent(eventId);
	}

	@RequestMapping(value = "events/modified/{modifiedTime}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public List<EventBE> getModifiedEvents(@PathVariable long modifiedTime) {
		return eventService.getModifiedEvents(modifiedTime);
	}


}
