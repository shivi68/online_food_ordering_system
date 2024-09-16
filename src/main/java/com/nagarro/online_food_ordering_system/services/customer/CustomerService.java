package com.nagarro.online_food_ordering_system.services.customer;

import java.util.List;

import com.nagarro.online_food_ordering_system.dtos.CategoryDto;
import com.nagarro.online_food_ordering_system.dtos.UserDto;

public interface CustomerService {

//	List<CategoryDto> getAllCategories();
//
//	List<CategoryDto> getCategoriesByName(String title);

	UserDto getCustomerDetailsWithOrders(Long userId);

}
