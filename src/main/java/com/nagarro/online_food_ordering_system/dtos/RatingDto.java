package com.nagarro.online_food_ordering_system.dtos;

public class RatingDto {
	
	 private Long id;
	 
	 private Long userId;
	 
	 private Long dishId; 
     
	 private int rating;
	
     
     public Long getId() {
		return id;
	 }
	
     public void setId(Long id) {
		this.id = id;
	 }
	
     public Long getUserId() {
		return userId;
	 }
	
     public void setUserId(Long userId) {
		this.userId = userId;
	 }
	
     public Long getDishId() {
		return dishId;
     }
	
     public void setDishId(Long dishId) {
		this.dishId = dishId;
	 }
	
     public int getRating() {
		return rating;
	 }
	
     public void setRating(int rating) {
		this.rating = rating;
	 }
         

}
