package com.nagarro.online_food_ordering_system.services.order;

import java.util.List;
import java.util.Optional;

import com.nagarro.online_food_ordering_system.dtos.OrderDto;

public interface OrderService {
	
	OrderDto placeOrder(OrderDto orderDto);

	List<OrderDto> getOrdersForCustomer(Long customerId);

	Optional<OrderDto> getOrderById(Long orderId);

	Optional<OrderDto> getBillOfOrder(Long orderId);

	Optional<OrderDto> getOrderByOrderNumber(String orderNumber);

}
