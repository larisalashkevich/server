package com.example.sources.model.response.comment;

import com.example.sources.domain.Comment;
import com.example.sources.domain.ProfileInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.Hibernate;

import java.util.Date;

@Data
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private Long itemId;
    private String text;
    private Date created;
    private ProfileInfo creator;

    public CommentResponse(Comment comment){
        Hibernate.initialize(comment.getItem());
        Hibernate.initialize(comment.getCreator());
        Hibernate.initialize(comment.getCreator().getAccount());

        this.id = comment.getId();
        this.itemId = comment.getItem().getId();
        this.text = comment.getText();
        this.created = comment.getCreated();
        this.creator = comment.getCreator();
    }
}
