package com.example.sources.service;

import com.example.sources.domain.Category;
import com.example.sources.domain.Item;
import com.example.sources.domain.ProfileInfo;
import com.example.sources.model.request.item.CreateItemRequest;
import com.example.sources.model.request.item.UpdateItemRequest;
import com.example.sources.model.response.item.ItemResponse;
import com.example.sources.repo.CategoryRepo;
import com.example.sources.repo.ItemRepo;
import com.example.sources.repo.ProfileInfoRepo;
import com.example.sources.utils.exception.AccountNotFoundException;
import com.example.sources.utils.exception.CategoryNotFoundException;
import com.example.sources.utils.exception.ItemNotFoundException;
import com.example.sources.utils.files.FilePathResolver;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ItemService {
    private final ItemRepo itemRepo;
    private final CategoryRepo categoryRepo;
    private final ProfileInfoRepo profileInfoRepo;

    public ItemResponse create(CreateItemRequest request) throws CategoryNotFoundException, AccountNotFoundException {
        Optional<Category> category = categoryRepo.findById(request.getCategoryId());

        if(!category.isPresent())
            throw new CategoryNotFoundException(request.getCategoryId().toString());

        ProfileInfo creator = profileInfoRepo.findByAccount_Id(request.getAccountId());

        if(creator == null)
            throw new AccountNotFoundException(request.getAccountId().toString());

        Item item = Item.builder()
                .name(request.getName())
                .price(request.getPrice())
                .imagePath(request.getImagePath())
                .description(request.getDescription())
                .category(category.get())
                .created(new Date())
                .isDeleted(false)
                .creator(creator)
                .build();

        return new ItemResponse(itemRepo.save(item));
    }

    public List<ItemResponse> readAll() {
        List<Item> items = itemRepo.findAll();

        List<ItemResponse> response = new ArrayList<>();
        for(Item item : items)
            if(!item.getIsDeleted())
                response.add(new ItemResponse(item));

        return response;
    }

    public List<ItemResponse> readByAccountId(Long id) {
        List<Item> items = itemRepo.findByCreator_Account_Id(id);

        List<ItemResponse> response = new ArrayList<>();
        for(Item item : items)
            if(!item.getIsDeleted())
                response.add(new ItemResponse(item));

        return response;
    }

    public ItemResponse readById(Long id) throws ItemNotFoundException {
        Optional<Item> state = itemRepo.findById(id);

        if(!state.isPresent())
            throw new ItemNotFoundException(id.toString());

        return new ItemResponse(state.get());
    }

    public ItemResponse update(UpdateItemRequest request) throws ItemNotFoundException, CategoryNotFoundException {
        Optional<Item> oldState = itemRepo.findById(request.getId());

        if(!oldState.isPresent())
            throw new ItemNotFoundException(request.getId().toString());

        Optional<Category> category = categoryRepo.findById(request.getCategoryId());

        if(!category.isPresent())
            throw new CategoryNotFoundException(request.getCategoryId().toString());

        Item newState = oldState.get();
        Hibernate.initialize(newState.getCreator());
        newState.setName(request.getName());
        newState.setPrice(request.getPrice());
        if(newState.getImagePath() == null ||!request.getImagePath().contains(newState.getImagePath())){
            FilePathResolver.deleteFile(newState.getImagePath());
            newState.setImagePath(request.getImagePath());
        }
        newState.setDescription(request.getDescription());
        newState.setCategory(category.get());

        return new ItemResponse(itemRepo.save(newState));
    }

    public void delete(Long id) throws ItemNotFoundException {
        Optional<Item> oldState = itemRepo.findById(id);

        if(!oldState.isPresent())
            throw new ItemNotFoundException(id.toString());

        Item newState = oldState.get();
        Hibernate.initialize(newState.getCreator());
        Hibernate.initialize(newState.getCategory());
        newState.setIsDeleted(true);

        newState = itemRepo.save(newState);
    }
}
