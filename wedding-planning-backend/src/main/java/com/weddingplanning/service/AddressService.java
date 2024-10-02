package com.weddingplanning.service;

import java.util.List;

import com.weddingplanning.entity.Address;
import com.weddingplanning.entity.User;

public interface AddressService {
	
	Address addAddress(Address address);
	
	Address updateAddress(Address address);
	
	Address getAddressById(int addressId);

}
