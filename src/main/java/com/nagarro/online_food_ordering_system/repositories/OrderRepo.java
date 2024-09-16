package com.nagarro.online_food_ordering_system.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nagarro.online_food_ordering_system.entities.Order;


@Repository
public interface OrderRepo extends JpaRepository<Order,Long> {

	  @Query("SELECT o FROM Order o WHERE o.id = :orderId AND o.user.id = :userId")
	  Optional<Order> findByIdAndUserId(@Param("orderId") Long orderId, @Param("userId") Long userId);

	  List<Order> findByUserId(Long customerId);

	  Optional<Order> findByOrderNumber(String orderNumber);

	  @Query("SELECT COUNT(o) FROM Order o JOIN o.orderItems oi WHERE oi.dish.id = :dishId")
	   int countOrdersByDishId(@Param("dishId") Long dishId);

}
