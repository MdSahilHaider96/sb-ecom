package com.ecommerce.project.Controller;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryReponse;
import com.ecommerce.project.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    //Create Categories
    @PostMapping("/public/categories")
    public ResponseEntity<CategoryDTO> create(@Valid @RequestBody CategoryDTO categoryDTO){
        System.out.println("BackEnd create" + categoryDTO);
        CategoryDTO savedcategoryDTO = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(savedcategoryDTO,HttpStatus.CREATED );
    }

    //GetAll
    @GetMapping("/public/categories")
    public ResponseEntity<CategoryReponse> getAllCatergories(
            @RequestParam (name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER , required = false)Integer pageNumber,
            @RequestParam (name = "pageSize",defaultValue = AppConstants.PAGE_SIZE , required = false)Integer pageSize,
            @RequestParam (name = "sortBy",defaultValue = AppConstants.SORT_CATEGORIES_BY , required = false) String sortBy,
            @RequestParam (name = "sortOrder",defaultValue = AppConstants.SORT_ORDER , required = false) String sortOrder) {
                CategoryReponse categoryReponse = categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder);
                return new ResponseEntity<>(categoryReponse,HttpStatus.OK);
    }

    //GetById
    @GetMapping("/public/categories/{id}")
    public Optional<Category> getCategoryById(@PathVariable int id){
        return categoryService.getCategoryById(id);
    }

    //Delete By Id
        @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable int id){
     CategoryDTO deletedCategory = categoryService.deleteCategory(id);
     return new ResponseEntity<>(deletedCategory,HttpStatus.OK);
    }

    // UpdateById
    @PutMapping("/admin/update/{id}")
    public ResponseEntity<CategoryDTO> updateUser(@Valid @PathVariable int id, @RequestBody CategoryDTO categoryDetails) {
        // Call the service to update the user and return the updated user
        CategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDetails);
        // Return 200 OK if updated, 404 if not found
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
   }
}
