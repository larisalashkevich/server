package com.example.sources.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Comment")
public class Comment {
    @Id
    @Setter(AccessLevel.PROTECTED)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "creatorId", referencedColumnName = "id")
    private ProfileInfo creator;
    @ManyToOne
    @JoinColumn(name = "itemId", referencedColumnName = "id")
    private Item item;
    private Date created;
    @Column(length = 10000)
    private String text;
}
