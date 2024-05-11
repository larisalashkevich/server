package com.example.sources.model.response.order_entity;

import com.example.sources.domain.OrderEntity;
import com.example.sources.domain.OrderItem;
import com.example.sources.model.response.order_item.OrderItemResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderEntityResponse {
    private Long id;
    private Long accountId;
    private Date ordered;
    private String state;
    private Double totalPrice;
    private String address;
    private List<OrderItemResponse> items;

    public OrderEntityResponse(OrderEntity orderEntity){
        Hibernate.initialize(orderEntity.getOwner());
        Hibernate.initialize(orderEntity.getOwner().getAccount());
        Hibernate.initialize(orderEntity.getItems());

        this.id = orderEntity.getId();
        this.accountId = orderEntity.getOwner().getAccount().getId();
        this.ordered = orderEntity.getOrdered();
        this.state = orderEntity.getState().toString();
        this.totalPrice = orderEntity.getTotalPrice();
        this.address = orderEntity.getAddress();
        this.items = new ArrayList<>();

        for(OrderItem item : orderEntity.getItems()){
            items.add(new OrderItemResponse(item));
        }
    }
}
