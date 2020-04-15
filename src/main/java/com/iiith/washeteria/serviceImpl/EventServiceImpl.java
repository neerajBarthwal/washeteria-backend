package com.iiith.washeteria.serviceImpl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iiith.washeteria.businessentities.EventBE;
import com.iiith.washeteria.dao.EventDAO;
import com.iiith.washeteria.dataentities.Event;
import com.iiith.washeteria.service.EventService;
import com.iiith.washeteria.service.MachineService;
import com.iiith.washeteria.translator.EventTranslator;

@Service
public class EventServiceImpl implements EventService {

	@Autowired
	private EventDAO eventDAO;

	@Autowired
	private MachineService machineService;

	@Autowired
	private EventTranslator translate;

	@Override
	public List<EventBE> getEvents() {

		List<EventBE> eventBEs = null;

		Instant today = Instant.now();
		Instant endOfweek = today.plus(Period.ofDays(7));
		List<Event> events = eventDAO.findByStartTimeBetween(today, endOfweek);
		if(events!=null)
			eventBEs = translate.toBE(events);
		return eventBEs;
	}

	@Override
	public EventBE getEvent(long eventId) {

		EventBE eventBE = null;
		if(eventId>0) {
			Event event = eventDAO.getOne(eventId);
			eventBE = translate.toBE(event);
		}
		return eventBE;
	}

	@Override
	public void addEvents(EventBE eventBE) {

		if(eventBE!=null) {
			Event event = translate.toDE(eventBE);
			eventDAO.save(event);
			Instant availableAt = getNextAvailableTime(event);
			machineService.updateMachineAvailability(event.getMachineId(),availableAt);
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
	public void deleteEvent(long eventId) {

		if(eventId>0) {
			eventDAO.deleteById(eventId);
		}

	}

	@Override
	public List<EventBE> getModifiedEvents(long modifiedTime) {
		List<EventBE> eventBEs = null;
		if(modifiedTime>0) {
			Instant modifiedAt = Instant.ofEpochSecond(modifiedTime);
			List<Event>  events = eventDAO.findByModifiedTimeGreaterThanEqualOrderByModifiedTime(modifiedAt);
			eventBEs = translate.toBE(events);
		}
		return eventBEs;
	}


	private Instant getNextAvailableTime(Event event) {

		Instant now = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant();
		Instant endOfWeek = now.plus(Period.ofDays(7));

		List<Event> events = eventDAO.findByMachineIdAndStartTimeBetween(event.getMachineId(),now,endOfWeek);

		PriorityQueue<Event> priorityQueue = new PriorityQueue<Event>(new EventComparator());
		priorityQueue.addAll(events);
		priorityQueue.add(event);

		Instant nextAvailableAt = null;
		while(priorityQueue.size()>1) {
			Event topEvent = priorityQueue.poll();
			long timeDifferenceInMinutes = (priorityQueue.peek().getStartTime().getEpochSecond()
											-topEvent.getEndTime().getEpochSecond())/60;
			if(timeDifferenceInMinutes>=30) {
				nextAvailableAt = topEvent.getEndTime();
				break;
			}
		}

		if(nextAvailableAt==null)
			nextAvailableAt = priorityQueue.poll().getEndTime();
		return nextAvailableAt;
	}

}

class EventComparator implements Comparator<Event>{

	@Override
	public int compare(Event firstEvent, Event secondEvent) {
		if(firstEvent.getEndTime().isAfter(secondEvent.getEndTime()))
			return 1;
		else if(firstEvent.getEndTime().isBefore(secondEvent.getEndTime()))
			return -1;
		return 0;
	}	
}
