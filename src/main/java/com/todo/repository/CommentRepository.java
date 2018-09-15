package com.todo.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.todo.entities.Comment;
import com.todo.entities.Item;


@Repository
public interface CommentRepository extends CrudRepository<Comment, Integer> {

	Set<Comment> findCommentByItemIdItemId(Integer itemId);
	
}