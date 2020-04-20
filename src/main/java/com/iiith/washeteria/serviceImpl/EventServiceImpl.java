package com.iiith.washeteria.serviceImpl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iiith.washeteria.businessentities.AssistedEvent;
import com.iiith.washeteria.businessentities.EventBE;
import com.iiith.washeteria.businessentities.MachineBE;
import com.iiith.washeteria.dao.EventDAO;
import com.iiith.washeteria.dataentities.Event;
import com.iiith.washeteria.exceptions.ErrorMessage;
import com.iiith.washeteria.exceptions.ExceptionCodes;
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
		List<Event> events = eventDAO.findByEndTimeBetweenOrderByStartTime(today, endOfweek);
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
	public EventBE addEvents(EventBE eventBE) throws ErrorMessage {
		EventBE createdEvent = null;
		boolean isOverlapping  = isOverlapping(eventBE);
		//boolean isUnderAllowedLimit = isWithinLimit(eventBE.getUserId());//Removed reservation limit for demo purpose.
		if(eventBE!=null && !isOverlapping) {
			Event event = translate.toDE(eventBE);
			event = eventDAO.save(event);
			Instant availableAt = getNextAvailableTime(event);
			machineService.updateMachineAvailability(event.getMachineId(),availableAt);
			createdEvent = translate.toBE(event);
		}
		return createdEvent;
	}
	
