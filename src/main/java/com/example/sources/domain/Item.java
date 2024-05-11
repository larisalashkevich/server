package com.example.sources.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Item")
public class Item {
    @Id
    @Setter(AccessLevel.PROTECTED)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String imagePath;
    private Double price;
    @Column(length = 10000)
    private String description;
    @ManyToOne
    @JoinColumn(name = "categoryId", referencedColumnName = "id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "creatorId", referencedColumnName = "id")
    private ProfileInfo creator;
    private Date created;
    private Boolean isDeleted;
}
