package com.example.sources.model.response.order_item;

import com.example.sources.domain.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.Hibernate;

@Data
@AllArgsConstructor
public class OrderItemResponse {
    private Long id;
    private Long orderId;
    private Long itemId;
    private Integer count;
    private String state;

    public OrderItemResponse(OrderItem item){
        Hibernate.initialize(item.getOrder());
        Hibernate.initialize(item.getItem());

        this.id = item.getId();
        this.orderId = item.getOrder().getId();
        this.itemId = item.getItem().getId();
        this.count = item.getCount();
        this.state = item.getState().toString();
    }
}
