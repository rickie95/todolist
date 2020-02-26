package com.riccardomalavolti.apps.todolist.repositories;

import java.util.List;

import com.riccardomalavolti.apps.todolist.Tag;
import com.riccardomalavolti.apps.todolist.TodoElement;

public interface TodoRepository {
	
	List<TodoElement> findAll();
	List<TodoElement> findByTag(Tag t);
	List<TodoElement> findByBody(String text);
	
	TodoElement findById(TodoElement te);
	
	void addTodoElement(TodoElement te);
	void updateTodoElement(TodoElement te);
	void removeTodoElement(TodoElement te);
	
	void clear();
}
