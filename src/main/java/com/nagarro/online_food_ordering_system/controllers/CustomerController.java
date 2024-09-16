package com.nagarro.online_food_ordering_system.controllers;

import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nagarro.online_food_ordering_system.dtos.UserDto;
import com.nagarro.online_food_ordering_system.entities.User;
import com.nagarro.online_food_ordering_system.enums.UserRole;
import com.nagarro.online_food_ordering_system.repositories.UserRepo;
import com.nagarro.online_food_ordering_system.services.customer.CustomerService;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

	 private final CustomerService customerService;
	 private final UserRepo userRepo;
	
	 public CustomerController(CustomerService customerService, UserRepo userRepo) {
	        this.customerService = customerService;
	        this.userRepo = userRepo;
	
	 }
	 
	 @GetMapping("/{userId}")
	 public ResponseEntity<?> getCustomerDetailsWithOrders(@PathVariable Long userId, Authentication authentication) {
	     if (authentication == null || !authentication.isAuthenticated()) {
	         return new ResponseEntity<>(Map.of("error", "Authentication token is required"), HttpStatus.UNAUTHORIZED);
	     }
	     UserDetails currentUserDetails = (UserDetails) authentication.getPrincipal();
	     Optional<User> currentUserOptional = userRepo.findByEmail(currentUserDetails.getUsername());

	     if (currentUserOptional.isEmpty()) {
	         return new ResponseEntity<>(Map.of("error", "Current user not found"), HttpStatus.NOT_FOUND);
	     }

	     User currentUser = currentUserOptional.get();
	     if (currentUser.getUserRole() == UserRole.ADMIN) {
	         UserDto userDto = customerService.getCustomerDetailsWithOrders(userId);
	         if (userDto != null) {
	             return new ResponseEntity<>(userDto, HttpStatus.OK);
	         } else {
	             return new ResponseEntity<>(Map.of("error", "User not found"), HttpStatus.NOT_FOUND);
	         }
	     }
	     if (currentUser.getUserRole() == UserRole.CUSTOMER && !currentUser.getId().equals(userId)) {
	         return new ResponseEntity<>(Map.of("error", "Access denied. You can only view your own details."), HttpStatus.FORBIDDEN);
	     }
	     UserDto userDto = customerService.getCustomerDetailsWithOrders(userId);
	     if (userDto != null) {
	         return new ResponseEntity<>(userDto, HttpStatus.OK);
	     } else {
	         return new ResponseEntity<>(Map.of("error", "User not found"), HttpStatus.NOT_FOUND);
	     }
	 }
}
