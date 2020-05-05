package com.riccardomalavolti.apps.todolist.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;

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
		initFrame(tagListModel);
		setTodoElement(new Todo());
	}

	protected void initFrame(DefaultComboBoxModel<Tag> tagModel) {
		contentPanel = new JPanel();

		setTitle(TITLE_TEXT);
		setBounds(100, 100, 389, 219);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setName("contentPanel");
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		headingLabel = new JLabel();
		headingLabel.setText(HEADING_LABEL_TEXT);
		headingLabel.setName("headingLabel");
		headingLabel.setBounds(12, 12, 432, 17);
		contentPanel.add(headingLabel);

		createTextField();
		createTagLabel();
		createComboBox(tagModel);
		
		JLabel lblTags = new JLabel("Tags");
		lblTags.setBounds(22, 114, 60, 17);
		contentPanel.add(lblTags);
		
		createButtonsPanel();

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		repaint();
		setVisible(true);
		requestFocus();
	}

	private void createButtonsPanel() {
		JPanel buttonPane = new JPanel();
		buttonPane.setName("buttonPanel");
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		createClearButton();
		createConfirmButton();
		createCancelButton();

		buttonPane.add(clearButton);
		buttonPane.add(confirmButton);
		buttonPane.add(cancelButton);
		getRootPane().setDefaultButton(confirmButton);
	}

	private void createCancelButton() {
		cancelButton = new JButton("Cancel");
		cancelButton.setName("cancelButton");
		cancelButton.addActionListener(e -> super.cancelButtonAction());
		cancelButton.setActionCommand("Cancel");
	}

	private void createConfirmButton() {
		confirmButton = new JButton("Insert");
		confirmButton.setName("confirmButton");
		confirmButton.setEnabled(false);
		confirmButton.setActionCommand("OK");
		confirmButton.addActionListener(e -> confirmTodo());
	}

	private void createClearButton() {
		clearButton = new JButton("Clear");
		clearButton.setName("clearButton");
		clearButton.setEnabled(false);
		clearButton.addActionListener(e -> clearTags());
		clearButton.setHorizontalAlignment(SwingConstants.LEFT);
	}

	private void createComboBox(DefaultComboBoxModel<Tag> tagListModel) {
		class ItemChangeListener implements ItemListener {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					tagSelected(event.getItem());
				}
			}
		}
		tagComboBox = new JComboBox<>();
		tagComboBox.setName("tagComboBox");
		tagComboBox.addItemListener(new ItemChangeListener());
		tagComboBox.setModel(tagListModel);
		tagComboBox.setSelectedItem(null);
		tagComboBox.setBounds(113, 109, 256, 26);

		contentPanel.add(tagComboBox);
	}
	
	private void createTagLabel() {
		tagLabel = new JLabel(TAG_LBL_NO_TAG_TEXT);
		tagLabel.setName("tagLabel");
		tagLabel.setFont(new Font("Dialog", Font.PLAIN, 10));
		tagLabel.setBounds(12, 30, 72, 17);
		contentPanel.add(tagLabel);
	}


	private void createTextField() {
		todoTextBox = new JFormattedTextField();
		todoTextBox.getDocument().addDocumentListener(new SimpleDocumentListener() {
			@Override
			public void update(DocumentEvent e) {
				textChanged();
			}
		});
		todoTextBox.setName("todoTextField");
		todoTextBox.setBounds(12, 51, 357, 41);
		contentPanel.add(todoTextBox);
	}

	private void confirmTodo() {
		LOGGER.info("Inserting new todo.");
		Todo todo = getTodoElement();
		todo.setBody(todoTextBox.getText());
		todo.setTagSet(selectedTagList);
		todoController.addTodo(todo);
		todoTextBox.setText("");
	}

}
