package com.example.sources.controller;

import com.example.sources.model.request.comment.CreateCommentRequest;
import com.example.sources.model.request.comment.UpdateCommentRequest;
import com.example.sources.model.response.ErrorMessageResponse;
import com.example.sources.model.response.MessageResponse;
import com.example.sources.service.CommentService;
import com.example.sources.utils.exception.AccountNotFoundException;
import com.example.sources.utils.exception.CommentNotFoundException;
import com.example.sources.utils.exception.ItemNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateCommentRequest request){
        try{
            return new ResponseEntity<>(commentService.create(request), HttpStatus.OK);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(new ErrorMessageResponse("ITEM_ID", "Item doesn't exists with id: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(new ErrorMessageResponse("ACCOUNT_ID", "Account doesn't exists with id: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping
    public ResponseEntity<?> readAll(){
        return new ResponseEntity<>(commentService.readAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> readById(@PathVariable Long id){
        try{
            return new ResponseEntity<>(commentService.readById(id), HttpStatus.OK);
        } catch (CommentNotFoundException e) {
            return new ResponseEntity<>(new ErrorMessageResponse("ID", "Comment doesn't exists with id: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/item/{id}")
    public ResponseEntity<?> readByItemId(@PathVariable Long id){
        return new ResponseEntity<>(commentService.readByItemId(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateCommentRequest request){
        try{
            return new ResponseEntity<>(commentService.update(request), HttpStatus.OK);
        } catch (CommentNotFoundException e) {
            return new ResponseEntity<>(new ErrorMessageResponse("ID", "Comment doesn't exists with id: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        try{
            commentService.delete(id);
        } catch (CommentNotFoundException e) {
            return new ResponseEntity<>(new ErrorMessageResponse("ID", "Comment doesn't exists with id: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new MessageResponse("Successfully deleted"), HttpStatus.OK);
    }
}
