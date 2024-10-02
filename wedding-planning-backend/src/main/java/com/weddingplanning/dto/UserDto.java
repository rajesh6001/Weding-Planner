package com.weddingplanning.dto;

import java.math.BigDecimal;

import org.springframework.beans.BeanUtils;

import com.weddingplanning.entity.Address;
import com.weddingplanning.entity.User;

import lombok.Data;

@Data
public class UserDto {
	
	private int id;

	private String firstName;

	private String lastName;

	private String emailId;

	private String phoneNo;

	private String role;

	private Address address;

	private UserDto seller;

	private String status;
	
	private BigDecimal wallet;
	
	public static UserDto toUserDtoEntity(User user) {
		UserDto userDto =new UserDto();
		BeanUtils.copyProperties(user, userDto, "seller");		
		return userDto;
	}

}
