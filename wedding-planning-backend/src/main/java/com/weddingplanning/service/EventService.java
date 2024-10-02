package com.weddingplanning.service;

import java.util.List;

import com.weddingplanning.entity.Category;
import com.weddingplanning.entity.Event;
import com.weddingplanning.entity.User;

public interface EventService {

	Event addEvent(Event event);

	Event updateEvent(Event event);

	Event getEventById(int eventId);

	List<Event> getEventsByStatus(String status);

	List<Event> getEventByStatusAndNameContainingIgnoreCase(String status, String name);

	List<Event> getEventByStatusAndCategory(String status, Category category);

	List<Event> updateEvents(List<Event> events);

	List<Event> getEventByManager(User manager);

}
