package com.example.sources.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CardItem")
public class CardItem {
    @Id
    @Setter(AccessLevel.PROTECTED)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ownerId", referencedColumnName = "id")
    private ProfileInfo owner;

    @ManyToOne
    @JoinColumn(name = "itemId", referencedColumnName = "id")
    private Item item;

    private Integer count;
}
