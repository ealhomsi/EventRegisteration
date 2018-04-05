package ca.mcgill.ecse321.eventregistration.dto;

import java.sql.Date;
import java.sql.Time;

import ca.mcgill.ecse321.eventregistration.model.Event;

public class EventDto {
	private String name;
	private Date eventDate;
	private Time startTime;
	private Time endTime;

	public EventDto() {

	}

	public EventDto(String name) {
		this(name, Date.valueOf("1971-01-01"), Time.valueOf("00:00:00"), Time.valueOf("23:59:59"));
	}

	public EventDto(String name, Date eventDate, Time startTime, Time endTime) {
		this.name = name;
		this.eventDate = eventDate;
		this.startTime = startTime;
		this.endTime = endTime;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

}
