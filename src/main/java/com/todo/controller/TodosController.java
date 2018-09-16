package com.todo.controller;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<Item>> items() {
    	List<Item> items = itemRepo.findAllByOrderByDoneAscCreatedOnDesc();
    	return new ResponseEntity<List<Item>>(items, HttpStatus.OK);
    }
    
    @CrossOrigin
	@PostMapping("/items")
	public ResponseEntity<Item> add(@RequestBody Item item) {
		Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();
		item.setUserId(auth.getName());
		item.setCreatedOn(new Date());
		if(StringUtils.isEmpty(item.getDone())) {
    		item.setDone(false);
    	}
		itemRepo.save(item);
		return new ResponseEntity<Item>(item, HttpStatus.CREATED); 
	}

	@DeleteMapping(value="/items/{itemId}")
    public ResponseEntity<Void> delete(@PathVariable Integer itemId){
		Optional<Item> existingItem = itemRepo.findById(itemId);
		  if (!existingItem.isPresent()) {
			  return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		  } else {
		  itemRepo.delete(existingItem.get());
		  }
		  return new ResponseEntity<Void>(HttpStatus.GONE);
    }
    
    @GetMapping("/items/{itemId}")
    public ResponseEntity<Item> retrieve(@PathVariable Integer itemId) {
    	Optional<Item> item = itemRepo.findById(itemId);

    	if (!item.isPresent()) {
    		return new ResponseEntity<Item>(HttpStatus.NOT_FOUND);
    	}
    		

    	return new ResponseEntity<Item>(item.get(), HttpStatus.OK);
    }
    
    @PostMapping("/items/{itemId}")
    public ResponseEntity<Item> update(@RequestBody Item item, @PathVariable Integer itemId) {
    	
    	Optional<Item> existingItem = itemRepo.findById(itemId);
   	 
    	if (!existingItem.isPresent())
    		return new ResponseEntity<Item>(HttpStatus.NOT_FOUND);
    	else {
    		item.setItemId(existingItem.get().getItemId());
        	item.setCreatedOn(existingItem.get().getCreatedOn());
        	item.setUserId(existingItem.get().getUserId());
        	item.setComments(existingItem.get().getComments());
        	if(StringUtils.isEmpty(item.getDone())) {
        		item.setDone(existingItem.get().getDone());
        	}
            itemRepo.save(item);
            return new ResponseEntity<Item>(HttpStatus.OK);
    	}
    	
        
         
    }
    
    @PostMapping("/items/{itemId}/comments")
    public ResponseEntity<Item> addComment(@RequestBody Comment itemComment, @PathVariable Integer itemId) {
    	
    	Optional<Item> existingItem = itemRepo.findById(itemId);
    	 
    	if (!existingItem.isPresent())
    		return new ResponseEntity<Item>(HttpStatus.NOT_FOUND);
    	else {
    	itemComment.setItemId(existingItem.get());
    	commentRepo.save(itemComment);
    	existingItem.get().setComments(commentRepo.findCommentByItemIdItemId(existingItem.get().getItemId()));
    	}
    	return new ResponseEntity<Item>(existingItem.get(), HttpStatus.CREATED); 
    }
    
    @GetMapping(value="/items/all")
    public ResponseEntity<List<Item>> allItems() {
    	List<Item> items = itemRepo.findAllByOrderByDoneAscCreatedOnDesc();
    	return new ResponseEntity<List<Item>>(items, HttpStatus.OK);
    }
    
}