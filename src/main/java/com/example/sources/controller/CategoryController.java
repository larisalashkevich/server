package com.example.sources.controller;

import com.example.sources.model.request.category.CreateCategoryRequest;
import com.example.sources.model.request.category.UpdateCategoryRequest;
import com.example.sources.model.response.ErrorMessageResponse;
import com.example.sources.model.response.MessageResponse;
import com.example.sources.service.CategoryService;
import com.example.sources.utils.exception.CategoryNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateCategoryRequest request){
        return new ResponseEntity<>(categoryService.create(request), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> readAll(){
        return new ResponseEntity<>(categoryService.readAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> readById(@PathVariable Long id){
        try{
            return new ResponseEntity<>(categoryService.readById(id), HttpStatus.OK);
        } catch (CategoryNotFoundException e) {
            return new ResponseEntity<>(new ErrorMessageResponse("ID", "Category doesn't exists with id: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateCategoryRequest request){
        try{
            return new ResponseEntity<>(categoryService.update(request), HttpStatus.OK);
        } catch (CategoryNotFoundException e) {
            return new ResponseEntity<>(new ErrorMessageResponse("ID", "Category doesn't exists with id: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        try{
            categoryService.delete(id);
        } catch (CategoryNotFoundException e) {
            return new ResponseEntity<>(new ErrorMessageResponse("ID", "Department doesn't exists with id: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new MessageResponse("Successfully deleted"), HttpStatus.OK);
    }
}
