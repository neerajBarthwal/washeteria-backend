package com.iiith.washeteria.translator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.iiith.washeteria.businessentities.EventBE;
import com.iiith.washeteria.dataentities.Event;

@Component
public class EventTranslator {

	public EventBE toBE(Event event) {
		EventBE eventBE = null;
		if(event!=null) {
			eventBE = new EventBE();
			eventBE.setEventId(event.getEventId());
			eventBE.setMachineId(event.getMachineId());
			eventBE.setLocationId(event.getLocationId());
			eventBE.setStartTime(event.getStartTime().getTime()/1000);
			eventBE.setEndTime(event.getEndTime().getTime()/1000);
			eventBE.setUserId(event.getUserId());
		}
		return eventBE;
	}

	public List<EventBE> toBE(List<Event> events){

		List<EventBE> eventBEs = null;
		if(events!=null) {
			eventBEs = new ArrayList<EventBE>();
			for(Event event:events) {
				EventBE eventBE = toBE(event);
				eventBEs.add(eventBE);
			}
		}
		return eventBEs;
	}

	public Event toDE(EventBE eventBE) {

		Event event = null;

		if(eventBE!=null) {
			event = new Event();
			event.setEventId(eventBE.getEventId());
			event.setMachineId(eventBE.getMachineId());
			event.setLocationId(eventBE.getLocationId());
			if(eventBE.getStartTime()!=0) {
				Date startTime = new Date(eventBE.getStartTime()*1000);
				event.setStartTime(startTime);
			}
			if(eventBE.getEndTime()!=0) {
				Date endTime = new Date(eventBE.getEndTime()*1000);
				event.setEndTime(endTime);

			}
			event.setUserId(eventBE.getUserId());
		}
		return event;
	}

	public List<Event> toDE(List<EventBE> eventBEs){

		List<Event> events = null;
		if(eventBEs!=null) {
			events = new ArrayList<Event>();
			for(EventBE eventBE:eventBEs) {
				Event event = toDE(eventBE);
				events.add(event);
			}
		}
		return events;
	}
}
