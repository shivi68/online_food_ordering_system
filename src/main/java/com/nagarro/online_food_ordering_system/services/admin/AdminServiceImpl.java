package com.nagarro.online_food_ordering_system.services.admin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nagarro.online_food_ordering_system.dtos.CategoryDto;
import com.nagarro.online_food_ordering_system.entities.Category;
import com.nagarro.online_food_ordering_system.repositories.CategoryRepo;
import com.nagarro.online_food_ordering_system.repositories.DishesRepo;

@Service
public class AdminServiceImpl implements AdminService {

    private final CategoryRepo categoryRepo;
    
    private final DishesRepo dishesRepo;

    @Autowired
    public AdminServiceImpl(CategoryRepo categoryRepo, DishesRepo dishesRepo) {
        this.categoryRepo = categoryRepo;
        this.dishesRepo = dishesRepo;
    }

    //Method to add Category (Cuisine) 
    @Override
    public CategoryDto postCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        Category createdCategory = categoryRepo.save(category);

        CategoryDto createdCategoryDto = new CategoryDto();
        createdCategoryDto.setId(createdCategory.getId());
        createdCategoryDto.setName(createdCategory.getName());
        createdCategoryDto.setDescription(createdCategory.getDescription());
        return createdCategoryDto;
    }

    public boolean categoryNameExists(String name) {
        return categoryRepo.findByName(name).isPresent();
    }
    
    //Method to get all Category (Cuisine) 
    @Override
    public List<CategoryDto> getAllCategories() {
    	return categoryRepo.findAll().stream().map(Category::getCategoryDto).collect(Collectors.toList());
    }

    //Method to get all Category (Cuisine) By Name
	@Override
	public List<CategoryDto> getAllCategoriesByTitle(String title) {
		return categoryRepo.findAllByNameContaining(title).stream().map(Category::getCategoryDto).collect(Collectors.toList());
	}


	//Delete Cuisine 
	@Override
	public boolean deleteCategory(Long categoryId) {
		if (categoryRepo.existsById(categoryId)) {
            categoryRepo.deleteById(categoryId);
            return true;
        }
		return false;
	}

	//Update Category or Cuisine
	@Override
	public CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto) {
	        Optional<Category> optionalCategory = categoryRepo.findById(categoryId);
	        if (optionalCategory.isPresent()) {
	            Category category = optionalCategory.get();
	            category.setName(categoryDto.getName());
	            category.setDescription(categoryDto.getDescription());
	            Category updatedCategory = categoryRepo.save(category);
	            CategoryDto updatedCategoryDto = new CategoryDto();
	            updatedCategoryDto.setId(updatedCategory.getId());
	            updatedCategoryDto.setName(updatedCategory.getName());
	            updatedCategoryDto.setDescription(updatedCategory.getDescription());
	            return updatedCategoryDto;
	        }
	        return null;
	}

	
	@Override
	public boolean categoryExists(Long categoryId) {
		return categoryRepo.existsById(categoryId);
	}


}
