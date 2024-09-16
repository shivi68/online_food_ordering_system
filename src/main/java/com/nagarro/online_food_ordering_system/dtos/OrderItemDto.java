package com.nagarro.online_food_ordering_system.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class OrderItemDto {

	@JsonIgnore
	private Long id;
	
	private Long dishId;
	
	private String dishName;
    
	private int quantity;
   
	private double price;
    
   
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
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public String getDishName() {
		return dishName;
	}
	
	public void setDishName(String dishName) {
		this.dishName = dishName;
	}
	

    
}
