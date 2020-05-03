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
		} catch(RuntimeException ex) {
			String message = "A problem as occurred while insering a new tag: ";
			todoView.error(message + ex.toString());
		}
	}

	public void findTodoByText(String searchText) {
		todoView.showAllTodo(todoManager.findTodoByText(searchText));
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
		newTodoDialog = new NewTodoDialog(this, tagListModel);
		newTodoDialog.toFront();
	}
	
	public void editTodoDialog(DefaultComboBoxModel<Tag> tagListModel, Todo todo) {
		editTodoDialog = new EditTodoDialog(this, tagListModel, todo);
		editTodoDialog.toFront();
	}

	public void newTagDialog() {
		newTagDialog = new NewTagDialog(this);
		newTagDialog.toFront();
	}

	public void dispose(JDialog aDialog) {
		aDialog.dispose();
	}
	
	public void setNewTodoDialog(JDialog newTodoDialog) {
		this.newTodoDialog = newTodoDialog;
	}
	
	public JDialog getNewTodoDialog() {
		return newTodoDialog;
	}

	public void setEditTodoDialog(JDialog editTodoDialog) {
		this.editTodoDialog = editTodoDialog;
	}
	
	public JDialog getEditTodoDialog() {
		return editTodoDialog;
	}

	public void setNewTagDialog(JDialog newTagDialog) {
		this.newTagDialog = newTagDialog;
	}

	public JDialog getNewTagDialog() {
		return newTagDialog;
	}
}
