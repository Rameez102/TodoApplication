package com.todo.assignment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.todo.entities.Item;
import com.todo.repository.ItemRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TodoApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class TodoApplicationTests{
	
	@LocalServerPort
	private int port;
    
    private TestRestTemplate testRestTemplate = new TestRestTemplate("Belal", "123");
    
    private static final String URL = "/todo/items/";
    
    
    @Autowired
    private ItemRepository repo;
    

 
 
    public void prepare() {
    	List<Item> items = new ArrayList<Item>();
        
    	Item item = new Item();
        item.setItemId(1);
        item.setTodo("Test Todo");
        item.setCreatedOn(new Date());
        item.setDone(false);
        item.setUserId("Belal");
        
        Item item2 = new Item();
        item2.setItemId(2);
        item2.setTodo("Test Todo 2");
        item2.setCreatedOn(new Date());
        item2.setDone(false);
        item2.setUserId("Belal");
        
        items.add(item);
        items.add(item2);
        
        repo.saveAll(items);
    }
    
      
    @Test
    public void addItem() throws Exception {
    	 Item item = new Item();
       item.setItemId(3);
       item.setTodo("Test Todo");
       item.setCreatedOn(new Date());
       item.setDone(false);
       item.setUserId("Belal");
    
     // execute
     ResponseEntity<Item> responseEntity = testRestTemplate.postForEntity(getUrl(URL), 
    		 item, 
    		 Item.class);
    
     // collect Response
     int status = responseEntity.getStatusCodeValue();
     Item resultItem = responseEntity.getBody();
    
     // verify
     assertEquals("Incorrect Response Status", HttpStatus.CREATED.value(), status);
    
     assertNotNull(resultItem);
     assertNotNull(resultItem.getItemId().intValue());
    
   }
    
    
    
    
    @Test
    public void deleteItem() throws Exception {
    
     // execute - delete the record added while initializing database with
     // test data
     ResponseEntity<Void> responseEntity = testRestTemplate.exchange(getUrl(URL) + "{itemId}", 
     HttpMethod.DELETE, 
     null, 
     Void.class,
     new Integer(1));
    
     // verify
     int status = responseEntity.getStatusCodeValue();
     assertEquals("Incorrect Response Status", HttpStatus.GONE.value(), status);
    
   }
    
    
    @Test
    public void updateItem() throws Exception {
    	Item item = new Item();
        item.setItemId(999);
        item.setTodo("Test Todo");
        item.setCreatedOn(new Date());
        item.setDone(false);
        item.setUserId("Belal");
        
     HttpEntity<Item> requestEntity = new HttpEntity<Item>(item);
    
     // execute
     ResponseEntity<Item> responseEntity = testRestTemplate.exchange(getUrl(URL) + "{itemId}", 
     HttpMethod.POST, 
     requestEntity, 
     Item.class,
     new Integer(1));
    
     // verify
     int status = responseEntity.getStatusCodeValue();
     assertEquals("Incorrect Response Status", HttpStatus.OK.value(), status);
   }
    
    
    @Test
    public void retrieveItem() throws Exception {
    
    	Item item = new Item();
        item.setItemId(16);
        item.setTodo("Test Todo");
        item.setCreatedOn(new Date());
        item.setDone(false);
        item.setUserId("Belal");
    
     // execute
     ResponseEntity<Item> responseEntity = testRestTemplate.getForEntity(getUrl(URL) + "{itemId}", 
    		 Item.class, 
     new Integer(1));
    
     // collect response
     int status = responseEntity.getStatusCodeValue();
     Item resultItem = responseEntity.getBody();
    
     // verify
     assertEquals("Incorrect Response Status", HttpStatus.OK.value(), status);
    
     assertNotNull(resultItem);
     assertEquals(1, resultItem.getItemId().intValue());
    
   }
    
    @Test
    public void retrieveItems() throws Exception {
    	prepare();
        ResponseEntity<List<Item>> responseEntity = testRestTemplate.exchange(
        		getUrl(URL),
        		  HttpMethod.GET,
        		  null,
        		  new ParameterizedTypeReference<List<Item>>(){});   
        
        
    
    
     // collect response
     int status = responseEntity.getStatusCodeValue();
     List<Item> resultItem = responseEntity.getBody();
    
     // verify
     assertEquals("Incorrect Response Status", HttpStatus.OK.value(), status);
    
     assertNotNull(resultItem);
     assertThat(resultItem.size()).isEqualTo(2);
    
   }
    
    @Test
    public void all() throws Exception {
    	prepare();
    	TestRestTemplate template = new TestRestTemplate("Ahmad", "123");
        ResponseEntity<List<Item>> responseEntity = template.exchange(
        		getUrl(URL) + "all",
        		  HttpMethod.GET,
        		  null,
        		  new ParameterizedTypeReference<List<Item>>(){});   
        
        
    
    
     // collect response
     int status = responseEntity.getStatusCodeValue();
     List<Item> resultItem = responseEntity.getBody();
    
     // verify
     assertEquals("Incorrect Response Status", HttpStatus.OK.value(), status);
    
     assertNotNull(resultItem);
     assertThat(resultItem.size()).isEqualTo(2);
    
   }
    
//    @Test
//    public void addComment() throws Exception {
//    	 Comment comment = new Comment();
//	       comment.setCommnetId(1);
//	       comment.setCommentDate(new Date());
//	       comment.setComment("New Comment");
//	       
//	     
//	       
//     // execute
//	       
//	       HttpEntity<Comment> requestEntity = new HttpEntity<Comment>(comment);
//	       ResponseEntity<Item> responseEntity = testRestTemplate.postForEntity(getUrl(URL) + "{itemId}/comments", 
//	    		   requestEntity,
//	    		   Item.class,
//	    		   new Integer(1)
//	    		     );
//	      
//	       
//
//     int status = responseEntity.getStatusCodeValue();
//     Item resultComment = responseEntity.getBody();
//    
//     // verify
//     assertEquals("Incorrect Response Status", HttpStatus.CREATED.value(), status);
//    
//     assertNotNull(resultComment);
//     assertNotNull(resultComment.getItemId().intValue());
//    
//   }
    
   
    
    
    
    private String getUrl(String requestUrl) {
    	return "http://localhost:"+port+requestUrl;
    }
}


