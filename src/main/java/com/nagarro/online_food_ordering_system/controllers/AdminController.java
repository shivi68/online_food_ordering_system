package com.nagarro.online_food_ordering_system.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nagarro.online_food_ordering_system.dtos.CategoryDto;
import com.nagarro.online_food_ordering_system.services.admin.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	 @Autowired
	private final AdminService adminService;
	
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    //API to add a new Cuisine
    @PostMapping("/addCuisine")
    public ResponseEntity<?> postCategory(@RequestBody CategoryDto categoryDto, Authentication authentication) {
        if (categoryDto.getName() == null || categoryDto.getName().isBlank()) {
            return new ResponseEntity<>(Map.of("error", "Category name cannot be blank"), HttpStatus.BAD_REQUEST);
        }
        if (categoryDto.getDescription() == null || categoryDto.getDescription().isBlank()) {
            return new ResponseEntity<>(Map.of("error", "Category description cannot be blank"), HttpStatus.BAD_REQUEST);
        }
        if (adminService.categoryNameExists(categoryDto.getName())) {
            return new ResponseEntity<>(Map.of("error", "Category name already exists"), HttpStatus.BAD_REQUEST);
        }

        CategoryDto createdCategoryDto = adminService.postCategory(categoryDto);
        if (createdCategoryDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(createdCategoryDto);
    }
    
    //Accessible for both admin and customer
    //API to get all Cuisine
    @GetMapping("/allCuisine")
    public ResponseEntity<?> getAllCategories() {
        List<CategoryDto> categoryDtoList = adminService.getAllCategories();
        if (categoryDtoList == null || categoryDtoList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(Map.of("message", "Oops, there are no categories available!!"));
        }
        return ResponseEntity.ok(categoryDtoList);
    }

    //API to get Cuisine by Cuisine Name
    @GetMapping("/cuisine/{title}")
    public ResponseEntity<?> getAllCategoryByTitle(@PathVariable String title) {
        List<CategoryDto> categoryDtoList = adminService.getAllCategoriesByTitle(title);
        
        if (categoryDtoList == null || categoryDtoList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(Map.of("error", "Oops!! No such Cuisine found"));
        }
        
        return ResponseEntity.ok(categoryDtoList);
    }

    //API to delete Cuisine By Cuisine Id = Category Id
    @DeleteMapping("/cuisine/{categoryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
        boolean categoryExists = adminService.categoryExists(categoryId);

        if (!categoryExists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(Map.of("error", "No such Cuisine with the CuisineID " + categoryId + " is Present"));
        }

        boolean isDeleted = adminService.deleteCategory(categoryId);
        if (!isDeleted) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(Map.of("error", "Failed to delete the Cuisine"));
        }
        return ResponseEntity.ok(Map.of("message", "Cuisine with ID " + categoryId + " is deleted successfully"));
    }
    
    //API to update Cuisine BY Cuisine Id = Category Id
    @PutMapping("/cuisine/{categoryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDto categoryDto) {
        if (categoryDto.getName() == null || categoryDto.getName().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(Map.of("error", "Category name cannot be blank"));
        }
        if (categoryDto.getDescription() == null || categoryDto.getDescription().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(Map.of("error", "Category description cannot be blank"));
        }
        CategoryDto updatedCategoryDto = adminService.updateCategory(categoryId, categoryDto);
        if (updatedCategoryDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(Map.of("error", "No such Cuisine with the CuisineID " + categoryId + " is Present"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(updatedCategoryDto);
    }
}  
    


