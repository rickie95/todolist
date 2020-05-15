package com.riccardomalavolti.apps.todolist.view;

import java.util.Set;

import com.riccardomalavolti.apps.todolist.controller.TodoController;
import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.model.Todo;

public class EditTodoAction implements TodoAction {
	
	public static final String TITLE_TEXT = "Edit To Do";
	public static final String HEADING_LABEL_TEXT = "Edit Todo";
	private TodoController todoController;
	private Todo todo;
	
	public EditTodoAction(TodoController todoController, Todo todo) {
		this.todoController = todoController;
		this.todo = todo;
	}

	@Override
	public void sendToController(String body, Set<Tag> tagList) {
		todo.setBody(body);
		todo.setTagSet(tagList);
		todoController.updateTodo(todo);
	}

	@Override
	public Todo getTodo() {
		return todo;
	}

	@Override
	public String getTitle() {
		return TITLE_TEXT;
	}

	@Override
	public String getHeading() {
		return HEADING_LABEL_TEXT;
	}

	@Override
	public TodoController getController() {
		return todoController;
	}

}
