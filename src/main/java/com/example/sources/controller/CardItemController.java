package com.example.sources.controller;

import com.example.sources.model.request.card_item.CreateCardItemRequest;
import com.example.sources.model.request.card_item.UpdateCardItemRequest;
import com.example.sources.model.response.ErrorMessageResponse;
import com.example.sources.model.response.MessageResponse;
import com.example.sources.service.CardItemService;
import com.example.sources.utils.exception.AccountNotFoundException;
import com.example.sources.utils.exception.CardItemNotFoundException;
import com.example.sources.utils.exception.ItemNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/card_item")
public class CardItemController {
    private final CardItemService cardItemService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateCardItemRequest request){
        try {
            return new ResponseEntity<>(cardItemService.create(request), HttpStatus.OK);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(new ErrorMessageResponse("ITEM_ID", "Item doesn't exists with id: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(new ErrorMessageResponse("ACCOUNT_ID", "Account doesn't exists with id: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<?> readAll(){
        return new ResponseEntity<>(cardItemService.readAll(), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/account/{id}")
    public ResponseEntity<?> readByAccountId(@PathVariable Long id){
        return new ResponseEntity<>(cardItemService.readByAccountId(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> readById(@PathVariable Long id){
        try{
            return new ResponseEntity<>(cardItemService.readById(id), HttpStatus.OK);
        } catch (CardItemNotFoundException e) {
            return new ResponseEntity<>(new ErrorMessageResponse("ID", "CardItem doesn't exists with id: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateCardItemRequest request){
        try{
            return new ResponseEntity<>(cardItemService.update(request), HttpStatus.OK);
        } catch (CardItemNotFoundException e) {
            return new ResponseEntity<>(new ErrorMessageResponse("ID", "CardItem doesn't exists with id: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        try{
            cardItemService.delete(id);
        } catch (CardItemNotFoundException e) {
            return new ResponseEntity<>(new ErrorMessageResponse("ID", "CardItem doesn't exists with id: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new MessageResponse("Successfully deleted"), HttpStatus.OK);
    }
}
