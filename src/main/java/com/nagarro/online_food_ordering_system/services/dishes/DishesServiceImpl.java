package com.nagarro.online_food_ordering_system.services.dishes;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.nagarro.online_food_ordering_system.dtos.DishesDto;
import com.nagarro.online_food_ordering_system.dtos.RatingDto;
import com.nagarro.online_food_ordering_system.dtos.ReviewDto;
import com.nagarro.online_food_ordering_system.entities.Category;
import com.nagarro.online_food_ordering_system.entities.Dishes;
import com.nagarro.online_food_ordering_system.entities.Order;
import com.nagarro.online_food_ordering_system.entities.OrderItem;
import com.nagarro.online_food_ordering_system.entities.Rating;
import com.nagarro.online_food_ordering_system.entities.Review;
import com.nagarro.online_food_ordering_system.entities.User;
import com.nagarro.online_food_ordering_system.repositories.CategoryRepo;
import com.nagarro.online_food_ordering_system.repositories.DishesRepo;
import com.nagarro.online_food_ordering_system.repositories.OrderRepo;
import com.nagarro.online_food_ordering_system.repositories.RatingRepo;
import com.nagarro.online_food_ordering_system.repositories.ReviewRepo;
import com.nagarro.online_food_ordering_system.repositories.UserRepo;

@Service
public class DishesServiceImpl implements DishesService {

		private final UserRepo userRepo;
	    private final OrderRepo orderRepo;
	    private final DishesRepo dishesRepo;
	    private final CategoryRepo categoryRepo;
	    private final RatingRepo ratingRepo;
	    private final ReviewRepo reviewRepo;

	    public DishesServiceImpl(UserRepo userRepo, OrderRepo orderRepo, DishesRepo dishesRepo,
	    		CategoryRepo categoryRepo, RatingRepo ratingRepo, ReviewRepo reviewRepo) {
	        this.userRepo = userRepo;
	        this.orderRepo = orderRepo;
	        this.dishesRepo = dishesRepo;
	        this.categoryRepo = categoryRepo;
	        this.ratingRepo = ratingRepo;
	        this.reviewRepo = reviewRepo;
	    }
	    
	    //Add Dishes to a specific category
	    @Override
	    public DishesDto postDishes(Long categoryId, DishesDto dishesDto) {
	        Optional<Category> optionalCategory = categoryRepo.findById(categoryId);
	        if (optionalCategory.isEmpty()) {
	            return null;
	        }

	        Category category = optionalCategory.get();
	        Dishes dishes = new Dishes();
	        BeanUtils.copyProperties(dishesDto, dishes);
	        dishes.setCategory(category);
	        dishes.setAvailability(dishesDto.isAvailability());

	        Dishes createdDishes = dishesRepo.save(dishes);
	        
	        DishesDto createdDishesDto = new DishesDto();
	        createdDishesDto.setId(createdDishes.getId());
	        createdDishesDto.setName(createdDishes.getName());
	        createdDishesDto.setDescription(createdDishes.getDescription());
	        createdDishesDto.setPrice(createdDishes.getPrice());
	        createdDishesDto.setAvailability(createdDishes.isAvailability()); 
	        createdDishesDto.setCategoryId(categoryId);
	        createdDishesDto.setCategoryName(category.getName());

	        return createdDishesDto;
	    }


	    //Average Rating
	    @Override
	    public void updateAverageRating(Long dishId) {
	        Optional<Dishes> optionalDishes = dishesRepo.findById(dishId);
	        if (optionalDishes.isPresent()) {
	            Dishes dish = optionalDishes.get();
	            List<Rating> ratings = ratingRepo.findByDishId(dishId);
	            
	            if (ratings.isEmpty()) {
	                dish.setAvgRating(0);
	            } else {
	                double average = ratings.stream()
	                                        .mapToDouble(Rating::getRating) 
	                                        .average()
	                                        .orElse(0.0);
	                dish.setAvgRating(average);
	            }
	            
	            dishesRepo.save(dish);
	        } else {
	            throw new IllegalArgumentException("Dish with Id: " + dishId + " not found!");
	        }
	    }
	      
	    //Get Dish By Dish Id
	    @Override
	    public DishesDto getDishById(Long dishId) {
	        Optional<Dishes> optionalDishes = dishesRepo.findByIdWithReviews(dishId);
	        if (optionalDishes.isPresent()) {
	            Dishes dish = optionalDishes.get();
	            DishesDto dishesDto = convertToDto(dish);

	            return dishesDto;
	        }
	        return null;
	    }

