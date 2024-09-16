package com.nagarro.online_food_ordering_system.services.customer;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.nagarro.online_food_ordering_system.dtos.OrderDto;
import com.nagarro.online_food_ordering_system.dtos.OrderItemDto;
import com.nagarro.online_food_ordering_system.dtos.UserDto;
import com.nagarro.online_food_ordering_system.entities.Order;
import com.nagarro.online_food_ordering_system.entities.User;
import com.nagarro.online_food_ordering_system.repositories.CategoryRepo;
import com.nagarro.online_food_ordering_system.repositories.DishesRepo;
import com.nagarro.online_food_ordering_system.repositories.OrderRepo;
import com.nagarro.online_food_ordering_system.repositories.UserRepo;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final UserRepo userRepo;
    private final OrderRepo orderRepo;

    public CustomerServiceImpl(UserRepo userRepo, OrderRepo orderRepo) {
        this.userRepo = userRepo;
        this.orderRepo = orderRepo;
    }

    @Override
    public UserDto getCustomerDetailsWithOrders(Long userId) {
        Optional<User> optionalUser = userRepo.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Convert Orders to OrderDtos using convertToOrderDto method
            List<OrderDto> orderDtos = orderRepo.findByUserId(userId).stream()
                .map(order -> convertToOrderDto(order, user.getName(), order.getTotalAmount()))
                .collect(Collectors.toList());

            // Create and return UserDto
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setName(user.getName());
            userDto.setEmail(user.getEmail());
            userDto.setAddress(user.getAddress());
            userDto.setOrders(orderDtos);

            return userDto;
        }
        return null;
    }

    private OrderDto convertToOrderDto(Order order, String customerName, double totalAmount) {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(order.getId());
        orderDto.setCustomerId(order.getUser().getId());
        orderDto.setCustomerName(customerName);
        orderDto.setDeliveryStatus(order.getDeliveryStatus());
        orderDto.setOrderStatus(order.getOrderStatus());
        orderDto.setTotalAmount(totalAmount);
        orderDto.setOrderNumber(order.getOrderNumber());
        orderDto.setDeliveryAddress(order.getDeliveryAddress());
        orderDto.setOrderDate(new SimpleDateFormat("yyyy-MM-dd").format(order.getOrderDate()));

        // Map OrderItem entities to OrderItemDto
        List<OrderItemDto> orderItemDtos = order.getOrderItems().stream().map(orderItem -> {
            OrderItemDto orderItemDto = new OrderItemDto();
            orderItemDto.setDishId(orderItem.getDish().getId());
            orderItemDto.setDishName(orderItem.getDish().getName());
            orderItemDto.setQuantity(orderItem.getQuantity());
            orderItemDto.setPrice(orderItem.getPrice());
            return orderItemDto;
        }).collect(Collectors.toList());

        orderDto.setItems(orderItemDtos);

        return orderDto;
    }
}

