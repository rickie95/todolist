package com.riccardomalavolti.apps.todolist.repositories;

import java.util.List;

import com.riccardomalavolti.apps.todolist.Tag;
import com.riccardomalavolti.apps.todolist.Todo;

public interface TodoRepository {
	
	List<Todo> findAll();
	List<Todo> findByTag(Tag t);
	List<Todo> findByBody(String text);
	
	Todo findById(Todo te);
	
	void addTodoElement(Todo te);
	void updateTodoElement(Todo te);
	void removeTodoElement(Todo te);
	
	void clear();
}
