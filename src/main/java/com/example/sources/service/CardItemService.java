package com.example.sources.service;

import com.example.sources.domain.CardItem;
import com.example.sources.domain.Item;
import com.example.sources.domain.ProfileInfo;
import com.example.sources.model.request.card_item.CreateCardItemRequest;
import com.example.sources.model.request.card_item.UpdateCardItemRequest;
import com.example.sources.model.response.card_item.CardItemResponse;
import com.example.sources.repo.CardItemRepo;
import com.example.sources.repo.ItemRepo;
import com.example.sources.repo.ProfileInfoRepo;
import com.example.sources.utils.exception.AccountNotFoundException;
import com.example.sources.utils.exception.CardItemNotFoundException;
import com.example.sources.utils.exception.ItemNotFoundException;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CardItemService {
    private final CardItemRepo cardItemRepo;
    private final ProfileInfoRepo profileInfoRepo;
    private final ItemRepo itemRepo;

    public CardItemResponse create(CreateCardItemRequest request) throws ItemNotFoundException, AccountNotFoundException {
        Optional<Item> item = itemRepo.findById(request.getItemId());

        if(!item.isPresent())
            throw new ItemNotFoundException(request.getItemId().toString());

        ProfileInfo profileInfo = profileInfoRepo.findByAccount_Id(request.getAccountId());

        if(profileInfo == null)
            throw new AccountNotFoundException(request.getAccountId().toString());

        List<CardItem> items = cardItemRepo.findByOwner_Account_Id(request.getAccountId());
        for(CardItem cardItem: items){
            Hibernate.initialize(cardItem.getItem());
            if(cardItem.getItem().equals(item.get())){
                Hibernate.initialize(cardItem.getOwner());
                cardItem.setCount(cardItem.getCount() + 1);

                return new CardItemResponse(cardItemRepo.save(cardItem));
            }
        }
        CardItem cardItem = CardItem.builder()
                .count(request.getCount())
                .item(item.get())
                .owner(profileInfo)
                .build();

        return new CardItemResponse(cardItemRepo.save(cardItem));
    }

    public List<CardItemResponse> readAll(){
        List<CardItem> cardItems = cardItemRepo.findAll();

        List<CardItemResponse> response = new ArrayList<>();
        for(CardItem cardItem : cardItems)
            response.add(new CardItemResponse(cardItem));

        return response;
    }
    public List<CardItemResponse> readByAccountId(Long id){
        List<CardItem> cardItems = cardItemRepo.findByOwner_Account_Id(id);

        List<CardItemResponse> response = new ArrayList<>();
        for(CardItem cardItem : cardItems)
            response.add(new CardItemResponse(cardItem));

        return response;
    }
    public CardItemResponse readById(Long id) throws CardItemNotFoundException {
        Optional<CardItem> cardItem = cardItemRepo.findById(id);

        if(!cardItem.isPresent())
            throw new CardItemNotFoundException(id.toString());

        return new CardItemResponse(cardItem.get());
    }
    public CardItemResponse update(UpdateCardItemRequest request) throws CardItemNotFoundException {
        Optional<CardItem> oldState = cardItemRepo.findById(request.getId());

        if(!oldState.isPresent())
            throw new CardItemNotFoundException(request.getId().toString());

        CardItem newState = oldState.get();
        newState.setCount(request.getCount());

        newState = cardItemRepo.save(newState);

        return new CardItemResponse(newState);
    }

    public void delete(Long id) throws CardItemNotFoundException {
        Optional<CardItem> cardItem = cardItemRepo.findById(id);

        if(!cardItem.isPresent())
            throw new CardItemNotFoundException(id.toString());

        cardItemRepo.deleteById(id);
    }
}
