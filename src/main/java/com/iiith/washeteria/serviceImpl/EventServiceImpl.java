package com.iiith.washeteria.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iiith.washeteria.businessentities.EventBE;
import com.iiith.washeteria.dao.EventDAO;
import com.iiith.washeteria.dataentities.Event;
import com.iiith.washeteria.service.EventService;
import com.iiith.washeteria.translator.EventTranslator;

@Service
public class EventServiceImpl implements EventService {

	@Autowired
	private EventDAO eventDAO;

	@Autowired
	private EventTranslator translate;

	@Override
	public EventBE getEvent(String eventId) {

		EventBE eventBE = null;
		if(eventId!=null && eventId!="") {
			Event event = eventDAO.getOne(eventId);
			eventBE = translate.toBE(event);
		}
		return eventBE;
	}

	@Override
	public void addEvents(List<EventBE> eventBEs) {

		if(eventBEs!=null && eventBEs.size()>0) {
			List<Event> events = translate.toDE(eventBEs);
			eventDAO.saveAll(events);
		}

	}

	@Override
	public void updateEvent(EventBE eventBE) {

		if(eventBE!=null) {
			Event event = translate.toDE(eventBE);
			eventDAO.save(event);
		}

	}

	@Override
	public void deleteEvent(String eventId) {

		if(eventId!=null && eventId.length()>0) {
			eventDAO.deleteById(eventId);
		}
		
	}

}
