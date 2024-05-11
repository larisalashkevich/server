package com.example.sources.model.response.card_item;

import com.example.sources.domain.CardItem;
import com.example.sources.domain.Item;
import com.example.sources.domain.ProfileInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.Hibernate;

@Data
@AllArgsConstructor
public class CardItemResponse {
    private Long id;
    private ProfileInfo owner;
    private Item item;
    private Integer count;

    public CardItemResponse(CardItem cardItem){
        Hibernate.initialize(cardItem.getItem());
        Hibernate.initialize(cardItem.getItem().getCategory());
        Hibernate.initialize(cardItem.getOwner());
        Hibernate.initialize(cardItem.getOwner().getAccount());

        this.id = cardItem.getId();
        this.owner = cardItem.getOwner();
        this.item = cardItem.getItem();
        this.count = cardItem.getCount();
    }
}
