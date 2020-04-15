package com.iiith.washeteria.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iiith.washeteria.businessentities.EventBE;
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
	public void addEvents(@RequestBody EventBE event) {
		eventService.addEvents(event);
	}

	@RequestMapping(value = "/events",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateEvent(@RequestBody EventBE event) {
		eventService.updateEvent(event);
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