//	private boolean isWithinLimit(String userId) {
//		Instant today = Instant.now();
//		Instant startOfWeek = today.minus(Period.ofDays(6));
//		
//		List<Event> weeklyEventsByUser = eventDAO.
//				findByUserIdAndEndTimeBetweenOrderByStartTime(userId,startOfWeek, today);
//		if(weeklyEventsByUser.size()+1>3) // allow only three events per week.
//			return false;
//		return true;
//	}

	private boolean isOverlapping(EventBE newEvent) throws ErrorMessage {
		
		Instant today = Instant.now();
		Instant endOfweek = today.plus(Period.ofDays(7));
		List<Event> existingEvents = eventDAO.
									 findByMachineIdAndEndTimeBetween(newEvent.getMachineId(),today,endOfweek);
		
		for(Event existingEvent:existingEvents) {
			Instant startTimeOfNewEvent = Instant.ofEpochSecond(newEvent.getStartsAt());
			Instant endTimeOfNewEvent = Instant.ofEpochSecond(newEvent.getEndsAt());
			
			Instant startTime = existingEvent.getStartTime();
			Instant endTime = existingEvent.getEndTime();
			
			if(startTimeOfNewEvent.isAfter(startTime) && startTimeOfNewEvent.isBefore(endTime))
				throw new ErrorMessage(ExceptionCodes.OVERLAPPING_EVENT);
			else if(endTimeOfNewEvent.isAfter(startTime) && endTimeOfNewEvent.isBefore(endTime))
				throw new ErrorMessage(ExceptionCodes.OVERLAPPING_EVENT);
		}
		return false;
	}
	

	@Override
	public EventBE updateEvent(EventBE eventBE) {
		EventBE updatedEvent = null;
		if(eventBE!=null) {
			Event event = translate.toDE(eventBE);
			event = eventDAO.save(event);
			updatedEvent = translate.toBE(event);
			Instant availableAt = getNextAvailableTime(event);
			machineService.updateMachineAvailability(event.getMachineId(),availableAt);
		}
		return updatedEvent;
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

		List<Event> events = eventDAO.findByMachineIdAndEndTimeBetween(event.getMachineId(),now,endOfWeek);

		PriorityQueue<Event> priorityQueue = new PriorityQueue<Event>(new EventComparator());
		priorityQueue.addAll(events);
		if(!event.isCancelled())
			priorityQueue.add(event);
		long timeDifferenceInMinutes = (priorityQueue.peek().getStartTime().getEpochSecond()
																	-now.getEpochSecond())/60;
		if(timeDifferenceInMinutes>=30)
			return now;
		Instant nextAvailableAt = getFistAvailableSlot(priorityQueue);
		return nextAvailableAt;
	}

	private Instant getFistAvailableSlot(PriorityQueue<Event> priorityQueue) {
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

	@Override
	public EventBE createAssistedEvent(AssistedEvent assistedEvent) {
		//boolean isUnderAllowedLimit = isWithinLimit(assistedEvent.getUserId());
		boolean isAllotedPref = false;
		Instant startsAt = null;
		Instant endsAt = null;
		Set<Long> availableMachines = null;
		EventBE eventBE = null;
		if(assistedEvent!=null) {			
			Set<Long> machines = getMachinesAt(assistedEvent.getLocationId());
			List<List<Long>> timePreferences = assistedEvent.getPreferences();
			
			if(timePreferences!=null) {			
				for(List<Long> preferredTime: timePreferences) {
					Set<Long> allMachines = machines;
					availableMachines = getAvailableMachinesAtPreferredTime(allMachines,preferredTime,assistedEvent.getLocationId());
					if(!availableMachines.isEmpty()) {
						isAllotedPref = true;
						startsAt = Instant.ofEpochSecond(preferredTime.get(0));
						endsAt = Instant.ofEpochSecond(preferredTime.get(1));
						break;
					}
					
				}
				
			}
		}
		
		if(isAllotedPref) {
			long machineId = (long)availableMachines.toArray()[0];
			Event createdEvent = createEventFrom(assistedEvent.getUserId(),
							assistedEvent.getLocationId(),
							machineId,
							startsAt,
							endsAt);
			eventBE = translate.toBE(createdEvent);
		}else if(assistedEvent.isIgnorePreference()) {
			Event createdEvent = createDefaultEvent(assistedEvent);
			eventBE = translate.toBE(createdEvent);
		}
		
		return eventBE;
	}

	private Event createDefaultEvent(AssistedEvent assistedEvent) {
		List<Event> existingEvents = eventDAO.findByLocationId(assistedEvent.getLocationId());
		PriorityQueue<Event> priorityQueue = new PriorityQueue<Event>(new EventComparator());
		priorityQueue.addAll(existingEvents);
		Instant startTime = getFistAvailableSlot(priorityQueue);
		Instant endTime = startTime.plusSeconds(1800);
		return createEventFrom(assistedEvent.getUserId(),
							   assistedEvent.getLocationId(),
							   1,// machine id TODO: Create builder pattern for EVENT object.
							   startTime,
							   endTime);
		
		
	}

	private Event createEventFrom(String userId, long locationId, long machineId, Instant startsAt, Instant endsAt) {
		Event event = new Event();
		event.setUserId(userId);
		event.setMachineId(machineId);
		event.setLocationId(locationId);
		event.setStartTime(startsAt);
		event.setEndTime(endsAt);
		return eventDAO.save(event);
	}

	private Set<Long> getAvailableMachinesAtPreferredTime(Set<Long> allMachines, List<Long> preferredTime, long locationId) {
		
			Instant startTime = Instant.ofEpochSecond(preferredTime.get(0));
			Instant endTime = Instant.ofEpochSecond(preferredTime.get(1));
			Set<Long>occupiedMachines = occupiedMachinesBetween(startTime, endTime, locationId);
			allMachines.removeAll(occupiedMachines);//these are the available machines.
			return allMachines;			
	}

	private Set<Long> occupiedMachinesBetween(Instant startTime, Instant endTime, long locationId) {
		List<Event> existingEvents = eventDAO.
				findByLocationIdAndStartTimeBetweenOrEndTimeBetween(locationId,startTime, endTime,startTime, endTime);
		Set<Long>occupiedMachines = new HashSet<Long>();
		for(Event event: existingEvents)
			occupiedMachines.add(event.getMachineId());
		return occupiedMachines;
	}

	private Set<Long> getMachinesAt(long locationId) {
		List<MachineBE> machines = machineService.getMachinesAt(locationId);
		Set<Long> allMachines = new HashSet<Long>();
		for(MachineBE machine: machines) {
			allMachines.add(machine.getId());
		}
		return allMachines;
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
