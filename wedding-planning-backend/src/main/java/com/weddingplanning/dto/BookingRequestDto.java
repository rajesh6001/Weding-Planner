package com.weddingplanning.dto;

import lombok.Data;

@Data
public class BookingRequestDto {

	private int eventId;

	private int customerId;

	private String bookedTime;

	private String bookedDate;

}
