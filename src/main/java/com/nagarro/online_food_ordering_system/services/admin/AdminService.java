package com.nagarro.online_food_ordering_system.services.admin;

import java.util.List;
import com.nagarro.online_food_ordering_system.dtos.CategoryDto;


public interface AdminService {

	CategoryDto postCategory(CategoryDto categoryDto);

	List<CategoryDto> getAllCategories();

	List<CategoryDto> getAllCategoriesByTitle(String title);

	boolean deleteCategory(Long categoryId);

	CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto);

	boolean categoryExists(Long categoryId);

	boolean categoryNameExists(String name);


}
