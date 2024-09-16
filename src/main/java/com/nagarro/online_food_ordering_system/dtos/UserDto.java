package com.nagarro.online_food_ordering_system.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nagarro.online_food_ordering_system.enums.UserRole;

import lombok.Data;

@Data
public class UserDto {
	
	private Long id;
	
	private String name;
	
	private String email;
	
	@JsonIgnore
	private String password;
	
	@JsonIgnore
	private String address;
	
	@JsonIgnore
	private UserRole userRole;
    
	private List<OrderDto> orders;
	
    
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public UserRole getUserRole() {
		return userRole;
	}
	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}
	public List<OrderDto> getOrders() {
		return orders;
	}
	public void setOrders(List<OrderDto> orders) {
		this.orders = orders;
	}
	

}
