package com.nagarro.online_food_ordering_system.dtos;

import java.util.List;

public class DishesDto {

	    private Long id;
	   
	    private String name;
	    
	    private String description;
	    
	    private double price;
	    
	    private Boolean availability;
	    
	    private double avgRating;
	    
	    private Long categoryId;
	    
	    private String categoryName;
	    
	    private int orderCount;
	    
	    private List<ReviewDto> reviews;
	    
	
	    public Long getId() {
			return id;
		}
		
	    public void setId(Long id) {
			this.id = id;
		}
		
	    public String getName() {
			return name;
		}
		
	    public void setName(String name) {
			this.name = name;
		}
		
	    public String getDescription() {
			return description;
		}
		
	    public void setDescription(String description) {
			this.description = description;
		}
		
	    public double getPrice() {
			return price;
		}
		
	    public void setPrice(double price) {
			this.price = price;
		}
		
	    public Long getCategoryId() {
			return categoryId;
		}
		
	    public void setCategoryId(Long categoryId) {
			this.categoryId = categoryId;
		}
		
	    public String getCategoryName() {
			return categoryName;
		}
		
	    public void setCategoryName(String categoryName) {
			this.categoryName = categoryName;
		}
		
	    public boolean isAvailability() {
			return availability;
		}
		
	    public void setAvailability(boolean availability) {
			this.availability = availability;
		}
		
	    public double getAvgRating() {
			return avgRating;
		}
		
	    public void setAvgRating(double avgRating) {
			this.avgRating = avgRating;
		}
		
	    public int getOrderCount() {
			return orderCount;
		}
		
	    public void setOrderCount(int orderCount) {
			this.orderCount = orderCount;
		}
		
	    public Boolean getAvailability() {
			return availability;
		}
		
	    public void setAvailability(Boolean availability) {
			this.availability = availability;
		}

		public List<ReviewDto> getReviews() {
			return reviews;
		}

		public void setReviews(List<ReviewDto> reviews) {
			this.reviews = reviews;
		} 		
	    
	    
}
