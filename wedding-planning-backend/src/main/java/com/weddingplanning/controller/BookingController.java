package com.weddingplanning.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weddingplanning.dto.BookingRequestDto;
import com.weddingplanning.dto.BookingResponseDto;
import com.weddingplanning.dto.CommonApiResponse;
import com.weddingplanning.dto.OrderRazorPayResponse;
import com.weddingplanning.pg.RazorPayPaymentResponse;
import com.weddingplanning.resource.BookingResource;
import com.razorpay.RazorpayException;


@RestController
@RequestMapping("api/booking")
@CrossOrigin(origins = "http://localhost:3000")
public class BookingController {

	@Autowired
	private BookingResource bookingResource;

//	@PostMapping("add")
//	public ResponseEntity<CommonApiResponse> addEvent(@RequestBody BookingRequestDto request) {
//		return this.bookingResource.addBooking(request);
//	}

	@GetMapping("fetch/all")
	public ResponseEntity<BookingResponseDto> fetchAllBookings() {
		return this.bookingResource.fetchAllBookings();
	}

	@GetMapping("fetch/event-wise")
	public ResponseEntity<BookingResponseDto> fetchAllBookingsByEvent(@RequestParam("eventId") Integer eventId) {
		return this.bookingResource.fetchAllBookingsByEvent(eventId);
	}

	@GetMapping("fetch/customer-wise")
	public ResponseEntity<BookingResponseDto> fetchAllBookingsByCustomer(
			@RequestParam("customerId") Integer customerId) {
		return this.bookingResource.fetchAllBookingsByCustomer(customerId);
	}
	
	@GetMapping("fetch/manager-wise")
	public ResponseEntity<BookingResponseDto> fetchAllBookingsByManager(
			@RequestParam("managerId") Integer managerId) {
		return this.bookingResource.fetchAllBookingsByManager(managerId);
	}
	
	@PutMapping("order/create")
	public ResponseEntity<OrderRazorPayResponse> createRazorPayOrder(@RequestBody BookingRequestDto request)
			throws RazorpayException {
		return this.bookingResource.createRazorPayOrder(request);
	}
	
	@PutMapping("razorpPay/response")
	public ResponseEntity<CommonApiResponse> updateUserWallet(@RequestBody RazorPayPaymentResponse razorPayResponse)
			throws RazorpayException {
		return this.bookingResource.handleRazorPayPaymentResponse(razorPayResponse);
	}

}
