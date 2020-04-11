package com.riccardomalavolti.apps.todolist.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;

import com.riccardomalavolti.apps.todolist.controller.TodoController;
import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.model.Todo;


public class NewTodoDialog extends TodoDialog {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LogManager.getLogger(NewTodoDialog.class);

	public static final String HEADING_LABEL_TEXT = "Insert a new To Do";
	
	public NewTodoDialog(TodoController controller, DefaultComboBoxModel<Tag> tagModel) {
		super(controller);
		initFrame(tagModel);
	}

	@Override
	protected void initFrame(DefaultComboBoxModel<Tag> tagModel) {
		super.initFrame(tagModel);
		setTitle("New To Do ");
		setTodoElement(new Todo());
		headingLabel.setText(HEADING_LABEL_TEXT);
		confirmButton.setText("Add new Todo");
		confirmButton.addActionListener(e ->addNewTodo());
		setVisible(true);
	}
	
	protected void addNewTodo() {
		LOGGER.debug("Inserting {}", todoTextBox.getText());
		Todo todo = getTodoElement();
		todo.setBody(todoTextBox.getText());
		todoController.tagTodo(todo, new ArrayList<>(selectedTagList));
		todoController.addTodo(todo);
		todoTextBox.setText("");
		setTodoElement(new Todo());
	}
	
	
}
