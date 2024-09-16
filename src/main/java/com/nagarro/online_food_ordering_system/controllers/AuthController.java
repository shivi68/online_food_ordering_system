package com.nagarro.online_food_ordering_system.controllers;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nagarro.online_food_ordering_system.dtos.AuthenticationRequest;
import com.nagarro.online_food_ordering_system.dtos.AuthenticationResponse;
import com.nagarro.online_food_ordering_system.dtos.SignupRequest;
import com.nagarro.online_food_ordering_system.dtos.UserDto;
import com.nagarro.online_food_ordering_system.entities.User;
import com.nagarro.online_food_ordering_system.enums.UserRole;
import com.nagarro.online_food_ordering_system.repositories.UserRepo;
import com.nagarro.online_food_ordering_system.services.auth.AuthService;
import com.nagarro.online_food_ordering_system.services.auth.util.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	  @Autowired
	  private final AuthService authService;
	
	  @Autowired
	  private final AuthenticationManager authenticationManager;

	  @Autowired
	  private final UserDetailsService userDetailsService;
	
	  @Autowired
	  private final JwtUtil jwtUtil;
	
	  @Autowired
	  private final UserRepo userRepo;
	

	 public AuthController(AuthService authService, AuthenticationManager authenticationManager,
	                          UserDetailsService userDetailsService, JwtUtil jwtUtil, UserRepo userRepo) {
	        this.authService = authService;
	        this.authenticationManager = authenticationManager;
	        this.userDetailsService = userDetailsService;
	        this.jwtUtil = jwtUtil;
	        this.userRepo = userRepo;
	    }
	 
	 //API for Customer Signup
	 @PostMapping("/signup")
	 public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest) {
	     try {
	         UserDto createdUserDto = authService.createUser(signupRequest);
	         return new ResponseEntity<>(createdUserDto, HttpStatus.CREATED);
	     } catch (IllegalArgumentException e) {
	         return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
	     }
	 }
	 
	 //API for Customer and Admin Login
	@PostMapping("/login")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
	    // Validate input fields
	    if (authenticationRequest.getEmail() == null || authenticationRequest.getEmail().isBlank()) {
	        return new ResponseEntity<>(Map.of("error", "Email cannot be blank"), HttpStatus.BAD_REQUEST);
	    }
	    if (authenticationRequest.getPassword() == null || authenticationRequest.getPassword().isBlank()) {
	        return new ResponseEntity<>(Map.of("error", "Password cannot be blank"), HttpStatus.BAD_REQUEST);
	    }

	    try {
	        authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
	        );
	    } catch (BadCredentialsException e) {
	        return new ResponseEntity<>(Map.of("error", "Incorrect username or password"), HttpStatus.UNAUTHORIZED);
	    } catch (Exception e) {
	        return new ResponseEntity<>(Map.of("error", "Authentication failed"), HttpStatus.INTERNAL_SERVER_ERROR);
	    }

	    final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
	    final String jwt = jwtUtil.generateToken(userDetails);
	    Optional<User> optionalUser = userRepo.findFirstByEmail(userDetails.getUsername());
	    
	    // Check if user is present
	    if (optionalUser.isPresent()) {
	        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
	        authenticationResponse.setJwt(jwt);
	        authenticationResponse.setUserRole(optionalUser.get().getUserRole());
	        authenticationResponse.setUserId(optionalUser.get().getId());
	        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>(Map.of("error", "User account does not exist"), HttpStatus.NOT_FOUND);
	    }
	}
	 
	 //Only Customer is allowed to update his own details and admin is restricted 
	 //API to update Customer Details By Id
	 @PutMapping("/user/{userId}")
	 public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody UserDto userDto, Authentication authentication) {
	     // Check if Authentication object is null
	     if (authentication == null || !authentication.isAuthenticated()) {
	         return new ResponseEntity<>(Map.of("error", "Authentication token is required"), HttpStatus.UNAUTHORIZED);
	     }

	     
	     UserDetails currentUserDetails = (UserDetails) authentication.getPrincipal();
	     Optional<User> currentUserOptional = userRepo.findByEmail(currentUserDetails.getUsername());

	     if (currentUserOptional.isPresent()) {
	         User currentUser = currentUserOptional.get();
	         Optional<User> userOptional = userRepo.findById(userId);

	         if (userOptional.isPresent()) {
	             User user = userOptional.get();

	       
	             if (user.getUserRole() != UserRole.CUSTOMER) {
	                 return new ResponseEntity<>(Map.of("error", "Only users with CUSTOMER role can be updated"), HttpStatus.FORBIDDEN);
	             }

	             //Only the customer can update their own details
	             if (!currentUser.getId().equals(userId)) {
	                 return new ResponseEntity<>(Map.of("error", "Only the customer can update their own details"), HttpStatus.FORBIDDEN);
	             }

	             if (userDto.getName() != null && userDto.getName().isBlank()) {
	                 return new ResponseEntity<>(Map.of("error", "Name cannot be blank"), HttpStatus.BAD_REQUEST);
	             }
	             if (userDto.getEmail() != null && userDto.getEmail().isBlank()) {
	                 return new ResponseEntity<>(Map.of("error", "Email cannot be blank"), HttpStatus.BAD_REQUEST);
	             }
	            
	                String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
	                Pattern pattern = Pattern.compile(emailRegex);
	                if (!pattern.matcher(userDto.getEmail()).matches()) {
	                    return new ResponseEntity<>(Map.of("error", "Invalid email format"), HttpStatus.BAD_REQUEST);
	                }
	             if (userDto.getAddress() != null && userDto.getAddress().isBlank()) {
	                 return new ResponseEntity<>(Map.of("error", "Address cannot be blank"), HttpStatus.BAD_REQUEST);
	             }
	             if (userDto.getPassword() != null && userDto.getPassword().isBlank()) {
	                 return new ResponseEntity<>(Map.of("error", "Password cannot be blank"), HttpStatus.BAD_REQUEST);
	             }

	            
	             if (userDto.getEmail() != null && !userDto.getEmail().equals(user.getEmail())) {
	                 Optional<User> existingUser = userRepo.findByEmail(userDto.getEmail());
	                 if (existingUser.isPresent()) {
	                     return new ResponseEntity<>(Map.of("error", "Email already exists"), HttpStatus.BAD_REQUEST);
	                 }
	                 user.setEmail(userDto.getEmail());
	             }
	             if (userDto.getName() != null) {
	                 user.setName(userDto.getName());
	             }
	             if (userDto.getAddress() != null) {
	                 user.setAddress(userDto.getAddress());
	             }
	             if (userDto.getPassword() != null) {
	                 user.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
	             }

	             userRepo.save(user);

	             // Create a new UserDto to return
	             UserDto updatedUserDto = new UserDto();
	             updatedUserDto.setId(user.getId());
	             updatedUserDto.setName(user.getName());
	             updatedUserDto.setEmail(user.getEmail());
	             updatedUserDto.setAddress(user.getAddress());
	             updatedUserDto.setUserRole(user.getUserRole());

	             return new ResponseEntity<>(updatedUserDto, HttpStatus.OK);
	         } else {
	             return new ResponseEntity<>(Map.of("error", "User not found"), HttpStatus.NOT_FOUND);
	         }
	     } else {
	         return new ResponseEntity<>(Map.of("error", "Current user not found"), HttpStatus.NOT_FOUND);
	     }
	 }
	 
	  @DeleteMapping("/user/{userId}")
	    public ResponseEntity<?> deleteUser(@PathVariable Long userId, Authentication authentication) {
	        // Check if Authentication object is null
	        if (authentication == null || !authentication.isAuthenticated()) {
	            return new ResponseEntity<>(Map.of("error", "Authentication token is required"), HttpStatus.UNAUTHORIZED);
	        }

	       
	        UserDetails currentUserDetails = (UserDetails) authentication.getPrincipal();
	        Optional<User> currentUserOptional = userRepo.findByEmail(currentUserDetails.getUsername());

	        if (currentUserOptional.isPresent()) {
	            User currentUser = currentUserOptional.get();
	            Optional<User> userOptional = userRepo.findById(userId);

	            if (userOptional.isPresent()) {
	                User user = userOptional.get();

	                if (!currentUser.getId().equals(userId)) {
	                    return new ResponseEntity<>(Map.of("error", "Only the authenticated user can delete their own account"), HttpStatus.FORBIDDEN);
	                }

	               
	                userRepo.deleteById(userId);
	               // return new ResponseEntity<>(Map.of("message","User deleted successfully!!"), HttpStatus.NO_CONTENT);
	                return ResponseEntity.ok(Map.of("message", "User with ID " + userId + " is deleted successfully!!"));
	            } else {
	                return new ResponseEntity<>(Map.of("error", "User with id " + userId + " not found!!"), HttpStatus.NOT_FOUND);
	            }
	        } else {
	            return new ResponseEntity<>(Map.of("error","Current user not found"), HttpStatus.NOT_FOUND);
	        }
	    }
	 
}
	

