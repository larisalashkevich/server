package com.example.sources.service;

import com.example.sources.domain.Comment;
import com.example.sources.domain.Item;
import com.example.sources.domain.ProfileInfo;
import com.example.sources.model.request.comment.CreateCommentRequest;
import com.example.sources.model.request.comment.UpdateCommentRequest;
import com.example.sources.model.response.comment.CommentResponse;
import com.example.sources.repo.CommentRepo;
import com.example.sources.repo.ItemRepo;
import com.example.sources.repo.ProfileInfoRepo;
import com.example.sources.utils.exception.AccountNotFoundException;
import com.example.sources.utils.exception.CommentNotFoundException;
import com.example.sources.utils.exception.ItemNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepo commentRepo;
    private final ItemRepo itemRepo;
    private final ProfileInfoRepo profileInfoRepo;

    public CommentResponse create(CreateCommentRequest request) throws ItemNotFoundException, AccountNotFoundException {
        Optional<Item> item = itemRepo.findById(request.getItemId());

        if(!item.isPresent())
            throw new ItemNotFoundException(request.getItemId().toString());

        ProfileInfo profileInfo = profileInfoRepo.findByAccount_Id(request.getAccountId());

        if(profileInfo == null)
            throw new AccountNotFoundException(request.getAccountId().toString());

        Comment comment = Comment.builder()
                .text(request.getText())
                .created(new Date())
                .creator(profileInfo)
                .item(item.get())
                .build();

        return new CommentResponse(commentRepo.save(comment));
    }

    public List<CommentResponse> readAll(){
        List<Comment> comments = commentRepo.findAll();

        List<CommentResponse> response = new ArrayList<>();
        for(Comment comment : comments)
            response.add(new CommentResponse(comment));

        return response;
    }

    public List<CommentResponse> readByItemId(Long itemId){
        List<Comment> comments = commentRepo.findByItem_Id(itemId);

        List<CommentResponse> response = new ArrayList<>();
        for(Comment comment : comments)
            response.add(new CommentResponse(comment));

        return response;
    }
    public CommentResponse readById(Long id) throws CommentNotFoundException {
        Optional<Comment> comment = commentRepo.findById(id);

        if(!comment.isPresent())
            throw new CommentNotFoundException(id.toString());

        return new CommentResponse(comment.get());
    }
    public CommentResponse update(UpdateCommentRequest request) throws CommentNotFoundException {
        Optional<Comment> oldState = commentRepo.findById(request.getId());

        if(!oldState.isPresent())
            throw new CommentNotFoundException(request.getId().toString());

        Optional<Comment> category = commentRepo.findById(request.getId());

        Comment newState = oldState.get();
        newState.setText(request.getText());

        newState = commentRepo.save(newState);

        return new CommentResponse(newState);
    }

    public void delete(Long id) throws CommentNotFoundException {
        Optional<Comment> comment = commentRepo.findById(id);

        if(!comment.isPresent())
            throw new CommentNotFoundException(id.toString());

        commentRepo.deleteById(id);
    }
}
