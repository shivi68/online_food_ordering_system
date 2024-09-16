package com.nagarro.online_food_ordering_system.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nagarro.online_food_ordering_system.dtos.DishesDto;
import com.nagarro.online_food_ordering_system.dtos.RatingDto;
import com.nagarro.online_food_ordering_system.dtos.ReviewDto;
import com.nagarro.online_food_ordering_system.entities.Category;
import com.nagarro.online_food_ordering_system.entities.User;
import com.nagarro.online_food_ordering_system.enums.UserRole;
import com.nagarro.online_food_ordering_system.repositories.CategoryRepo;
import com.nagarro.online_food_ordering_system.repositories.UserRepo;
import com.nagarro.online_food_ordering_system.services.dishes.DishesService;

@RestController
@RequestMapping("/api/dishes")
public class DishesController {
	
	 private final DishesService dishesService;
	 private final CategoryRepo categoryRepo;
	 private final UserRepo userRepo;
	 
	    public DishesController(DishesService dishesService, CategoryRepo categoryRepo, UserRepo userRepo) {
	        this.dishesService = dishesService;
	        this.categoryRepo = categoryRepo;
	        this.userRepo = userRepo;
	    }
	    
	    //API to add new Dishes to a particular Cuisine by Category Id
	    @PostMapping("/{categoryId}/addDishes")
	    @PreAuthorize("hasAuthority('ADMIN')")
	    public ResponseEntity<?> postDishes(@PathVariable Long categoryId, @RequestBody DishesDto dishesDto) {
	        if (dishesDto.getName() == null || dishesDto.getName().isBlank()) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                                 .body(Map.of("error", "Dish name cannot be null or blank"));
	        }
	        if (dishesDto.getDescription() == null || dishesDto.getDescription().isBlank()) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                                 .body(Map.of("error", "Description cannot be null or blank"));
	        }
	        if (dishesDto.getPrice() <= 0) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                                 .body(Map.of("error", "Price must be greater than zero"));
	        }
	        if (dishesDto.getAvailability() == null) { 
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                                 .body(Map.of("error", "Availability cannot be null"));
	        }

	        dishesDto.setCategoryId(categoryId);

	        DishesDto createdDishesDto = dishesService.postDishes(categoryId, dishesDto);
	        if (createdDishesDto == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                                 .body(Map.of("error", "No such Cuisine with the CuisineID " + categoryId + " is Present"));
	        }

	        return ResponseEntity.status(HttpStatus.CREATED).body(createdDishesDto);
	    }
	        
	    //API to get Dish By dish Id
	    @GetMapping("/{dishesId}")
	    public ResponseEntity<?> getDishById(@PathVariable Long dishesId) {
	        DishesDto dishesDto = dishesService.getDishById(dishesId);
	        if (dishesDto == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                                 .body(Map.of("error", "There is no such dish with DishId " + dishesId + " is present"));
	        }
	        return ResponseEntity.ok(dishesDto);
	    }
	    
	   // Get All Dishes By Cuisine by Category Id
	    @GetMapping("/{categoryId}/allDishes")
	    public ResponseEntity<?> getAllDishesByCategory(@PathVariable Long categoryId) {
	        List<DishesDto> dishesDtoList = dishesService.getAllDishesByCategory(categoryId);
	        if (dishesDtoList == null || dishesDtoList.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                                 .body(Map.of("error", "No dishes found for Category ID " + categoryId));
	        }
	        return ResponseEntity.ok(dishesDtoList);
	    }
	    
	    //API to update Dish by dish Id
	    @PutMapping("/{dishesId}")
	    @PreAuthorize("hasAuthority('ADMIN')")
	    public ResponseEntity<?> updateDishes(@PathVariable Long dishesId, @RequestBody DishesDto dishesDto) {
	        if (dishesDto.getName() == null || dishesDto.getName().isBlank()) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                                 .body(Map.of("error", "Dish name cannot be null or blank"));
	        }
	        if (dishesDto.getDescription() == null || dishesDto.getDescription().isBlank()) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                                 .body(Map.of("error", "Description cannot be null or blank"));
	        }
	        if (dishesDto.getPrice() <= 0) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                                 .body(Map.of("error", "Price must be greater than zero"));
	        }
	        if (dishesDto.getCategoryId() != null) {
	            Optional<Category> categoryOptional = categoryRepo.findById(dishesDto.getCategoryId());
	            if (categoryOptional.isEmpty()) {
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                                     .body(Map.of("error", "No such Cuisine with the CuisineID " + dishesDto.getCategoryId() + " is Present"));
	            }
	        }

	        DishesDto updatedDishesDto = dishesService.updateDishes(dishesId, dishesDto);
	        if (updatedDishesDto == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                                 .body(Map.of("error", "No such Dish with the DishID " + dishesId + " is Present"));
	        }
	        return ResponseEntity.status(HttpStatus.OK).body(updatedDishesDto);
	    }
	    
	    //API to delete Dish By Dish Id
	    @DeleteMapping("/{dishesId}")
	    @PreAuthorize("hasAuthority('ADMIN')")
	    public ResponseEntity<?> deleteDishes(@PathVariable Long dishesId) {
	        boolean isDeleted = dishesService.deleteDishes(dishesId);
	        if (isDeleted) {
	            return ResponseEntity.status(HttpStatus.OK)
	                                 .body(Map.of("message", "Dish is successfully deleted"));
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                                 .body(Map.of("error", "There is no such dish with DishId " + dishesId + " present"));
	        }
	    }	    
	    
	    //API to Get Dish By Category means Cuisine and Title means Cuisine Name
	    @GetMapping("/{categoryId}/dishes/{title}")
	    public ResponseEntity<?> getDishesByCategoryAndTitle(@PathVariable Long categoryId, @PathVariable String title) {
	        if (!categoryRepo.existsById(categoryId)) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                                 .body(Map.of("error", "No category found with ID " + categoryId));
	    }
	        List<DishesDto> dishesDtoList = dishesService.getDishesByCategoryAndTitle(categoryId, title);

	        if (dishesDtoList.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                                 .body(Map.of("error", "No dishes found for Category ID " + categoryId + " with title containing '" + title + "'"));
	        }

	        return ResponseEntity.ok(dishesDtoList);
	    }
	    
	    //API to add Review of a dish.Customer can only review those dishes which he/she ordered
	    @PostMapping("/{dishId}/review")
	    @PreAuthorize("hasAuthority('CUSTOMER')")
	    public ResponseEntity<?> addReview(@PathVariable Long dishId, @RequestBody ReviewDto reviewDto) {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        String username = authentication.getName(); 

	        Optional<User> authenticatedUserOpt = userRepo.findByEmail(username);
	        if (!authenticatedUserOpt.isPresent()) {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Authenticated user not found."));
	        }
	        
	        User authenticatedUser = authenticatedUserOpt.get();
	        Long authenticatedUserId = authenticatedUser.getId();
	        if (reviewDto.getUserId() == null) {
	            return ResponseEntity.badRequest().body(Map.of("error", "User ID cannot be null."));
	        }

	        if (!reviewDto.getUserId().equals(authenticatedUserId)) {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "You can only add reviews for your own user ID."));
	        }

	        if (reviewDto.getReview() == null || reviewDto.getReview().trim().isEmpty()) {
	            return ResponseEntity.badRequest().body(Map.of("error", "Review cannot be null or empty."));
	        }

	        try {
	            dishesService.addReview(reviewDto.getUserId(), dishId, reviewDto.getReview());
	            return ResponseEntity.ok("Review added successfully.");
	        } catch (IllegalArgumentException ex) {
	            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
	        } catch (Exception ex) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                                 .body(Map.of("error", "An unexpected error occurred: " + ex.getMessage()));
	        }
	    }

	    @GetMapping("/{dishId}/allReviews")
	    public ResponseEntity<?> getReviewsByDishId(@PathVariable Long dishId) {
	        try {
	            List<ReviewDto> reviews = dishesService.getReviewsByDishId(dishId);
	            if (reviews.isEmpty()) {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "No reviews found for the specified dish."));
	            }
	            return ResponseEntity.ok(reviews);
	        } catch (IllegalArgumentException ex) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
	        } catch (Exception ex) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An unexpected error occurred: " + ex.getMessage()));
	        }
	    }
   
	    //API to add Rating of a dish. Customer can only rate those dishes which he/she ordered.
	    @PostMapping("/{dishId}/rating")
	    @PreAuthorize("hasAuthority('CUSTOMER')")
	    public ResponseEntity<?> addRating(@PathVariable Long dishId, @RequestBody RatingDto ratingDto) {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        String username = authentication.getName(); 

	        Optional<User> authenticatedUserOpt = userRepo.findByEmail(username);
	        if (!authenticatedUserOpt.isPresent()) {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Authenticated user not found."));
	        }
	        
	        User authenticatedUser = authenticatedUserOpt.get();
	        Long authenticatedUserId = authenticatedUser.getId();

	        if (ratingDto.getUserId() == null) {
	            return ResponseEntity.badRequest().body(Map.of("error", "User ID cannot be null."));
	        }

	        if (!ratingDto.getUserId().equals(authenticatedUserId)) {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "You can only add ratings for your own user ID."));
	        }

	        if (ratingDto.getRating() < 1 || ratingDto.getRating() > 5) {
	            return ResponseEntity.badRequest().body(Map.of("error", "Rating must be between 1 and 5."));
	        }

	        try {
	            dishesService.addRating(ratingDto.getUserId(), dishId, ratingDto.getRating());
	            return ResponseEntity.ok("Rating added successfully.");
	        } catch (IllegalArgumentException ex) {
	            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
	        } catch (Exception ex) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                                 .body(Map.of("error", "An unexpected error occurred: " + ex.getMessage()));
	        }
	    }

	    @GetMapping("/{dishId}/allRatings")
	    public ResponseEntity<?> getRatingsByDishId(@PathVariable Long dishId) {
	        try {
	          
	            List<RatingDto> ratings = dishesService.getRatingsByDishId(dishId);

	            if (ratings.isEmpty()) {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "No ratings found for the specified dish."));
	            }
	            return ResponseEntity.ok(ratings);
	        } catch (IllegalArgumentException ex) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
	        } catch (Exception ex) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An unexpected error occurred: " + ex.getMessage()));
	        }
	    }

	   // Get Dishes Sorted By Rating
	    @GetMapping("/sortedByRating")
	    public ResponseEntity<?> getDishesSortedByRating() {
	        List<DishesDto> sortedDishes = dishesService.getDishesSortedByRating();
	        if (sortedDishes.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                                 .body(Map.of("error", "No ratings available for any Dish!!"));
	        }
	        
	        return ResponseEntity.ok(sortedDishes);
	    }

	    
	    // Get All Dishes Sorted By Order Count
	    @GetMapping("/sortedByOrderCount")
	    public ResponseEntity<?> getAllDishesSortedByOrderCount() {
	        List<DishesDto> sortedDishes = dishesService.getAllDishesSortedByOrderCount();
	        if (sortedDishes.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                                 .body(Map.of("error", "No orders have been made yet!!"));
	        }
	        
	        return ResponseEntity.ok(sortedDishes);
	    }
	    
	    
}
