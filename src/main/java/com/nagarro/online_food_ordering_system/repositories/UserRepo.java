package com.nagarro.online_food_ordering_system.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nagarro.online_food_ordering_system.entities.User;
import com.nagarro.online_food_ordering_system.enums.UserRole;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {

	Optional<User> findFirstByEmail(String email);

	User findByUserRole(UserRole admin);

	Optional<User> findByEmail(String email);



}
