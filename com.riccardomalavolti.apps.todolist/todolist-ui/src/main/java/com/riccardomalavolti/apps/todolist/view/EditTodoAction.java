package com.riccardomalavolti.apps.todolist.view;

import java.util.Set;

import com.riccardomalavolti.apps.todolist.controller.TodoController;
import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.model.Todo;

public class EditTodoAction implements TodoAction {

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

}
