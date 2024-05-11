package com.example.sources.controller;

import com.example.sources.model.request.card_item.UpdateCardItemRequest;
import com.example.sources.model.request.order_item.UpdateOrderItemRequest;
import com.example.sources.model.response.ErrorMessageResponse;
import com.example.sources.service.OrderService;
import com.example.sources.utils.exception.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/{accountId}")
    public ResponseEntity<?> create(@PathVariable Long accountId){
        try {
            return new ResponseEntity<>(orderService.create(accountId), HttpStatus.OK);
        } catch (ProfileInfoNotFoundException e) {
            return new ResponseEntity<>(new ErrorMessageResponse("ACCOUNT_ID", "Profile info doesn't exists with accountId: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (CardItemsEmptyException e) {
            return new ResponseEntity<>(new ErrorMessageResponse("CARD_ITEM", "No cardItems with accountId: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (NotDeletedCardItemsEmptyException e) {
            return new ResponseEntity<>(new ErrorMessageResponse("CARD_ITEM_NOT_DELETED", "No undeleted cardItems with accountId: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<?> readAll(){
        return new ResponseEntity<>(orderService.readAll(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/account/{id}")
    public ResponseEntity<?> readByAccountId(@PathVariable Long id){
        return new ResponseEntity<>(orderService.readByAccountId(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/account/{id}/{state}")
    public ResponseEntity<?> readByAccountId(@PathVariable Long id, @PathVariable String state){
        return new ResponseEntity<>(orderService.readByState(id, state), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> readById(@PathVariable Long id){
        try {
            return new ResponseEntity<>(orderService.readById(id), HttpStatus.OK);
        } catch (OrderEntityNotFoundException e) {
            return new ResponseEntity<>(new ErrorMessageResponse("ID", "OrderEntity doesn't exists with id: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/item/seller/{id}")
    public ResponseEntity<?> readItemsBySellerId(@PathVariable Long id){
        return new ResponseEntity<>(orderService.readItemsBySellerId(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/item/seller/{id}/{state}")
    public ResponseEntity<?> readItemsBySellerId(@PathVariable Long id, @PathVariable String state){
        return new ResponseEntity<>(orderService.readItemsBySellerIdAndState(id, state), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateOrderItemRequest request){
        try{
            return new ResponseEntity<>(orderService.updateItem(request), HttpStatus.OK);
        } catch (OrderItemNotFoundException e) {
            return new ResponseEntity<>(new ErrorMessageResponse("ID", "OrderItem doesn't exists with accountId: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
