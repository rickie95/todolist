package com.riccardomalavolti.apps.todolist.repositories.todo;

import java.util.List;

import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.model.Todo;

public interface TodoRepository {
	
	List<Todo> findAll();
	List<Todo> findByTag(Tag t);
	List<Todo> findByBody(String text);
	
	Todo findById(Todo te);
	
	void addTodoElement(Todo todo);
	void updateTodoElement(Todo todo);
	void removeTodoElement(Todo todo);
	
	void clear();
}
