package com.nagarro.online_food_ordering_system.entities;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nagarro.online_food_ordering_system.dtos.DishesDto;
import com.nagarro.online_food_ordering_system.dtos.ReviewDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;


@Entity
public class Dishes {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	   
	    private String name;
	    
	    private String description;
	    
	    private double price;
	    
	    private boolean availability; 
	    
	    @ManyToOne(fetch = FetchType.LAZY, optional = false)
	    @JoinColumn(name = "category_id", nullable = false)
	    @OnDelete(action = OnDeleteAction.CASCADE)
	    @JsonIgnore
	    private Category category;

	    private double avgRating; 
	    
	    @OneToMany(mappedBy = "dish", cascade = CascadeType.ALL)
	    private List<Review> reviews;
	    

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

		public Category getCategory() {
			return category;
		}

		public void setCategory(Category category) {
			this.category = category;
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
		
		
		public List<Review> getReviews() {
			return reviews;
		}

		public void setReviews(List<Review> reviews) {
			this.reviews = reviews;
		}

}
