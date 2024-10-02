package com.weddingplanning.service;

import java.util.List;

import com.weddingplanning.entity.Booking;
import com.weddingplanning.entity.Event;
import com.weddingplanning.entity.User;

public interface BookingService {

	Booking addBooking(Booking booking);

	Booking updateBooking(Booking booking);

	Booking getBookingById(int bookingId);

	List<Booking> getAllBookings();

	List<Booking> getBookingByEvent(Event event);

	List<Booking> getBookingByCustomer(User customer);

	List<Booking> getBookingByManager(User manager);

	List<Booking> getBookingsByBookingId(String bookingId);

	List<Booking> getBookingsByEventAndBookedDateAndBookedTime(Event event, String bookedDate, String bookedTime);

}
