package com.weddingplanning.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weddingplanning.dao.BookingDao;
import com.weddingplanning.entity.Booking;
import com.weddingplanning.entity.Event;
import com.weddingplanning.entity.User;

@Service
public class BookingServiceImpl implements BookingService {

	@Autowired
	private BookingDao bookingDao;

	@Override
	public Booking addBooking(Booking booking) {
		// TODO Auto-generated method stub
		return bookingDao.save(booking);
	}

	@Override
	public Booking updateBooking(Booking booking) {
		// TODO Auto-generated method stub
		return bookingDao.save(booking);
	}

	@Override
	public Booking getBookingById(int bookingId) {

		Optional<Booking> optional = this.bookingDao.findById(bookingId);

		if (optional.isEmpty()) {
			return null;
		}

		return optional.get();
	}

	@Override
	public List<Booking> getAllBookings() {
		// TODO Auto-generated method stub
		return this.bookingDao.findAll();
	}

	@Override
	public List<Booking> getBookingByEvent(Event event) {
		// TODO Auto-generated method stub
		return this.bookingDao.findByEvent(event);
	}

	@Override
	public List<Booking> getBookingByCustomer(User customer) {
		// TODO Auto-generated method stub
		return this.bookingDao.findByCustomer(customer);
	}

	@Override
	public List<Booking> getBookingsByBookingId(String bookingId) {
		// TODO Auto-generated method stub
		return this.bookingDao.findByBookingIdContainingIgnoreCase(bookingId);
	}

	@Override
	public List<Booking> getBookingByManager(User manager) {
		// TODO Auto-generated method stub
		return this.bookingDao.findByManager(manager);
	}

	@Override
	public List<Booking> getBookingsByEventAndBookedDateAndBookedTime(Event event, String bookedDate,
			String bookedTime) {
		// TODO Auto-generated method stub
		return this.bookingDao.findByEventAndBookedDateAndBookedTime(event, bookedDate, bookedTime);
	}

}
