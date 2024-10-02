package com.weddingplanning.resource;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;

import com.weddingplanning.dto.AddEventRequestDto;
import com.weddingplanning.dto.CommonApiResponse;
import com.weddingplanning.dto.EventResponseDto;
import com.weddingplanning.entity.Category;
import com.weddingplanning.entity.Event;
import com.weddingplanning.entity.User;
import com.weddingplanning.exception.EventSaveFailedException;
import com.weddingplanning.service.CategoryService;
import com.weddingplanning.service.EventService;
import com.weddingplanning.service.StorageService;
import com.weddingplanning.service.UserService;
import com.weddingplanning.utility.Constants.ActiveStatus;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class EventResource {

	private final Logger LOG = LoggerFactory.getLogger(EventResource.class);

	@Autowired
	private EventService eventService;

	@Autowired
	private StorageService storageService;

	@Autowired
	private UserService userService;

	@Autowired
	private CategoryService categoryService;

	public ResponseEntity<CommonApiResponse> addEvent(AddEventRequestDto request) {

		LOG.info("request received for Event add");

		CommonApiResponse response = new CommonApiResponse();

		if (request == null || request.getCategoryId() == 0) {
			response.setResponseMessage("missing input");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (request.getName() == null || request.getDescription() == null || request.getLocation() == null
				|| request.getVenueName() == null || request.getVenueType() == null || request.getManagerId() == 0) {
			response.setResponseMessage("bad request - missing input!!!");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		String addedDateTime = String
				.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
		Long addedTime = Long.valueOf(addedDateTime);

		Event event = AddEventRequestDto.toEntity(request);

		Category category = this.categoryService.getCategoryById(request.getCategoryId());

		if (category == null) {
			response.setResponseMessage("Event Category not found");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		User manager = this.userService.getUserById(request.getManagerId());

		// store event image in Image Folder and give event name to store in database
		String eventImageName = storageService.store(request.getImage());

		event.setImage(eventImageName);
		event.setCategory(category);
		event.setStatus(ActiveStatus.ACTIVE.value());
		event.setAddedDate(addedDateTime);
		event.setManager(manager);

		Event savedEvent = this.eventService.addEvent(event);

		if (savedEvent == null) {
			throw new EventSaveFailedException("Failed to save the Wedding Plan");
		}

		response.setResponseMessage("Wedding Plan added successful");
		response.setSuccess(true);

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);

	}

	public ResponseEntity<CommonApiResponse> updateEvent(AddEventRequestDto request) {

		LOG.info("request received for Event upate");

		CommonApiResponse response = new CommonApiResponse();

		if (request == null || request.getCategoryId() == 0) {
			response.setResponseMessage("missing input");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (request.getName() == null || request.getDescription() == null || request.getLocation() == null
				|| request.getVenueName() == null || request.getVenueType() == null) {
			response.setResponseMessage("bad request - missing input!!!");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		Event dbEvent = this.eventService.getEventById(request.getId());

		if (dbEvent == null) {
			response.setResponseMessage("Event not found!!!");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		Event event = AddEventRequestDto.toEntity(request);
		event.setId(dbEvent.getId());
		event.setCategory(dbEvent.getCategory());
		event.setAddedDate(dbEvent.getAddedDate());
		event.setManager(dbEvent.getManager());

		String currentTime = String
				.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

		Long updatedTime = Long.valueOf(currentTime);

		// store event image in Image Folder and give event name to store in database
		String eventImageName = storageService.store(request.getImage());

		event.setImage(eventImageName);

		// it will update the event category if changed
		if (event.getCategory().getId() != request.getCategoryId()) {
			Category category = this.categoryService.getCategoryById(request.getCategoryId());
			event.setCategory(category);
		}

		event.setStatus(ActiveStatus.ACTIVE.value());

		Event updatedEvent = this.eventService.updateEvent(event);

		if (updatedEvent == null) {
			throw new EventSaveFailedException("Failed to update the Weeding Plan");
		}

		response.setResponseMessage("Wedding Plan updated successful");
		response.setSuccess(true);

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);

	}

	public ResponseEntity<EventResponseDto> fetchActiveEvents() {

		String currentTime = String
				.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

		EventResponseDto response = new EventResponseDto();

		List<Event> events = this.eventService.getEventsByStatus(ActiveStatus.ACTIVE.value());

		if (CollectionUtils.isEmpty(events)) {
			response.setResponseMessage("Events not found");
			response.setSuccess(false);

			return new ResponseEntity<EventResponseDto>(response, HttpStatus.OK);
		}

		response.setEvents(events);
		response.setResponseMessage("Events fetched successful!!");
		response.setSuccess(true);

		return new ResponseEntity<EventResponseDto>(response, HttpStatus.OK);
	}

	public ResponseEntity<EventResponseDto> fetchAllEventsByStatus(String status) {

		EventResponseDto response = new EventResponseDto();

		if (status == null) {
			response.setResponseMessage("missing input!!!");
			response.setSuccess(false);

			return new ResponseEntity<EventResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<Event> events = this.eventService.getEventsByStatus(status);

		if (CollectionUtils.isEmpty(events)) {
			response.setResponseMessage("Events not found");
			response.setSuccess(false);

			return new ResponseEntity<EventResponseDto>(response, HttpStatus.OK);
		}

		response.setEvents(events);
		response.setResponseMessage("Events fetched successful!!");
		response.setSuccess(true);

		return new ResponseEntity<EventResponseDto>(response, HttpStatus.OK);
	}

	public ResponseEntity<EventResponseDto> fetchEventByEventId(Integer eventId) {

		EventResponseDto response = new EventResponseDto();

		if (eventId == null) {
			response.setResponseMessage("missing input!!!");
			response.setSuccess(false);

			return new ResponseEntity<EventResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		Event event = this.eventService.getEventById(eventId);

		if (event == null) {
			response.setResponseMessage("event not found!!!");
			response.setSuccess(false);

			return new ResponseEntity<EventResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		response.setEvents(Arrays.asList(event));
		response.setResponseMessage("Events fetched successful!!");
		response.setSuccess(true);

		return new ResponseEntity<EventResponseDto>(response, HttpStatus.OK);
	}

	public ResponseEntity<EventResponseDto> fetchActiveEventsByCategory(Integer categoryId) {

		EventResponseDto response = new EventResponseDto();

		if (categoryId == null) {
			response.setResponseMessage("missing input!!!");
			response.setSuccess(false);

			return new ResponseEntity<EventResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		Category category = this.categoryService.getCategoryById(categoryId);

		if (category == null) {
			response.setResponseMessage("category not found");
			response.setSuccess(false);

			return new ResponseEntity<EventResponseDto>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		String currentTime = String
				.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

		List<Event> events = this.eventService.getEventByStatusAndCategory(ActiveStatus.ACTIVE.value(), category);

		if (CollectionUtils.isEmpty(events)) {
			response.setResponseMessage("Events not found");
			response.setSuccess(false);

			return new ResponseEntity<EventResponseDto>(response, HttpStatus.OK);
		}

		response.setEvents(events);
		response.setResponseMessage("Events fetched successful!!");
		response.setSuccess(true);

		return new ResponseEntity<EventResponseDto>(response, HttpStatus.OK);
	}

	public ResponseEntity<EventResponseDto> searchActiveEventsByName(String eventName) {

		EventResponseDto response = new EventResponseDto();

		if (eventName == null) {
			response.setResponseMessage("missing input!!!");
			response.setSuccess(false);

			return new ResponseEntity<EventResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		String currentTime = String
				.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

		List<Event> events = this.eventService.getEventByStatusAndNameContainingIgnoreCase(ActiveStatus.ACTIVE.value(),
				eventName);

		if (CollectionUtils.isEmpty(events)) {
			response.setResponseMessage("Events not found");
			response.setSuccess(false);

			return new ResponseEntity<EventResponseDto>(response, HttpStatus.OK);
		}

		response.setEvents(events);
		response.setResponseMessage("Events fetched successful!!");
		response.setSuccess(true);

		return new ResponseEntity<EventResponseDto>(response, HttpStatus.OK);
	}

	public void fetchEventImage(String eventImageName, HttpServletResponse resp) {
		Resource resource = storageService.load(eventImageName);
		if (resource != null) {
			try (InputStream in = resource.getInputStream()) {
				ServletOutputStream out = resp.getOutputStream();
				FileCopyUtils.copy(in, out);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public ResponseEntity<CommonApiResponse> deleteEvent(Integer eventId) {

		CommonApiResponse response = new CommonApiResponse();

		if (eventId == null) {
			response.setResponseMessage("missing input!!!");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		Event event = this.eventService.getEventById(eventId);

		if (event == null) {
			response.setResponseMessage("event not found!!!");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		event.setStatus(ActiveStatus.DEACTIVATED.value());
		this.eventService.updateEvent(event);

		response.setResponseMessage("Events Deleted successful!!");
		response.setSuccess(true);

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
	}

	public ResponseEntity<EventResponseDto> fetchEventsByManager(Integer managerId) {

		EventResponseDto response = new EventResponseDto();

		if (managerId == null) {
			response.setResponseMessage("missing input!!!");
			response.setSuccess(false);

			return new ResponseEntity<EventResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		User manager = this.userService.getUserById(managerId);

		if (manager == null) {
			response.setResponseMessage("Manager not found");
			response.setSuccess(false);

			return new ResponseEntity<EventResponseDto>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		List<Event> events = this.eventService.getEventByManager(manager);

		if (CollectionUtils.isEmpty(events)) {
			response.setResponseMessage("Events not found");
			response.setSuccess(false);

			return new ResponseEntity<EventResponseDto>(response, HttpStatus.OK);
		}

		response.setEvents(events);
		response.setResponseMessage("Events fetched successful!!");
		response.setSuccess(true);

		return new ResponseEntity<EventResponseDto>(response, HttpStatus.OK);
	}

}
