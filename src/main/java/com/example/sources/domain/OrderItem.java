package com.example.sources.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "OrderItem")
public class OrderItem {
    @Id
    @Setter(AccessLevel.PROTECTED)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orderId", referencedColumnName = "id")
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "itemId", referencedColumnName = "id")
    private Item item;
    private Integer count;
    @Enumerated(EnumType.STRING)
    private OrderState state;
}
