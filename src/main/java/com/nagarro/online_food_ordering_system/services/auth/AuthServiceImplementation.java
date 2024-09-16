package com.nagarro.online_food_ordering_system.services.auth;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.nagarro.online_food_ordering_system.dtos.SignupRequest;
import com.nagarro.online_food_ordering_system.dtos.UserDto;
import com.nagarro.online_food_ordering_system.entities.User;
import com.nagarro.online_food_ordering_system.enums.UserRole;
import com.nagarro.online_food_ordering_system.repositories.UserRepo;
import jakarta.annotation.PostConstruct;

@Service
public class AuthServiceImplementation implements AuthService {
	
	@Autowired
	private final UserRepo userRepo;
	
	public AuthServiceImplementation(UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	//Creating ADMIN 
	@PostConstruct
	public void createAdminAccount() {
		User adminAccount = userRepo.findByUserRole(UserRole.ADMIN);
		if(adminAccount == null) {
			User user = new User();
			user.setName("admin");
			user.setEmail("admin@gmail.com");
			user.setPassword(new BCryptPasswordEncoder().encode("admin"));
			user.setUserRole(UserRole.ADMIN);
			userRepo.save(user);
		}
	}
	
	//Method to create User or Customer
	@Override
	public UserDto createUser(SignupRequest signupRequest) {
	    // Validate input
	    if (signupRequest.getEmail() == null || signupRequest.getEmail().isEmpty()) {
	        throw new IllegalArgumentException("Email cannot be blank");
	    }
	    if (signupRequest.getPassword() == null || signupRequest.getPassword().isEmpty()) {
	        throw new IllegalArgumentException("Password cannot be blank");
	    }
	    if (signupRequest.getName() == null || signupRequest.getName().isEmpty()) {
	        throw new IllegalArgumentException("Name cannot be blank");
	    }
	    
	    // Email format validation using regex
	    String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
	    Pattern pattern = Pattern.compile(emailRegex);
	    if (!pattern.matcher(signupRequest.getEmail()).matches()) {
	        throw new IllegalArgumentException("Please provide a valid email address");
	    }
	    
	    // Check if email already exists
	    if (userRepo.findByEmail(signupRequest.getEmail()).isPresent()) {
	        throw new IllegalArgumentException("Email already exists");
	    }

	    User user = new User();
	    user.setName(signupRequest.getName());
	    user.setEmail(signupRequest.getEmail());
	    user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
	    user.setUserRole(UserRole.CUSTOMER);
	    user.setAddress(signupRequest.getAddress());

	    User createdUser = userRepo.save(user);
	    UserDto createdUserDto = new UserDto();
	    createdUserDto.setId(createdUser.getId());
	    createdUserDto.setName(createdUser.getName());
	    createdUserDto.setEmail(createdUser.getEmail());
	    createdUserDto.setUserRole(createdUser.getUserRole());
	    createdUserDto.setAddress(createdUser.getAddress());
	    return createdUserDto;
	}
}
