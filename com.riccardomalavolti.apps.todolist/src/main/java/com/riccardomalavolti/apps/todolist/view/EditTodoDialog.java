package com.riccardomalavolti.apps.todolist.view;

import javax.swing.DefaultComboBoxModel;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.riccardomalavolti.apps.todolist.controller.TodoController;
import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.model.Todo;

public class EditTodoDialog extends TodoDialog {

	private static final long serialVersionUID = -626987188470141213L;
	public static final String TITLE_TEXT = "Edit To Do";
	public static final String HEADING_LABEL_TEXT = "Edit a Todo";

	private static final Logger LOGGER = LogManager.getLogger(EditTodoDialog.class);

	public EditTodoDialog(TodoController todoController, DefaultComboBoxModel<Tag> tagListModel, Todo todo) {
		super(todoController);
		headingLabelText = HEADING_LABEL_TEXT;
		setTitle(TITLE_TEXT);
		initFrame(tagListModel);
		setTodoElement(todo);
	}

	protected void confirmButtonAction() {
		LOGGER.debug("Updating todo.");
		Todo todo = getTodoElement();
		todo.setBody(todoTextBox.getText());
		todo.setTagSet(selectedTagList);
		todoController.updateTodo(todo);
	}

}
