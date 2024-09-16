package com.nagarro.online_food_ordering_system.entities;

import com.nagarro.online_food_ordering_system.dtos.CategoryDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="categories")
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private String description;
	
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
	
	public CategoryDto getCategoryDto() {
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setId(id);
		categoryDto.setName(name);
		categoryDto.setDescription(description);
		return categoryDto;
	}
	
}
