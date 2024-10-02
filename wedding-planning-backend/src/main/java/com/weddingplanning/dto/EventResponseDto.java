package com.weddingplanning.dto;

import java.util.ArrayList;
import java.util.List;

import com.weddingplanning.entity.Event;

import lombok.Data;

@Data
public class EventResponseDto extends CommonApiResponse {

	private List<Event> events = new ArrayList();

}
