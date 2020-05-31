package com.riccardomalavolti.apps.todolist.view;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;

import com.riccardomalavolti.apps.todolist.controller.TodoController;
import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.model.Todo;

public class DialogController {

	private NewTagDialog newTagDialog;
	private TodoController todoController;
	private TodoDialog todoDialog;
	
	public static final String EDIT_DIALOG_TITLE = "Edit To Do";
	public static final String NEW_DIALOG_TITLE = "New To Do";
	
	public DialogController(TodoController todoController) {
		this.todoController = todoController;
	}
	

	public void newTagDialog() {
		newTagDialog = new NewTagDialog(todoController);
		newTagDialog.showDialog();
	}

	public void todoDialog(TodoController todoController, DefaultComboBoxModel<Tag> tagListModel, Todo todo) {
		if (todoDialog != null)
			disposeDialog(todoDialog);
		
		if (todo == null) {
			todoDialog = new TodoDialog(todoController, tagListModel, new NewTodoAction(todoController), NEW_DIALOG_TITLE);
		}
		else {
			todoDialog = new TodoDialog(todoController, tagListModel, new EditTodoAction(todoController, todo), EDIT_DIALOG_TITLE);
		}
		todoDialog.showDialog();
		
	}

	public void disposeDialog(JDialog aDialog) {
		aDialog.dispose();
		aDialog = null;
	}

	public NewTagDialog getNewTagDialog() {
		return newTagDialog;
	}


	public TodoDialog getTodoDialog() {
		return todoDialog;
	}

}
