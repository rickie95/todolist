package com.riccardomalavolti.apps.todolist;

import java.util.List;

public class TodoManager {

	private TodoRepository todoRepository;
	
	public TodoManager(TodoRepository todoRepository) {
		this.todoRepository = todoRepository;
	}

	public List<TodoElement> getTodoList() {
		return todoRepository.findAll();
	}

	public void addTodo(TodoElement todoElement) {
		this.todoRepository.addTodoElement(todoElement);		
	}

	public void removeTodo(TodoElement todoElement) {
		this.todoRepository.removeTodoElement(todoElement);	
	}
	
	public void updateTodo(TodoElement todoElement) {
		this.todoRepository.updateTodoElement(todoElement);
	}

	public void tagTodo(TodoElement todo, Tag tag) {
		todo.addTag(tag);
	}

	

}
