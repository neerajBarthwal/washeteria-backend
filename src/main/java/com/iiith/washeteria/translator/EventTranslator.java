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
			eventBE.setStartsAt(event.getStartTime().getTime()/1000);
			eventBE.setEndsAt(event.getEndTime().getTime()/1000);
			eventBE.setUserId(event.getUserId());
			eventBE.setModifiedAt(event.getModifiedTime().getTime()/1000);
			eventBE.setCancelled(event.isCancelled());
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
			
			if(eventBE.getStartsAt()!=0) {
				Date startTime = new Date(eventBE.getStartsAt()*1000);
				event.setStartTime(startTime);
			}
			if(eventBE.getEndsAt()!=0) {
				Date endTime = new Date(eventBE.getEndsAt()*1000);
				event.setEndTime(endTime);

			}
			if(eventBE.getModifiedAt()==0)
				event.setModifiedTime(new Date());
			else
				event.setModifiedTime(new Date(eventBE.getModifiedAt()*1000));
			event.setUserId(eventBE.getUserId());
			event.setCancelled(eventBE.isCancelled());
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
