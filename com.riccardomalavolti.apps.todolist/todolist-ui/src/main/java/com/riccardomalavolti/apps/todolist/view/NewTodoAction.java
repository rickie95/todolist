package com.riccardomalavolti.apps.todolist.view;

import java.util.Set;

import com.riccardomalavolti.apps.todolist.controller.TodoController;
import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.model.Todo;

public class NewTodoAction implements TodoAction {

	public static final String TITLE_TEXT = "New To Do";
	public static final String HEADING_LABEL_TEXT = "New Todo";

	private Todo todo;
	private TodoController todoController;

	public NewTodoAction(TodoController todoController) {
		this.todoController = todoController;
		todo = new Todo();
	}

	@Override
	public void sendToController(String body, Set<Tag> tagList) {
		todo.setBody(body);
		todo.setTagSet(tagList);
		todoController.addTodo(todo);
	}

	@Override
	public Todo getTodo() {
		return todo;
	}

}
