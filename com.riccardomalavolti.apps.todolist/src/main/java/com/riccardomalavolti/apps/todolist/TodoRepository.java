package com.riccardomalavolti.apps.todolist;

import java.util.List;

public interface TodoRepository {
	
	List<TodoElement> findAll();
	List<TodoElement> findByTag(Tag t);
	List<TodoElement> findByBody(String text);
	
	TodoElement findById(TodoElement te);
	
	void addTodoElement(TodoElement te);
	void updateTodoElement(TodoElement te);
	void removeTodoElement(TodoElement te);
}
