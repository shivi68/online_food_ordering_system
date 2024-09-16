package com.nagarro.online_food_ordering_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nagarro.online_food_ordering_system.entities.OrderItem;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, Long>{

}
