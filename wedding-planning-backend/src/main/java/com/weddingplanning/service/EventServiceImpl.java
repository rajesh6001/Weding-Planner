package com.weddingplanning.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weddingplanning.dao.EventDao;
import com.weddingplanning.entity.Category;
import com.weddingplanning.entity.Event;
import com.weddingplanning.entity.User;

@Service
public class EventServiceImpl implements EventService {

	@Autowired
	private EventDao eventDao;

	@Override
	public Event addEvent(Event event) {
		// TODO Auto-generated method stub
		return eventDao.save(event);
	}

	@Override
	public Event updateEvent(Event event) {
		// TODO Auto-generated method stub
		return eventDao.save(event);
	}

	@Override
	public Event getEventById(int eventId) {

		Optional<Event> optional = this.eventDao.findById(eventId);

		if (optional.isEmpty()) {
			return null;
		}
		return optional.get();

	}

	@Override
	public List<Event> getEventsByStatus(String status) {
		return this.eventDao.findByStatusOrderByIdDesc(status);
	}

	@Override
	public List<Event> getEventByStatusAndCategory(String status, Category category) {
		// TODO Auto-generated method stub
		return this.eventDao.findByStatusAndCategory(status, category);
	}

	@Override
	public List<Event> updateEvents(List<Event> events) {
		// TODO Auto-generated method stub
		return this.eventDao.saveAll(events);
	}

	@Override
	public List<Event> getEventByManager(User manager) {
		// TODO Auto-generated method stub
		return this.eventDao.findByManager(manager);
	}

	@Override
	public List<Event> getEventByStatusAndNameContainingIgnoreCase(String status, String name) {
		// TODO Auto-generated method stub
		return this.eventDao.findByStatusAndNameContainingIgnoreCase(status, name);
	}

}
