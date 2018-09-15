package com.todo.assignment;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.entities.Item;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TodoApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class TodoApplicationTests {
	
	@LocalServerPort
	private int port;
    
    
    private TestRestTemplate testRestTemplate;
    
    private HttpHeaders headers = new HttpHeaders();
    
    @Test
    public void addItem() throws Exception {
    	testRestTemplate = new TestRestTemplate("Belal", "123");
    	
        Item item = new Item();
        item.setItemId(1);
        item.setTodo("Test Todo");
        item.setCreatedOn(new Date());
        item.setDone(false);
        item.setUserId("Belal");
        
        ObjectMapper mapper = new ObjectMapper();
        
        String uriToCreateItem = "/todo/items";
        HttpEntity<Item> entity = new HttpEntity<Item>(item, headers);
        ResponseEntity<String> response = testRestTemplate
        		.exchange("http://localhost:"+port+uriToCreateItem, HttpMethod.POST, entity, String.class);
        String responseInJson = response.getBody();
        assertThat(responseInJson).isEqualTo(mapper.writeValueAsString(item));
        
    }
}


