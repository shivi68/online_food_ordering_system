package com.nagarro.online_food_ordering_system.services.order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nagarro.online_food_ordering_system.dtos.OrderDto;
import com.nagarro.online_food_ordering_system.dtos.OrderItemDto;
import com.nagarro.online_food_ordering_system.entities.Dishes;
import com.nagarro.online_food_ordering_system.entities.Order;
import com.nagarro.online_food_ordering_system.entities.OrderItem;
import com.nagarro.online_food_ordering_system.entities.User;
import com.nagarro.online_food_ordering_system.repositories.DishesRepo;
import com.nagarro.online_food_ordering_system.repositories.OrderRepo;
import com.nagarro.online_food_ordering_system.repositories.UserRepo;

@Service
public class OrderServiceImpl implements OrderService {

    private final UserRepo userRepo;
    private final OrderRepo orderRepo;
    private final DishesRepo dishesRepo;
    
    public OrderServiceImpl(UserRepo userRepo, OrderRepo orderRepo, DishesRepo dishesRepo) {
        this.userRepo = userRepo;
        this.orderRepo = orderRepo;
        this.dishesRepo = dishesRepo;
    }

    //Place An Order
    @Override
    public OrderDto placeOrder(OrderDto orderDto) {
        Optional<User> optionalUser = userRepo.findById(orderDto.getCustomerId());
        if (!optionalUser.isPresent()) {
            throw new IllegalArgumentException("Customer not found for ID: " + orderDto.getCustomerId());
        }

        User user = optionalUser.get();

        // Validate delivery address
        String deliveryAddress = orderDto.getDeliveryAddress();
        if (deliveryAddress == null || deliveryAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Delivery address is not provided.");
        }

        Order order = new Order();
        order.setUser(user);
        order.setDeliveryAddress(deliveryAddress);
        order.setDeliveryStatus("Pending"); // Set initial delivery status
        order.setOrderStatus("Pending"); 

        double totalAmount = 0.0;

        for (OrderItemDto itemDto : orderDto.getItems()) {
            Optional<Dishes> optionalDish = dishesRepo.findById(itemDto.getDishId());
            if (!optionalDish.isPresent()) {
                throw new IllegalArgumentException("Dish not found for dish ID: " + itemDto.getDishId());
            }

            Dishes dish = optionalDish.get();
            OrderItem orderItem = new OrderItem();
            orderItem.setDish(dish);
            orderItem.setQuantity(itemDto.getQuantity());

            double price = dish.getPrice();
            orderItem.setPrice(price);

            order.addOrderItem(orderItem);

            totalAmount += price * itemDto.getQuantity();
            itemDto.setDishName(dish.getName());
        }

        order.setTotalAmount(totalAmount);
        order.setOrderDate(new Date()); 

        String orderNumber = generateOrderNumber(); 
        order.setOrderNumber(orderNumber); 

        Order savedOrder = orderRepo.save(order);

        // Update status to "Placed" after saving
        savedOrder.setOrderStatus("Placed");
        orderRepo.save(savedOrder);

        return convertToOrderDto(savedOrder, user.getName(),totalAmount);

    }
    
    @Override
    public List<OrderDto> getOrdersForCustomer(Long customerId) {
        List<Order> orders = orderRepo.findByUserId(customerId);
        return orders.stream()
            .map(order -> convertToOrderDto(order, order.getUser().getName(), order.getTotalAmount()))
            .collect(Collectors.toList());
    }
    
    @Override
    public Optional<OrderDto> getOrderById(Long orderId) {
        Optional<Order> optionalOrder = orderRepo.findById(orderId);
        return optionalOrder.map(order -> convertToOrderDto(order, order.getUser().getName(), order.getTotalAmount()));
    }

    @Override
    public Optional<OrderDto> getBillOfOrder(Long orderId) {
        return getOrderById(orderId);
    }

    // Method to generate a unique order number
    private String generateOrderNumber() {
        // Generates a unique order number based on the current timestamp
        return "ORD" + System.currentTimeMillis();
    }
    
    //Get order by ordernumber
    @Override
    public Optional<OrderDto> getOrderByOrderNumber(String orderNumber) {
        Optional<Order> optionalOrder = orderRepo.findByOrderNumber(orderNumber);
        return optionalOrder.map(order -> convertToOrderDto(order, order.getUser().getName(), order.getTotalAmount()));
    }

    private OrderDto convertToOrderDto(Order order, String customerName, double totalAmount) {
        OrderDto orderDto = new OrderDto();
       // orderDto.setId(order.getId());
        orderDto.setOrderId(order.getId()); 
        orderDto.setCustomerId(order.getUser().getId());
        orderDto.setCustomerName(customerName);
        orderDto.setDeliveryStatus(order.getDeliveryStatus());
        orderDto.setOrderStatus(order.getOrderStatus());
        orderDto.setTotalAmount(totalAmount);
        orderDto.setOrderNumber(order.getOrderNumber());
        orderDto.setDeliveryAddress(order.getDeliveryAddress());
        orderDto.setOrderDate(new SimpleDateFormat("yyyy-MM-dd").format(order.getOrderDate())); // Format date

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
