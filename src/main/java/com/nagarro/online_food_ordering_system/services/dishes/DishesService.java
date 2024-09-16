package com.nagarro.online_food_ordering_system.services.dishes;

import java.util.List;

import com.nagarro.online_food_ordering_system.dtos.DishesDto;
import com.nagarro.online_food_ordering_system.dtos.RatingDto;
import com.nagarro.online_food_ordering_system.dtos.ReviewDto;

public interface DishesService {
	
	 DishesDto postDishes(Long categoryId, DishesDto dishesDto);
	 
	 DishesDto getDishById(Long dishesId);
	 
	 List<DishesDto> getAllDishesByCategory(Long categoryId);
	 
	 List<DishesDto> getDishesByCategoryAndTitle(Long categoryId, String title);
	 
	 DishesDto updateDishes(Long dishesId, DishesDto dishesDto);
	 
	 boolean deleteDishes(Long dishesId);

	 void addReview(Long userId, Long dishId, String review);
	 
	 void addRating(Long userId, Long dishId, int rating);
	 
	 void updateAverageRating(Long dishId);
	 
	 List<ReviewDto> getReviewsByDishId(Long dishId);
	 
	 List<RatingDto> getRatingsByDishId(Long dishId);
	 
	 List<DishesDto> getDishesSortedByRating();
	 
	 List<DishesDto> getAllDishesSortedByOrderCount();
	 
	 List<ReviewDto> getReviewsByDishIdForUser(Long dishId, Long authenticatedUserId);
	 
	 boolean hasOrderedDish(Long authenticatedUserId, Long dishId);
	 
	  List<RatingDto> getRatingsByDishIdForUser(Long dishId, Long authenticatedUserId);

}