	    private DishesDto convertToDto(Dishes dish) {
	        DishesDto dto = new DishesDto();
	        BeanUtils.copyProperties(dish, dto);
	        if (dish.getCategory() != null) {
	            dto.setCategoryId(dish.getCategory().getId());
	            dto.setCategoryName(dish.getCategory().getName());
	        }
	        dto.setReviews(dish.getReviews().stream()
	                          .map(this::convertToDto)
	                          .collect(Collectors.toList()));
	        dto.setOrderCount(orderRepo.countOrdersByDishId(dish.getId()));
	        return dto;
	    }
		
	    //All Dishesh By Category Id (Cuisine)
		@Override
		public List<DishesDto> getAllDishesByCategory(Long categoryId) {
			return dishesRepo.findAllByCategoryId(categoryId).stream().map(this::convertToDto).collect(Collectors.toList());
		}
		
		  //All Dishesh By Category Id and name (Cuisine)
		@Override
		public List<DishesDto> getDishesByCategoryAndTitle(Long categoryId, String title) {
			return dishesRepo.findAllByCategoryIdAndNameContaining(categoryId, title).stream().map(this::convertToDto).collect(Collectors.toList());
		}
		
		//Update Dishes By Id
		@Override
		public DishesDto updateDishes(Long dishesId, DishesDto dishesDto) {
		    Optional<Dishes> optionalDishes = dishesRepo.findById(dishesId);
		    if (optionalDishes.isPresent()) {
		        Dishes dishes = optionalDishes.get();
		        BeanUtils.copyProperties(dishesDto, dishes, "id", "category");

		        if (dishesDto.getCategoryId() != null) {
		            Optional<Category> categoryOptional = categoryRepo.findById(dishesDto.getCategoryId());
		            if (categoryOptional.isPresent()) {
		                dishes.setCategory(categoryOptional.get());
		            }
		        }
		        
		        Dishes updatedDishes = dishesRepo.save(dishes);
		        
		        DishesDto updatedDishesDto = new DishesDto();
		        BeanUtils.copyProperties(updatedDishes, updatedDishesDto);
		        if (updatedDishes.getCategory() != null) {
		            updatedDishesDto.setCategoryId(updatedDishes.getCategory().getId());
		            updatedDishesDto.setCategoryName(updatedDishes.getCategory().getName());
		        }
		        
		        return updatedDishesDto;
		    } 
		    return null;
		}
		
		
		@Override
		public boolean deleteDishes(Long dishesId) {
		    Optional<Dishes> optionalDishes = dishesRepo.findById(dishesId);
		    if (optionalDishes.isPresent()) {
		        dishesRepo.deleteById(dishesId);
		        return true;
		    }
		    return false;
		}

		
		@Override
		public void addReview(Long userId, Long dishId, String reviewText) {
		    if (reviewText == null || reviewText.trim().isEmpty()) {
		        throw new IllegalArgumentException("Review cannot be null or empty.");
		    }
		    
		    if (!hasOrderedDish(userId, dishId)) {
		        throw new IllegalArgumentException("User has not ordered this dish!!");
		    }
		    
		    Optional<Dishes> dishOpt = dishesRepo.findById(dishId);
		    if (!dishOpt.isPresent()) {
		        throw new IllegalArgumentException("Dish not found!!");
		    }

		    Dishes dish = dishOpt.get();

		    // Fetch and validate the user
		    User user = userRepo.findById(userId)
		                        .orElseThrow(() -> new IllegalArgumentException("User not found!!"));
		    
		    // Add the review
		    Review review = new Review();
		    review.setDish(dish);
		    review.setUser(user);
		    review.setReview(reviewText);
		    reviewRepo.save(review); 
		}


