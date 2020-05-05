package com.riccardomalavolti.apps.todolist.view;

import javax.swing.DefaultComboBoxModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.riccardomalavolti.apps.todolist.controller.TodoController;
import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.model.Todo;

public class NewTodoDialog extends TodoDialog {

	private static final long serialVersionUID = 2366306011589695297L;
	public static final String TITLE_TEXT = "New To Do";
	public static final String HEADING_LABEL_TEXT = "New Todo";

	private static final Logger LOGGER = LogManager.getLogger(NewTodoDialog.class);

	public NewTodoDialog(TodoController todoController, DefaultComboBoxModel<Tag> tagListModel) {
		super(todoController);
		headingLabelText = HEADING_LABEL_TEXT;
		setTitle(TITLE_TEXT);
		initFrame(tagListModel);
		setTodoElement(new Todo());
	}

	protected void confirmButtonAction() {
		LOGGER.info("Inserting new todo.");
		Todo todo = getTodoElement();
		todo.setBody(todoTextBox.getText());
		todo.setTagSet(selectedTagList);
		todoController.addTodo(todo);
		todoTextBox.setText("");
	}
}
