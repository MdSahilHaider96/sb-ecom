package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIExceptions;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;

    //Create
    public Category createCategory(Category category) {
        Category savedCategory = (Category) categoryRepo.findByCategoryName(category.getCategoryName());
        if (savedCategory != null) {
            throw new APIExceptions("category with name " + category.getCategoryName() + "already exists !!!!");
        }
        return  categoryRepo.save(category);
    }

    //FindAll
    public List<Category> getAllCategories() {
        List<Category> categoryList = categoryRepo.findAll();
        if (categoryRepo.findAll().isEmpty()) throw new APIExceptions("No categories found !!!!");
        return categoryList;
    }
    //FindById
    public Optional getCategoryById(int id){
        Optional<Category> optionalId = categoryRepo.findById(id);
        return optionalId;
    }
    //DeleteByID
    public boolean deleteCategory(int id){
       Optional<Category> optionalId = categoryRepo.findById(id);
       if (optionalId.isPresent()){
           categoryRepo.delete(optionalId.get());
           return true;
       }
       return false;
    }
    //updateById
    public Category updateCategory(int id, Category categoryDetails) {
        // Check if a user with the given ID exists
        Optional<Category> optionalId = categoryRepo.findById(id);
        if (optionalId.isPresent()) {
            // Get the existing user
            Category category = optionalId.get();
            // Update the fields with new values
            category.setCategoryName(categoryDetails.getCategoryName());
            // Save the updated user
            return categoryRepo.save(category);
        }
        return null; // Return null if the user was not found
    }

}
