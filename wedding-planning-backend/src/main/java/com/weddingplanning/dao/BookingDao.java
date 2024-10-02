package com.weddingplanning.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.weddingplanning.entity.Booking;
import com.weddingplanning.entity.Event;
import com.weddingplanning.entity.User;

@Repository
public interface BookingDao extends JpaRepository<Booking, Integer> {

	List<Booking> findByCustomer(User customer);

	List<Booking> findByEvent(Event event);

	List<Booking> findByBookingIdContainingIgnoreCase(String bookingId);

	@Query("SELECT b FROM Booking b WHERE b.event.manager = :manager")
	List<Booking> findByManager(@Param("manager") User manager);

	List<Booking> findByEventAndBookedDateAndBookedTime(Event event, String bookedDate, String bookedTime);

}
