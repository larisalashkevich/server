package com.example.sources.controller;

import com.example.sources.model.request.item.CreateItemRequest;
import com.example.sources.model.request.item.UpdateItemRequest;
import com.example.sources.model.response.ErrorMessageResponse;
import com.example.sources.model.response.MessageResponse;
import com.example.sources.service.ItemService;
import com.example.sources.utils.exception.AccountNotFoundException;
import com.example.sources.utils.exception.CategoryNotFoundException;
import com.example.sources.utils.exception.ItemNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/item")
public class ItemController {
    private final ItemService itemService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateItemRequest request){
        try{
            return new ResponseEntity<>(itemService.create(request), HttpStatus.OK);
        } catch (CategoryNotFoundException e) {
            return new ResponseEntity<>(new ErrorMessageResponse("CATEGORY_ID", "Category doesn't exists with id: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(new ErrorMessageResponse("ACCOUNT_ID", "Account doesn't exists with id: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<?> readAll(){
        return new ResponseEntity<>(itemService.readAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> readById(@PathVariable Long id){
        try{
            return new ResponseEntity<>(itemService.readById(id), HttpStatus.OK);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(new ErrorMessageResponse("ID", "Item doesn't exists with id: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/account/{id}")
    public ResponseEntity<?> readByAccountId(@PathVariable Long id){
        return new ResponseEntity<>(itemService.readByAccountId(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateItemRequest request){
        try{
            return new ResponseEntity<>(itemService.update(request), HttpStatus.OK);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(new ErrorMessageResponse("ID", "Item doesn't exists with id: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (CategoryNotFoundException e) {
            return new ResponseEntity<>(new ErrorMessageResponse("CATEGORY_ID", "Category doesn't exists with id: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        try{
            itemService.delete(id);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(new ErrorMessageResponse("ID", "Item doesn't exists with id: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new MessageResponse("Successfully deleted"), HttpStatus.OK);
    }
}