		@Override
		public void addRating(Long userId, Long dishId, int rating) {
		    if (hasOrderedDish(userId, dishId)) {
		        Optional<Dishes> dishOpt = dishesRepo.findById(dishId);
		        if (dishOpt.isPresent()) {
		            Dishes dish = dishOpt.get();
		            Rating ratingEntity = new Rating();
		            ratingEntity.setDish(dish);
		            ratingEntity.setUser(userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found")));
		            ratingEntity.setRating(rating);
		            ratingRepo.save(ratingEntity); 
		            
		           updateAverageRating(dishId); 
		           
		        } else {
		            throw new IllegalArgumentException("Dish not found.");
		        }
		    } else {
		        throw new IllegalArgumentException("User has not ordered this dish.");
		    }
		}

		
		 @Override
		 public boolean hasOrderedDish(Long userId, Long dishId) {
		        List<Order> orders = orderRepo.findByUserId(userId);
		        for (Order order : orders) {
		            for (OrderItem item : order.getOrderItems()) {
		                if (item.getDish().getId().equals(dishId)) {
		                    return true;
		                }
		            }
		        }
		        return false;
		    }


	    @Override
	    public List<ReviewDto> getReviewsByDishId(Long dishId) {
	        List<Review> reviews = reviewRepo.findByDishId(dishId);
	        
	        if (reviews.isEmpty()) {
	            throw new IllegalArgumentException("No reviews found for the specified dish.");
	        }
	        
	        return reviews.stream()
	                      .map(this::convertToDto) 
	                      .collect(Collectors.toList());
	    }


	    // Helper method to convert Review entity to ReviewDto
	    private ReviewDto convertToDto(Review review) {
	        ReviewDto reviewDto = new ReviewDto();
	        reviewDto.setId(review.getId());
	        reviewDto.setDishId(review.getDish().getId());
	        reviewDto.setUserId(review.getUser().getId());
	        reviewDto.setReview(review.getReview());
	        return reviewDto;
	    }
	    
	    
	    @Override
	    public List<RatingDto> getRatingsByDishId(Long dishId) {
	        List<Rating> ratings = ratingRepo.findByDishId(dishId);
	        if (ratings.isEmpty()) {
	            throw new IllegalArgumentException("No ratings found for the specified dish.");
	        }
	        return ratings.stream()
	                      .map(rating -> {
	                          RatingDto dto = new RatingDto();
	                          dto.setId(rating.getId());
	                          dto.setUserId(rating.getUser().getId());
	                          dto.setDishId(rating.getDish().getId());
	                          dto.setRating(rating.getRating());
	                          return dto;
	                      })
	                      .collect(Collectors.toList());
	    }

	    public List<RatingDto> getRatingsByDishIdForUser(Long dishId, Long userId) {
	        List<Rating> ratings = ratingRepo.findByDishIdAndUserId(dishId, userId);
	        if (ratings.isEmpty()) {
	            throw new IllegalArgumentException("You haven't added a rating for this dish.");
	        }
	        return ratings.stream()
	                      .map(rating -> {
	                          RatingDto dto = new RatingDto();
	                          dto.setId(rating.getId());
	                          dto.setUserId(rating.getUser().getId());
	                          dto.setDishId(rating.getDish().getId());
	                          dto.setRating(rating.getRating());
	                          return dto;
	                      })
	                      .collect(Collectors.toList());
	    }


	    @Override
	    public List<DishesDto> getDishesSortedByRating() {
	        List<Dishes> dishesList = dishesRepo.findAll();

	        if (dishesList.isEmpty()) {
	            return Collections.emptyList();
	        }

	        // Convert the list of Dishes entities to a list of DishesDto
	        List<DishesDto> dishesDtoList = dishesList.stream()
	            .map(this::convertToDto)
	            .collect(Collectors.toList());

	        // Sort the list by average rating in descending order
	        List<DishesDto> sortedDishes = dishesDtoList.stream()
	            .sorted((d1, d2) -> Double.compare(d2.getAvgRating(), d1.getAvgRating()))
	            .collect(Collectors.toList());

	        return sortedDishes;
	    }
	    
	    @Override
	    public List<DishesDto> getAllDishesSortedByOrderCount() {
	        List<Dishes> dishes = dishesRepo.findAll();

	        if (dishes.isEmpty()) {
	            return Collections.emptyList();
	        }

	        return dishes.stream()
	            .map(dish -> {
	                DishesDto dto = convertToDto(dish);
	                dto.setOrderCount(orderRepo.countOrdersByDishId(dish.getId())); 
	                return dto;
	            })
	            .sorted((d1, d2) -> Integer.compare(d2.getOrderCount(), d1.getOrderCount()))
	            .collect(Collectors.toList());
	    }

	    
	    @Override
	    public List<ReviewDto> getReviewsByDishIdForUser(Long dishId, Long userId) {
	        List<Review> reviews = reviewRepo.findByDishIdAndUserId(dishId, userId);
	        return reviews.stream()
	                      .map(this::convertToDto)
	                      .collect(Collectors.toList());
	    }


	}
	    
