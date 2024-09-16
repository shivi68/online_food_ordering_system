package com.nagarro.online_food_ordering_system.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nagarro.online_food_ordering_system.entities.Category;

@Repository
public interface CategoryRepo extends JpaRepository<Category,Long> {

	List<Category> findAllByNameContaining(String title);
	Optional<Category> findByName(String name);
	

}
