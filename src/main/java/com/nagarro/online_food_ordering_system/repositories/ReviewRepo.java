package com.nagarro.online_food_ordering_system.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nagarro.online_food_ordering_system.entities.Review;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Long>{

	List<Review> findByDishId(Long dishId);

	List<Review> findByDishIdAndUserId(Long dishId, Long userId);

}
