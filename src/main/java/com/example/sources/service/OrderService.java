package com.example.sources.service;

import com.example.sources.domain.*;
import com.example.sources.model.request.order_item.UpdateOrderItemRequest;
import com.example.sources.model.response.order_entity.OrderEntityResponse;
import com.example.sources.model.response.order_item.OrderItemResponse;
import com.example.sources.repo.CardItemRepo;
import com.example.sources.repo.OrderEntityRepo;
import com.example.sources.repo.OrderItemRepo;
import com.example.sources.repo.ProfileInfoRepo;
import com.example.sources.utils.exception.*;
import lombok.AllArgsConstructor;
import org.apache.xpath.operations.Bool;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderItemRepo orderItemRepo;
    private final OrderEntityRepo orderEntityRepo;
    private final CardItemRepo cardItemRepo;
    private final ProfileInfoRepo profileInfoRepo;

    public OrderEntityResponse create(Long accountId) throws CardItemsEmptyException, NotDeletedCardItemsEmptyException, ProfileInfoNotFoundException {
        List<CardItem> cardItems = cardItemRepo.findByOwner_Account_Id(accountId);

        if(cardItems.isEmpty())
            throw new CardItemsEmptyException(accountId.toString());

        List<CardItem> notDeletedCardItems = new ArrayList<>();
        for(CardItem cardItem: cardItems){
            Hibernate.initialize(cardItem.getItem());
            if(!cardItem.getItem().getIsDeleted())
                notDeletedCardItems.add(cardItem);
        }

        if(notDeletedCardItems.isEmpty())
            throw new NotDeletedCardItemsEmptyException(accountId.toString());

        ProfileInfo profileInfo = profileInfoRepo.findByAccount_Id(accountId);
        if(profileInfo == null)
            throw new ProfileInfoNotFoundException(accountId.toString());

        Double totalPrice = 0.;
        for(CardItem cardItem: notDeletedCardItems){
            totalPrice += cardItem.getItem().getPrice();
        }

        OrderEntity orderEntity = OrderEntity.builder()
                .ordered(new Date())
                .owner(profileInfo)
                .state(OrderState.NEW)
                .totalPrice(totalPrice)
                .address(profileInfo.getAddress())
                .build();

        orderEntity = orderEntityRepo.save(orderEntity);

        for(CardItem cardItem : notDeletedCardItems){
            OrderItem orderItem = OrderItem.builder()
                    .order(orderEntity)
                    .item(cardItem.getItem())
                    .count(cardItem.getCount())
                    .state(OrderState.NEW)
                    .build();

            orderItem = orderItemRepo.save(orderItem);
        }

        for(CardItem cardItem : cardItems){
            cardItemRepo.delete(cardItem);
        }

        return new OrderEntityResponse(orderEntity);
    }

    public List<OrderEntityResponse> readAll(){
        List<OrderEntity> orderEntities = orderEntityRepo.findAll();

        List<OrderEntityResponse> response = new ArrayList<>();
        for(OrderEntity orderEntity : orderEntities)
            response.add(new OrderEntityResponse(orderEntity));

        return response;
    }

    public List<OrderEntityResponse> readByAccountId(Long accountId){
        List<OrderEntity> orderEntities = orderEntityRepo.findByOwner_Account_Id(accountId);

        List<OrderEntityResponse> response = new ArrayList<>();
        for(OrderEntity orderEntity : orderEntities)
            response.add(new OrderEntityResponse(orderEntity));

        return response;
    }
    public List<OrderEntityResponse> readByState(Long accountId, String state){
        OrderState orderState = OrderState.fromString(state);

        List<OrderEntity> orderEntities = orderEntityRepo.findByOwner_Account_Id(accountId);

        List<OrderEntityResponse> response = new ArrayList<>();
        for(OrderEntity orderEntity : orderEntities)
            if(orderEntity.getState().equals(orderState))
                response.add(new OrderEntityResponse(orderEntity));

        return response;
    }

    public OrderEntityResponse readById(Long id) throws OrderEntityNotFoundException {
        Optional<OrderEntity> orderEntity = orderEntityRepo.findById(id);

        if(!orderEntity.isPresent())
            throw new OrderEntityNotFoundException(id.toString());

        return new OrderEntityResponse(orderEntity.get());
    }

    public List<OrderItemResponse> readItemsBySellerId(Long accountId){
        List<OrderItem> orderItems = orderItemRepo.findByItem_Creator_Account_Id(accountId);

        List<OrderItemResponse> response = new ArrayList<>();
        for(OrderItem orderItem : orderItems)
            response.add(new OrderItemResponse(orderItem));

        return response;
    }

    public List<OrderItemResponse> readItemsBySellerIdAndState(Long accountId, String state){
        OrderState orderState = OrderState.fromString(state);

        List<OrderItem> orderItems = orderItemRepo.findByItem_Creator_Account_Id(accountId);

        List<OrderItemResponse> response = new ArrayList<>();
        for(OrderItem orderItem : orderItems)
            if(orderItem.getState().equals(orderState))
                response.add(new OrderItemResponse(orderItem));

        return response;
    }

    public OrderItemResponse updateItem(UpdateOrderItemRequest request) throws OrderItemNotFoundException {
        Optional<OrderItem> oldState = orderItemRepo.findById(request.getId());

        if(!oldState.isPresent())
            throw new OrderItemNotFoundException(request.getId().toString());

        OrderState newOrderState = OrderState.fromString(request.getState());

        OrderItem newState = oldState.get();
        newState.setState(newOrderState);

        Hibernate.initialize(newState.getOrder());
        Hibernate.initialize(newState.getItem());

        OrderEntity orderEntity = newState.getOrder();

        newState = orderItemRepo.save(newState);

        resolveEntityState(orderEntity);

        return new OrderItemResponse(newState);
    }

    private void resolveEntityState(OrderEntity orderEntity){
        Hibernate.initialize(orderEntity.getItems());
        Hibernate.initialize(orderEntity.getOwner());

        OrderState result = orderEntity.getState();

        Map<OrderState, Integer> statesMap = new HashMap<>();
        statesMap.put(OrderState.NEW, 0);//
        statesMap.put(OrderState.ACTIVE, 0);//
        statesMap.put(OrderState.PACKING, 0);//
        statesMap.put(OrderState.PROBLEM, 0);//
        statesMap.put(OrderState.DONE, 0);//
        statesMap.put(OrderState.HALF_DONE, 0);//
        statesMap.put(OrderState.CANCELLED, 0);//

        for(OrderItem item: orderEntity.getItems()){
            statesMap.put(item.getState(), statesMap.get(item.getState()) + 1);
        }

        if(statesMap.get(OrderState.CANCELLED).equals(orderEntity.getItems().size())){
            result = OrderState.CANCELLED;
        }
        else if(statesMap.get(OrderState.DONE).equals(orderEntity.getItems().size())){
            result = OrderState.DONE;
        }
        else if(statesMap.get(OrderState.NEW).equals(orderEntity.getItems().size())){
            result = OrderState.NEW;
        }
        else if(statesMap.get(OrderState.PROBLEM) > 0){
            result = OrderState.PROBLEM;
        }
        else if(statesMap.get(OrderState.DONE) > 0){
            result = OrderState.HALF_DONE;
        }
        else{
            result = OrderState.ACTIVE;
        }

        orderEntity.setState(result);
        orderEntityRepo.save(orderEntity);
    }
}
