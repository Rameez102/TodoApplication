package com.todo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.todo.entities.Item;


@Repository
public interface ItemRepository extends CrudRepository<Item, Integer> {
	List<Item> findAllByOrderByDoneAscCreatedOnDesc();
}