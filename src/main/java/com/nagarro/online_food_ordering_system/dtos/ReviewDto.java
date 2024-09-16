package com.nagarro.online_food_ordering_system.dtos;

public class ReviewDto {

	private Long id;
	
	private Long dishId;
    
	private Long userId;
    
	private String review;
	
    public Long getId() {
		return id;
	}
	
    public void setId(Long id) {
		this.id = id;
	}
	
	public Long getDishId() {
		return dishId;
	}
	
	public void setDishId(Long dishId) {
		this.dishId = dishId;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public String getReview() {
		return review;
	}
	
	public void setReview(String review) {
		this.review = review;
	}
    
    
}
