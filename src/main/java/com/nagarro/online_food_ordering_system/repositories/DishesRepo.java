package com.nagarro.online_food_ordering_system.repositories;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.nagarro.online_food_ordering_system.entities.Dishes;

@Repository
public interface DishesRepo extends JpaRepository<Dishes,Long>{

	List<Dishes> findAllByCategoryId(Long categoryId);

	List<Dishes> findAllByCategoryIdAndNameContaining(Long categoryId, String title);

	@Query("SELECT d FROM Dishes d LEFT JOIN FETCH d.reviews r LEFT JOIN FETCH r.dish LEFT JOIN FETCH r.user WHERE d.id = :id")
	Optional<Dishes> findByIdWithReviews(@Param("id") Long id);

}
