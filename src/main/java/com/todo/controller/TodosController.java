package com.todo.controller;


import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todo.entities.Comment;
import com.todo.entities.Item;
import com.todo.repository.CommentRepository;
import com.todo.repository.ItemRepository;

@RequestMapping("/todo")
@RestController
public class TodosController {

	@Autowired
	private ItemRepository itemRepo;
	
	@Autowired
	private CommentRepository commentRepo;

	
    @GetMapping(value="/items")
    public Iterable<Item> items() {
    	return itemRepo.findAllByOrderByDoneAscCreatedOnDesc();
    }
    
    @CrossOrigin
	@PostMapping("/items")
	public void add(@RequestBody Item item) {
		Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();
		
		item.setUserId(auth.getName());
		item.setCreatedOn(new Date());
		 itemRepo.save(item);
	}

	@DeleteMapping(value="/items/{itemId}")
    public void delete(@PathVariable Integer itemId){
    		this.itemRepo.deleteById(itemId);
    }
    
    @GetMapping("/items/{itemId}")
    public Item retrieve(@PathVariable Integer itemId) {
    	Optional<Item> item = itemRepo.findById(itemId);

    	if (!item.isPresent())
    		throw new RuntimeException("id-" + itemId + " Not Found");

    	return item.get();
    }
    
    @PostMapping("/items/{itemId}")
    public Item update(@RequestBody Item item, @PathVariable Integer itemId) {
    	
    	Optional<Item> existingItem = itemRepo.findById(itemId);
    	 
    	if (!existingItem.isPresent())
    		throw new RuntimeException("id-" + itemId + " Not Found");
  
    	item.setItemId(existingItem.get().getItemId());
    	item.setCreatedOn(existingItem.get().getCreatedOn());
    	item.setUserId(existingItem.get().getUserId());
    	item.setComments(existingItem.get().getComments());
    	if(StringUtils.isEmpty(item.getDone())) {
    		item.setDone(existingItem.get().getDone());
    	}
    	
        itemRepo.save(item);
        
         return itemRepo.findById(itemId).get();
    }
    
    @PostMapping("/items/{itemId}/comments")
    public Item addComment(@RequestBody Comment itemComment, @PathVariable Integer itemId) {
    	
    	Optional<Item> existingItem = itemRepo.findById(itemId);
    	 
    	if (!existingItem.isPresent())
    		throw new RuntimeException("id-" + itemId + " Not Found");
    	itemComment.setItemId(existingItem.get());
    	commentRepo.save(itemComment);
    	existingItem.get().setComments(commentRepo.findCommentByItemIdItemId(existingItem.get().getItemId()));
    	return existingItem.get();
    }
    
    @GetMapping(value="/items/all")
    public Iterable<Item> allItems() {
    	return itemRepo.findAllByOrderByDoneAscCreatedOnDesc();
    }
    
}