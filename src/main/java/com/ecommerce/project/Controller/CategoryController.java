package com.ecommerce.project.Controller;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    //Create Categories
    @PostMapping("/public/categories")
    public ResponseEntity<Category> create(@Valid @RequestBody Category category){
        System.out.println("BackEnd create" + category);
        Category create = categoryService.createCategory(category);
        return new ResponseEntity<>(create,HttpStatus.CREATED );
    }
    //GetAll
    @GetMapping("/public/categories")
    public List<Category> getAllCatergories(){
        return categoryService.getAllCategories();
    }
    //GetById
    @GetMapping("/public/categories/{id}")
    public Optional<Category> getCategoryById(@PathVariable int id){
         return categoryService.getCategoryById(id);
    }
    //Delete By Id
        @DeleteMapping("/admin/delete/{id}")
    public boolean deleteCategory(@PathVariable int id){
        return categoryService.deleteCategory(id);
    }
    //UpdateById
    @PutMapping("/admin/update/{id}")
    public ResponseEntity<Category> updateUser(@Valid @PathVariable int id, @RequestBody Category categoryDetails) {
        // Call the service to update the user and return the updated user
        Category updatedCategory = categoryService.updateCategory(id, categoryDetails);
        // Return 200 OK if updated, 404 if not found
        return updatedCategory != null ? ResponseEntity.ok(updatedCategory) : ResponseEntity.notFound().build();
    }
}
