package com.riccardomalavolti.apps.todolist.view;


import javax.swing.DefaultComboBoxModel;

import org.apache.logging.log4j.LogManager;

import com.riccardomalavolti.apps.todolist.controller.TodoController;
import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.model.Todo;

import org.apache.logging.log4j.Logger;

public class EditTodoDialog extends TodoDialog {

	private static final long serialVersionUID = -626987188470141213L;
	
	private static final Logger LOGGER = LogManager.getLogger(EditTodoDialog.class);
	
	
	public static final String HEADING_LABEL_TEXT = "Edit a Todo";

	public EditTodoDialog(TodoController todoController, DefaultComboBoxModel<Tag> tagListModel, Todo todo) {
		super(todoController);
		initFrame(tagListModel);
		setTodoElement(todo);
	}
	
	@Override
	protected void initFrame(DefaultComboBoxModel<Tag> tagModel) {
		super.initFrame(tagModel);
		
		setTitle("Edit To Do");
		headingLabel.setText(HEADING_LABEL_TEXT);
		
		confirmButton.addActionListener(e -> updateTodo());	
		confirmButton.setText("Update Todo");
		
		setVisible(true);
	}
	

	private void updateTodo() {
		
		LOGGER.debug("Send request to controller.");
		Todo todoElement = getTodoElement();
		todoElement.setBody(todoTextBox.getText());
		todoElement.setTagSet(selectedTagList);
		todoController.updateTodo(todoElement);		
	}

}
