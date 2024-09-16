package com.nagarro.online_food_ordering_system.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nagarro.online_food_ordering_system.dtos.OrderDto;
import com.nagarro.online_food_ordering_system.dtos.OrderItemDto;
import com.nagarro.online_food_ordering_system.entities.Dishes;
import com.nagarro.online_food_ordering_system.entities.User;
import com.nagarro.online_food_ordering_system.enums.UserRole;
import com.nagarro.online_food_ordering_system.repositories.DishesRepo;
import com.nagarro.online_food_ordering_system.repositories.UserRepo;
import com.nagarro.online_food_ordering_system.services.order.OrderService;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	private final OrderService orderService;
	private final DishesRepo dishesRepo;
	private final UserRepo userRepo;
	
	 public OrderController(OrderService orderService, DishesRepo dishesRepo, UserRepo userRepo) {
		 this.orderService = orderService;
		 this.dishesRepo = dishesRepo;
		 this.userRepo = userRepo;
	 }


	 //API to place an Order
	 @PostMapping("/order")
	 @PreAuthorize("hasAuthority('CUSTOMER')")
	 public ResponseEntity<?> placeOrder(@RequestBody OrderDto orderDto) {
	     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	     String email = authentication.getName(); 
	     
	     Optional<User> authenticatedUserOpt = userRepo.findByEmail(email);
	     if (!authenticatedUserOpt.isPresent()) {
	         return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                              .body(Map.of("error", "Authenticated user not found"));
	     }
	     
	     User authenticatedUser = authenticatedUserOpt.get();
	     Long authenticatedCustomerId = authenticatedUser.getId();
	     
	     if (orderDto.getCustomerId() == null) {
	         return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                              .body(Map.of("error", "Customer ID is required and cannot be null"));
	     }

	     if (!orderDto.getCustomerId().equals(authenticatedCustomerId)) {
	         return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                              .body(Map.of("error", "You are not authorized to place an order for this customer"));
	     }

	     if (orderDto.getItems() == null || orderDto.getItems().isEmpty()) {
	         return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                              .body(Map.of("error", "Order must contain at least one item"));
	     }

	     for (OrderItemDto item : orderDto.getItems()) {
	         if (item.getDishId() == null) {
	             return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                                  .body(Map.of("error", "Dish ID is required and cannot be null for each item"));
	         }

	         if (item.getQuantity() <= 0) {
	             return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                                  .body(Map.of("error", "Quantity must be greater than zero for each item"));
	         }

	         Optional<Dishes> dishOpt = dishesRepo.findById(item.getDishId());
	         if (!dishOpt.isPresent() || !dishOpt.get().isAvailability()) {
	             return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                                  .body(Map.of("error", "Cannot place order for dish ID " + item.getDishId() +
	                                       " as it is not available right now"));
	         }
	     }
	     try {
	         OrderDto placedOrder = orderService.placeOrder(orderDto);
	         if (placedOrder == null) {
	             return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                                  .body(Map.of("error", "Failed to place the order. Please check the provided details."));
	         }
	         return ResponseEntity.status(HttpStatus.CREATED).body(placedOrder);
	     } catch (IllegalArgumentException ex) {
	         return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                              .body(Map.of("error", ex.getMessage()));
	     } catch (Exception ex) {
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                              .body(Map.of("error", "An unexpected error occurred: " + ex.getMessage()));
	     }
	 }

	 //API to get Orders For Customer
	 @GetMapping("/customer/{customerId}")
	 @PreAuthorize("hasAuthority('ADMIN')")
	 public ResponseEntity<?> getOrdersForCustomer(@PathVariable Long customerId) {
	     Optional<User> customerOpt = userRepo.findById(customerId);
	     
	     if (!customerOpt.isPresent()) {
	         return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                              .body(Map.of("error", "No customer found with ID " + customerId));
	     }
	     
	     List<OrderDto> orders = orderService.getOrdersForCustomer(customerId);
	     
	     if (orders.isEmpty()) {
	         return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                              .body(Map.of("message", "No orders found for customer with ID " + customerId));
	     }
	     return ResponseEntity.ok(orders);
	 }
	  
	 //API to get Details of Order By Order Id
	 @GetMapping("/details/{orderId}")
	 public ResponseEntity<?> getOrderById(@PathVariable Long orderId) {
	     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	     String username = authentication.getName();

	     Optional<User> authenticatedUserOpt = userRepo.findByEmail(username);
	     if (!authenticatedUserOpt.isPresent()) {
	         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized"));
	     }
	     User authenticatedUser = authenticatedUserOpt.get();

	     Optional<OrderDto> orderDto = orderService.getOrderById(orderId);
	     
	     if (!orderDto.isPresent()) {
	         return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                              .body(Map.of("error", "No order found with orderId " + orderId));
	     }
	     
	     boolean isAdmin = authenticatedUser.getUserRole() == UserRole.ADMIN;
	     boolean isOwner = orderDto.get().getCustomerId().equals(authenticatedUser.getId());

	     if (isAdmin || isOwner) {
	         return ResponseEntity.ok(orderDto.get());
	     } else {
	         return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                              .body(Map.of("error", "You are providing the wrong order ID"));
	     }
	 }

	 //API to get bill BY OrderId
	 @GetMapping("/bill/{orderId}")
	 public ResponseEntity<?> getBillOfOrder(@PathVariable Long orderId) {
	 
	     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	     String username = authentication.getName(); 

	     Optional<User> userOpt = userRepo.findByEmail(username);
	     if (!userOpt.isPresent()) {
	         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not found"));
	     }

	     User authenticatedUser = userOpt.get();
	     UserRole userRole = authenticatedUser.getUserRole();
	     Optional<OrderDto> orderDtoOpt = orderService.getBillOfOrder(orderId);

	     if (!orderDtoOpt.isPresent()) {
	         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Order ID not found"));
	     }

	     OrderDto orderDto = orderDtoOpt.get();
	     if (userRole == UserRole.ADMIN || orderDto.getCustomerId().equals(authenticatedUser.getId())) {
	         return ResponseEntity.ok(orderDto);
	     } else {
	         return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "You are not authorized to view this bill. Provide the Order Id of your own."));
	     }
	 }
 
	 //API to get Order Details BY Order Number
	 @GetMapping("/{orderNumber}")
	 public ResponseEntity<?> getOrderByOrderNumber(@PathVariable String orderNumber) {
	     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	     String username = authentication.getName();

	     Optional<User> authenticatedUserOpt = userRepo.findByEmail(username);
	     if (!authenticatedUserOpt.isPresent()) {
	         return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                              .body(Map.of("error", "Authenticated user not found"));
	     }
	     
	     User authenticatedUser = authenticatedUserOpt.get();
	     Long authenticatedUserId = authenticatedUser.getId();
	     Optional<OrderDto> orderDtoOpt = orderService.getOrderByOrderNumber(orderNumber);

	     if (orderDtoOpt.isPresent()) {
	         OrderDto orderDto = orderDtoOpt.get();
	         if (authenticatedUser.getUserRole() == UserRole.ADMIN || 
	             orderDto.getCustomerId().equals(authenticatedUserId)) {
	             return ResponseEntity.ok(orderDto);
	         } else {
	             return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                                  .body(Map.of("error", "You are not authorized to view this order details"));
	         }
	     } else {
	         return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                              .body(Map.of("error", "Order with order number " + orderNumber + " not found"));
	     }
	 }
}
