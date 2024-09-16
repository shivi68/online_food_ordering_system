package com.nagarro.online_food_ordering_system.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nagarro.online_food_ordering_system.entities.Rating;

@Repository
public interface RatingRepo extends JpaRepository<Rating, Long>{

	List<Rating> findByDishId(Long dishId);

	List<Rating> findByDishIdAndUserId(Long dishId, Long userId);

}
