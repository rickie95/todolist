package com.riccardomalavolti.apps.todolist.controller;

import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;

import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.model.Todo;
import com.riccardomalavolti.apps.todolist.view.EditTodoDialog;
import com.riccardomalavolti.apps.todolist.view.NewTagDialog;
import com.riccardomalavolti.apps.todolist.view.NewTodoDialog;
import com.riccardomalavolti.apps.todolist.view.TodoView;

public class TodoController {
	
	private static final long serialVersionUID = 762758691L;
	TodoView todoView;
	TodoManager todoManager;
	
	private JDialog newTodoDialog;
	private JDialog editTodoDialog;
	private JDialog newTagDialog;
	
	
	public TodoController(TodoView todoView, TodoManager todoManager) {
		this.todoView = todoView;
		this.todoManager = todoManager;
		
		showTodos();
		showTags();
	}

	public void showTodos() {
		todoView.showAllTodo(todoManager.getTodoList());
	}

	public void showTags() {
		todoView.showAllTags(todoManager.getTagList());
	}
	
	public void addTodo(Todo todo) {
		try {
			todoManager.addTodo(todo);
			todoView.addTodo(todo);
		}catch(RuntimeException ex){
			String message = "A problem as occurred while insering a new todo: ";
			todoView.error(message + ex.toString());
		}
	}

	public void addTag(Tag tag) {
		try {
			todoManager.addTag(tag);
			todoView.addTag(tag);
		}catch(RuntimeException ex) {
			String message = "A problem as occurred while insering a new tag: ";
			todoView.error(message + ex.toString());
		}
	}

	public void findTodoByText(String searchText) {
		List<Todo> results = todoManager.findTodoByText(searchText);
		todoView.showAllTodo(results);
	}

	public void findTagByText(String searchText) {
		todoView.showAllTags(todoManager.findTagByText(searchText));
	}

	public void findTodoByTag(Tag tag) {
		todoView.showAllTodo(todoManager.findTodoByTag(tag));
	}

	public void updateTodo(Todo todo) {
		todoManager.updateTodo(todo);
	}

	public void updateTag(Tag tag) {
		todoManager.updateTag(tag);
	}

	public void removeTodo(Todo todo) {
		todoManager.removeTodo(todo);
		todoView.removeTodo(todo);
	}

	public void removeTag(Tag tag) {
		todoManager.removeTag(tag);
		todoView.removeTag(tag);
	}

	public void tagTodo(Todo todo, Tag tag) {
		todoManager.tagTodo(todo, tag);
	}
	
	public void tagTodo(Todo todo, List<Tag> tagList) {
		tagList.forEach(tag -> todoManager.tagTodo(todo, tag));
	}

	public void newTodoDialog(DefaultComboBoxModel<Tag> tagListModel) {
		if(newTodoDialog == null)
			newTodoDialog = new NewTodoDialog(this, tagListModel);
		newTodoDialog.setVisible(true);
	}
	
	public void editTodoDialog(DefaultComboBoxModel<Tag> tagListModel, Todo todo) {
		if(editTodoDialog == null)
			editTodoDialog = new EditTodoDialog(this, tagListModel, todo);
		editTodoDialog.setVisible(true);
	}

	public void newTagDialog() {
		if(newTagDialog == null)
			newTagDialog = new NewTagDialog(this);
		newTagDialog.setVisible(true);
	}

	public void dispose(JDialog aDialog) {
		aDialog.dispose();
	}
	
	public JDialog getNewTodoDialog() {
		return newTodoDialog;
	}

	public JDialog getNewTagDialog() {
		return newTagDialog;
	}
	
}
