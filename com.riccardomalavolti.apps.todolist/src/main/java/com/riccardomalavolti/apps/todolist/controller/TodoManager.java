package com.riccardomalavolti.apps.todolist.controller;

import java.util.ArrayList;
import java.util.List;

import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.model.Todo;
import com.riccardomalavolti.apps.todolist.repositories.tag.TagRepository;
import com.riccardomalavolti.apps.todolist.repositories.todo.TodoRepository;

public class TodoManager {

	private TodoRepository todoRepository;
	private TagRepository tagRepository;
	
	public TodoManager(TodoRepository todoRepo, TagRepository tagRepo) {
		this.todoRepository = todoRepo;
		this.tagRepository = tagRepo;
	}
	
	public void tagTodo(Todo todo, Tag tag) {
		todo.addTag(tag);
	}

	public List<Todo> getTodoList() {
		return todoRepository.findAll();
	}

	public void addTodo(Todo todoElement) {
		todoRepository.addTodoElement(todoElement);		
	}

	public void removeTodo(Todo todoElement) {
		todoRepository.removeTodoElement(todoElement);	
	}
	
	public void updateTodo(Todo todoElement) {
		todoRepository.updateTodoElement(todoElement);
	}
	
	public List<Tag> getTagList() {
		return new ArrayList<>(tagRepository.findAll());
	}
	
	public void addTag(Tag tag) {
		tagRepository.addTag(tag);
	}
	
	public void removeTag(Tag tag) {
		tagRepository.removeTag(tag);
	}
	
	public void updateTag(Tag tag) {
		tagRepository.updateTag(tag);
	}
	
}
