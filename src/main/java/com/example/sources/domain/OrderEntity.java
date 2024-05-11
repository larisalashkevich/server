package com.example.sources.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "OrderEntity")
public class OrderEntity {
    @Id
    @Setter(AccessLevel.PROTECTED)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ownerId", referencedColumnName = "id")
    private ProfileInfo owner;
    private Date ordered;
    @Enumerated(EnumType.STRING)
    private OrderState state;
    private Double totalPrice;
    private String address;

    @OneToMany
    private List<OrderItem> items;
}
