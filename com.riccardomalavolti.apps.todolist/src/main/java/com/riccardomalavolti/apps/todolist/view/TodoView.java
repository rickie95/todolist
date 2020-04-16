package com.riccardomalavolti.apps.todolist.view;

import java.util.List;

import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.model.Todo;

public interface TodoView {

	public void showAllTodo(List<Todo> list);

	public void showAllTags(List<Tag> tags);

	public void removeTodo(Todo todo);
	
	public void removeTag(Tag tag);
	
	public void error(String message);

	public void addTodo(Todo todo);

	public void addTag(Tag tag);
}
