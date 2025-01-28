package com.ecommerce.project.payload;

import lombok.*;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class CategoryReponse {
    private List<CategoryDTO> contents;
    private Integer pageSize;
    private Integer PageNumber;
    private Integer totalPages;
    private Integer totalElements;
    private boolean lastPage;
    public CategoryReponse(List<CategoryDTO> categoryDTOS) {
    }
}
