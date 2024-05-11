package com.example.sources.service;

import com.example.sources.domain.Category;
import com.example.sources.model.request.category.CreateCategoryRequest;
import com.example.sources.model.request.category.UpdateCategoryRequest;
import com.example.sources.model.response.category.CategoryResponse;
import com.example.sources.repo.CategoryRepo;
import com.example.sources.utils.exception.CategoryNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepo categoryRepo;

    public CategoryResponse create(CreateCategoryRequest request){
        Category category = Category.builder()
                .name(request.getName())
                .build();

        return new CategoryResponse(categoryRepo.save(category));
    }

    public List<CategoryResponse> readAll(){
        List<Category> categories = categoryRepo.findAll();

        List<CategoryResponse> response = new ArrayList<>();
        for(Category category : categories)
            response.add(new CategoryResponse(category));

        return response;
    }
    public CategoryResponse readById(Long id) throws CategoryNotFoundException {
        Optional<Category> category = categoryRepo.findById(id);

        if(!category.isPresent())
            throw new CategoryNotFoundException(id.toString());

        return new CategoryResponse(category.get());
    }
    public CategoryResponse update(UpdateCategoryRequest request) throws CategoryNotFoundException {
        Optional<Category> oldState = categoryRepo.findById(request.getId());

        if(!oldState.isPresent())
            throw new CategoryNotFoundException(request.getId().toString());

        Category newState = oldState.get();
        newState.setName(request.getName());

        newState = categoryRepo.save(newState);

        return new CategoryResponse(newState);
    }

    public void delete(Long id) throws CategoryNotFoundException {
        Optional<Category> category = categoryRepo.findById(id);

        if(!category.isPresent())
            throw new CategoryNotFoundException(id.toString());

        categoryRepo.deleteById(id);
    }
}
