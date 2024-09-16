package com.nagarro.online_food_ordering_system.services.auth;

import com.nagarro.online_food_ordering_system.dtos.SignupRequest;
import com.nagarro.online_food_ordering_system.dtos.UserDto;

public interface AuthService {

	UserDto createUser(SignupRequest signupRequest);

}
