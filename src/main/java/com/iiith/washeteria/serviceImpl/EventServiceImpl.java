package com.iiith.washeteria.serviceImpl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iiith.washeteria.businessentities.AssistedEvent;
import com.iiith.washeteria.businessentities.EventBE;
import com.iiith.washeteria.businessentities.Preference;
import com.iiith.washeteria.businessentities.Slot;
import com.iiith.washeteria.dao.EventDAO;
import com.iiith.washeteria.dao.MachineDAO;
import com.iiith.washeteria.dataentities.Event;
import com.iiith.washeteria.dataentities.Machine;
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
	private MachineDAO machineDAO;

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
		Instant nextAvailableAt = getFistAvailableSlot(priorityQueue,30);
		return nextAvailableAt;
	}

	private Instant getFistAvailableSlot(PriorityQueue<Event> priorityQueue, long durationInMinutes) {
		Instant nextAvailableAt = null;
		while(priorityQueue.size()>1) {
			Event topEvent = priorityQueue.poll();
			long timeDifferenceInMinutes = (priorityQueue.peek().getStartTime().getEpochSecond()
					-topEvent.getEndTime().getEpochSecond())/60;
			if(timeDifferenceInMinutes>=durationInMinutes) {
				nextAvailableAt = topEvent.getEndTime();
				break;
			}
		}

		if(nextAvailableAt==null)
			nextAvailableAt = priorityQueue.poll().getEndTime();
		return nextAvailableAt;
	}

	@Override
	public EventBE createAssistedEvent(AssistedEvent assistedEvent) throws ErrorMessage {

		EventBE newEvent = null;

		if(assistedEvent!=null) {
			// get all the machines at the current location
			List<Machine> allMachines = machineDAO.findByLocationIdOrderByAvailableAtDesc(assistedEvent.getLocationId());
			Slot availableSlot = null;
			if(assistedEvent.getPreferences()!=null && allMachines!=null) {
				for(Preference preference:assistedEvent.getPreferences()) {
					//find any available slot at this preferred time
					availableSlot = getAvailableSlotFor(preference,allMachines,assistedEvent);
					if(availableSlot!=null)
						break;
				}
			}
			if(availableSlot!=null) 
				newEvent = buildAndCreateEvent(availableSlot, assistedEvent.getLocationId(),assistedEvent.getUserId());
			else if(assistedEvent.isIgnorePreference() && allMachines!=null && allMachines.size()>0) {
				newEvent = createDefaultEvent(allMachines,assistedEvent);
			}
		}

		return newEvent;
	}
	
	private EventBE createDefaultEvent(List<Machine> allMachines, AssistedEvent assistedEvent) {
		Machine allotedMachine = allMachines.get(allMachines.size()-1);//sorted in desc order by availableAt.
		Instant startsAt = allotedMachine.getAvailableAt();
		Instant endsAt = allotedMachine.getAvailableAt().plusSeconds(assistedEvent.getDuration()*60);
		
		Slot defaultSlot = new Slot();
		defaultSlot.setStartTime(startsAt);
		defaultSlot.setEndTime(endsAt);
		defaultSlot.setMachineId(allotedMachine.getMachineId());
		return buildAndCreateEvent(defaultSlot, assistedEvent.getLocationId(), assistedEvent.getUserId());
	}

	private EventBE buildAndCreateEvent(Slot availableSlot, long locationId, String userId) {
		Event event = new Event();
		event.setMachineId(availableSlot.getMachineId());
		event.setLocationId(locationId);
		event.setStartTime(availableSlot.getStartTime());
		event.setEndTime(availableSlot.getEndTime());
		event.setModifiedTime(Instant.now());
		event.setUserId(userId);
		event = eventDAO.save(event);
		Instant availableAt = getNextAvailableTime(event);
		machineService.updateMachineAvailability(event.getMachineId(),availableAt);
		return translate.toBE(event);
	}

	private Slot getAvailableSlotFor(Preference preference, List<Machine>allMachines, AssistedEvent assistedEvent) {

		List<Slot> slots = new ArrayList<Slot>();
		Slot allotedSlot = null;
		Instant preferredStartTime = Instant.ofEpochSecond(preference.getStartTime());
		Instant preferredEndTime = Instant.ofEpochSecond(preference.getEndTime());
		for(Machine machine:allMachines) {
			
			if(machine.getAvailableAt()==null) {
				//No event has been set on this machine, therefore user event can be created right away.
				Slot bestSlot = createSlot(preferredStartTime,assistedEvent.getDuration(),machine.getMachineId());
				//This candidate slot will also be the most optimal alloted slot.
				return bestSlot;
			}else if(machine.getAvailableAt().isBefore(preferredEndTime)) {
				//Event could be create on this machine, if slot of required duration is available
				Instant possibleStart = machine.getAvailableAt().isBefore(preferredStartTime)?
										preferredStartTime:machine.getAvailableAt();
				Instant startTimeOfSlot = getStartTimeOfFirstAvailableSlot(possibleStart, 
																	  preferredEndTime,
																	  machine.getMachineId(),
																	  assistedEvent);
				if(startTimeOfSlot!=null){
					//If no event exists between the possible start and end time,
					// then, this will serve as one of the candidate slot which can be allocated in future
					// if no better slot is available
					Slot candidateSlot = createSlot(startTimeOfSlot,assistedEvent.getDuration(),machine.getMachineId());
					slots.add(candidateSlot);
				}
			}
		}
		if(slots.size()>0) {
			//If there are candidate slots, sort by start time to choose the most optimal.
			Collections.sort(slots, new Slot.SlotComparator());
			allotedSlot = slots.get(0);
		}
		return allotedSlot;
	}

	private Slot createSlot(Instant startTimeOfSlot, long duration, long machineId) {
		Slot slot = new Slot();
		slot.setStartTime(startTimeOfSlot);
		slot.setEndTime(startTimeOfSlot.plusSeconds(duration*60));
		slot.setMachineId(machineId);
		return slot;
	}

	private Instant getStartTimeOfFirstAvailableSlot(Instant possibleStart, Instant endTime, long machineId,
			AssistedEvent assistedEvent) {
		List<Event> existingEvents = eventDAO.
									 findByLocationIdAndMachineIdAndEndTimeBetween(assistedEvent.getLocationId(), 
											 									  machineId, 
											 									  possibleStart, endTime);
		
		if(existingEvents.size()==0)
			return possibleStart;
		PriorityQueue<Event> eventsQueue = new PriorityQueue<Event>(new EventComparator());
		eventsQueue.addAll(existingEvents);
		long timeDifferenceInMinutes = (eventsQueue.peek().getStartTime().getEpochSecond()
				-possibleStart.getEpochSecond())/60;
		if(timeDifferenceInMinutes>=assistedEvent.getDuration())
			return possibleStart;
		return getStartTimeOfFirstAvailableSlot(eventsQueue, endTime,assistedEvent.getDuration());
		
	}

	private Instant getStartTimeOfFirstAvailableSlot(PriorityQueue<Event> eventsQueue, Instant endTime, long durationInMins) {
		Instant startTimeOfSlot = null;
		while(eventsQueue.size()>1) {
			Event topEvent = eventsQueue.poll();
			long timeDifferenceInMinutes = (eventsQueue.peek().getStartTime().getEpochSecond()
					-topEvent.getEndTime().getEpochSecond())/60;
			if(timeDifferenceInMinutes>=durationInMins) {
				startTimeOfSlot = topEvent.getEndTime();
				break;
			}
		}
		if(startTimeOfSlot!=null)
			return startTimeOfSlot;
		
		Event lastEvent = eventsQueue.poll();
		long timeDifferenceInMinutes = (endTime.getEpochSecond()
				-lastEvent.getEndTime().getEpochSecond())/60;
		
		if(timeDifferenceInMinutes>=durationInMins)
			 startTimeOfSlot = lastEvent.getEndTime();
		return startTimeOfSlot;
	}
//	public static void main(String[] args) {
//		Slot s1 = new Slot();
//		s1.setStartTime(Instant.now());
//		s1.setEndTime(Instant.now().plusSeconds(1800));
//		s1.setMachineId(1);
//
//		Slot s2 = new Slot();
//		s2.setStartTime(Instant.now().plusSeconds(1800));
//		s2.setEndTime(Instant.now().plusSeconds(3600));
//		s2.setMachineId(2);
//
//		Slot s3 = new Slot();
//		s3.setStartTime(Instant.now().plusSeconds(3600));
//		s3.setEndTime(Instant.now().plusSeconds(7200));
//		s3.setMachineId(3);
//
//		List<Slot> list = new ArrayList<Slot>();
//		list.add(s3);list.add(s1);list.add(s2);
//		Collections.sort(list,new Slot.SlotComparator());
//
//		for(Slot slot:list) {
//			System.out.println(slot.getMachineId() + " "+ slot.getStartTime() + slot.getEndTime());
//		}
//	}

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
