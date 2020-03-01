package com.riccardomalavolti.apps.todolist;

import java.util.List;

import com.riccardomalavolti.apps.todolist.repositories.TodoRepository;

public class TodoManager {

	private TodoRepository todoRepository;
	
	public TodoManager(TodoRepository todoRepository) {
		this.todoRepository = todoRepository;
	}

	public List<Todo> getTodoList() {
		return todoRepository.findAll();
	}

	public void addTodo(Todo todoElement) {
		this.todoRepository.addTodoElement(todoElement);		
	}

	public void removeTodo(Todo todoElement) {
		this.todoRepository.removeTodoElement(todoElement);	
	}
	
	public void updateTodo(Todo todoElement) {
		this.todoRepository.updateTodoElement(todoElement);
	}

	public void tagTodo(Todo todo, Tag tag) {
		todo.addTag(tag);
	}

	

}
