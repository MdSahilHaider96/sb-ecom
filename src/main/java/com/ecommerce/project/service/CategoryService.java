package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIExceptions;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryReponse;
import com.ecommerce.project.repository.CategoryRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private ModelMapper modelMapper = new ModelMapper();
//    //COnversion dtoToUser
//    public Category dtoToCategory(CategoryDTO categoryDTO) {
//        Category category = modelMapper.map(categoryDTO, Category.class);
//        return category;
//    }
//    //Conversion User To Dto
//    public CategoryDTO categoryToDto(Category category) {
//        CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);
//        return categoryDTO;
//    }
    //Create
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category categoryFromDb = categoryRepo.findByCategoryName(category.getCategoryName());
        if (categoryFromDb != null) {
            throw new APIExceptions("category with name " + category.getCategoryName() + "already exists !!!!");
        }
        Category savedCategory =  categoryRepo.save(category);
        CategoryDTO   savedCategoryDTO = modelMapper.map(savedCategory, CategoryDTO.class);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    //FindAll
    public CategoryReponse getAllCategories(
     @RequestParam(name = "pageNumber")Integer pageNumber, @RequestParam (name = "pageSize")Integer pageSize ,@RequestParam (name = "sortBy")String soryBy,
     @RequestParam (name = "sortOrder")String sortOrder) {
        Sort sortByAndORder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(soryBy).ascending()
                : Sort.by(soryBy).descending();
        Pageable pageDetails =  PageRequest.of(pageNumber,pageSize,sortByAndORder);
        Page<Category> categoryPage = categoryRepo.findAll(pageDetails);
        List<Category> categoryList = categoryPage.getContent();
        if (categoryRepo.findAll().isEmpty()) throw new APIExceptions("No categories found !!!!");
         List<CategoryDTO> categoryDTOS = categoryList.stream().map(category -> modelMapper
                 .map(category, CategoryDTO.class)).toList();
        CategoryReponse categoryReponse = new CategoryReponse(categoryDTOS);
        categoryReponse.setContents(categoryDTOS);
        categoryReponse.setPageNumber(categoryPage.getNumber());
        categoryReponse.setPageSize(categoryPage.getSize());
        categoryReponse.setTotalPages(categoryPage.getTotalPages());
        categoryReponse.setTotalElements((int) categoryPage.getTotalElements());
        categoryReponse.setLastPage(categoryPage.isLast());
         return categoryReponse;
    }
    //FindById
    public Optional getCategoryById(int id){
        Optional<Category> optionalId = categoryRepo.findById(id);
        return optionalId;
    }

    //DeleteByID
    public CategoryDTO deleteCategory(int id){
       Optional<Category> category =  categoryRepo.findById(id);
       if (category.isPresent()){
           categoryRepo.delete(category.get());
       }
       return modelMapper.map(category , CategoryDTO.class);
    }
    //updateById
    public CategoryDTO updateCategory(int id, CategoryDTO categoryDetails) {
        // Check if a user with the given ID exists
        Optional<Category> optionalId = categoryRepo.findById(id);
        CategoryDTO savedCategory = null;
        Category category =  modelMapper.map(categoryDetails, Category.class);
        if (optionalId.isPresent()) {
            Category categoryToUpdate = optionalId.get();
            categoryToUpdate.setCategoryName(category.getCategoryName());
            savedCategory = modelMapper.map(categoryRepo.save(categoryToUpdate), CategoryDTO.class);
        }
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }
}
