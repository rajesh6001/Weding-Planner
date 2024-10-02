package com.weddingplanning.dto;

import java.math.BigDecimal;

import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import com.weddingplanning.entity.Event;

import lombok.Data;

@Data
public class AddEventRequestDto {

	private int id;
	
	private int managerId;

	private String name;

	private String description;

	private String venueName;

	private String venueType;

	private String location;

	private BigDecimal ticketPrice;

	private MultipartFile image;

	private Integer categoryId;

	public static Event toEntity(AddEventRequestDto dto) {
		Event entity = new Event();
		BeanUtils.copyProperties(dto, entity, "image", "categoryId", "managerId");
		return entity;
	}

}
