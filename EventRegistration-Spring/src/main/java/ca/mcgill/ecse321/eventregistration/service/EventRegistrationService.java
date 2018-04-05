package ca.mcgill.ecse321.eventregistration.service;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.eventregistration.model.Event;
import ca.mcgill.ecse321.eventregistration.model.Participant;
import ca.mcgill.ecse321.eventregistration.model.Registration;
import ca.mcgill.ecse321.eventregistration.model.RegistrationManager;
import ca.mcgill.ecse321.eventregistration.persistence.PersistenceXStream;

@Service
public class EventRegistrationService {
	private RegistrationManager rm;

	public EventRegistrationService(RegistrationManager rm) {
		this.rm = rm;
	}

	private boolean checkIfEmptyOrNull(String name) {
		return name == null || name.trim().equals("");
	}

	public Participant createParticipant(String name) throws InvalidInputException {
		if (checkIfEmptyOrNull(name))
			throw new InvalidInputException("Participant name cannot be empty!");
		//check if participant name already exists
		for(Participant tmp: rm.getParticipants()) {
			if(tmp.getName().equals(name))
				throw new InvalidInputException("Participant name already exists");
		}
		
		Participant p = new Participant(name);
		
		rm.addParticipant(p);
		PersistenceXStream.saveToXMLwithXStream(rm);
		return p;
	}

	public Event createEvent(String name, Date date, Time startTime, Time endTime) throws InvalidInputException {
		if(name == null || date == null || startTime == null || endTime == null) 
			throw new InvalidInputException("Event name cannot be empty! Event date cannot be empty! Event start time cannot be empty! Event end time cannot be empty!");
		else if (name.trim().contentEquals(""))
			throw new InvalidInputException("Event name cannot be empty!");				
		else if (startTime.compareTo(endTime) > 0)
			throw new InvalidInputException("Event end time cannot be before event start time!");
		
		//check if event already exists
		for(Event tmp: rm.getEvents()) {
			if(tmp.getName().contentEquals(name))
				throw new InvalidInputException("Event name already exists");
		}
		
		Event e = new Event(name, date, startTime, endTime);
		rm.addEvent(e);
		PersistenceXStream.saveToXMLwithXStream(rm);

		return e;
	}

	public Registration register(Participant p, Event e) throws InvalidInputException {
		if (p == null || e == null)
			throw new InvalidInputException("Participant needs to be selected for registration! Event needs to be selected for registration!");
		else if(!checkIfParticipantExists(p.getName()) || !checkIfEventExists(e.getName())) {
			throw new InvalidInputException("Participant does not exist! Event does not exist!");			
		}
		
		//check if p has already registered for e
		for(Event tmp: getEventsForParticipant(p)) {
			if(tmp.getName().contentEquals(e.getName()))
				throw new InvalidInputException("Participant " + p.getName() + " has already registered for " + e.getName());
		}
		
		Registration r = new Registration(p, e);
		rm.addRegistration(r);
		PersistenceXStream.saveToXMLwithXStream(rm);

		return r;
	}
	
	private boolean checkIfParticipantExists(String name) {
		for(Participant p: rm.getParticipants())
			if(p.getName().contentEquals(name))
				return true;
		return false;
	}
	
	private boolean checkIfEventExists(String name) {
		for(Event e: rm.getEvents())
			if(e.getName().contentEquals(name))
				return true;
		return false;
	}
	
	public List<Event> findAllEvents() {
		return rm.getEvents();
	}

	public List<Participant> findAllParticipants() {
		return rm.getParticipants();
	}

	public List<Event> getEventsForParticipant(Participant p) {
		List<Event> events = new ArrayList<>();
		for (Registration r : rm.getRegistrations()) {
			if (r.getParticipant().getName().contentEquals(p.getName()))
				events.add(r.getEvent());
		}

		return events;
	}

	public Participant findParticipant(String name) throws InvalidInputException {
		Participant p = null;
		for(Participant tmp: rm.getParticipants()) {
			if(tmp.getName().contentEquals(name)) {
				p = tmp;
				break;
			}
		}
		
		if (p == null)
			throw new InvalidInputException("Participant was not found");
		return p;
	}

	public Event findEvent(String name) throws InvalidInputException {
		Event e = null;
		for(Event tmp: rm.getEvents()) {
			if(tmp.getName().contentEquals(name)) {
				e = tmp;
				break;
			}
		}
		
		if (e == null)
			throw new InvalidInputException("Event was not found");
		return e;
	}

}
