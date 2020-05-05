package com.riccardomalavolti.apps.todolist.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
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

public abstract class TodoDialog extends JDialog {

	private static final long serialVersionUID = 5443345577609514544L;

	private static final Logger LOGGER = LogManager.getLogger(TodoDialog.class);
	
	public static final String TAG_LBL_NO_TAG_TEXT = "No tags.";

	private transient Todo todoElement;
	protected JComponent contentPanel;
	
	protected transient Set<Tag> selectedTagList;
	protected transient TodoController todoController;
	protected JFormattedTextField todoTextBox;
	protected JComboBox<Tag> tagComboBox;
	protected JButton confirmButton;
	protected JButton clearButton;
	protected JButton cancelButton;
	protected JLabel tagLabel;
	protected JLabel headingLabel;
	protected String headingLabelText;

	protected abstract void confirmButtonAction();

	public TodoDialog(TodoController controller) {
		this.todoController = controller;
		this.selectedTagList = new HashSet<>();
	}
	
	protected void initFrame(DefaultComboBoxModel<Tag> tagModel) {
		contentPanel = new JPanel();

		setBounds(100, 100, 389, 219);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setName("contentPanel");
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		headingLabel = new JLabel();
		headingLabel.setText(headingLabelText);
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
	
	protected void createButtonsPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setName("buttonPanel");
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		createClearButton();
		createConfirmButton();
		createCancelButton();

		buttonPanel.add(clearButton);
		buttonPanel.add(confirmButton);
		buttonPanel.add(cancelButton);
		getRootPane().setDefaultButton(confirmButton);
	}
	
	private void createCancelButton() {
		cancelButton = new JButton("Cancel");
		cancelButton.setName("cancelButton");
		cancelButton.addActionListener(e -> cancelButtonAction());
		cancelButton.setActionCommand("Cancel");
	}

	private void createConfirmButton() {
		confirmButton = new JButton();
		confirmButton.setName("confirmButton");
		confirmButton.setEnabled(false);
		confirmButton.setActionCommand("OK");
		confirmButton.setText("Update Todo");
		confirmButton.addActionListener(e -> confirmButtonAction());
	}

	private void createClearButton() {
		clearButton = new JButton("Clear tags");
		clearButton.setName("clearButton");
		clearButton.setEnabled(false);
		clearButton.addActionListener(e -> clearTags());
		clearButton.setHorizontalAlignment(SwingConstants.LEFT);
	}
	
	protected void cancelButtonAction() {
		LOGGER.debug("Exiting from New To Do dialog");
		todoController.dispose(this);
	}
	
	protected void createComboBox(DefaultComboBoxModel<Tag> tagModel) {
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
		tagComboBox.setModel(tagModel);
		tagComboBox.setSelectedItem(null);
		tagComboBox.setBounds(113, 109, 256, 26);

		contentPanel.add(tagComboBox);
	}
	
	private void createTextField() {
		todoTextBox = new JFormattedTextField();
		todoTextBox.getDocument().addDocumentListener(new SimpleDocumentListener() {
			@Override
			public void update(DocumentEvent e) {
				textChanged();
			}
		});
		todoTextBox.setName("todoTextBox");
		todoTextBox.setBounds(12, 51, 357, 41);
		contentPanel.add(todoTextBox);
	}
	
	private void createTagLabel() {
		tagLabel = new JLabel(TAG_LBL_NO_TAG_TEXT);
		tagLabel.setName("tagLabel");
		tagLabel.setFont(new Font("Dialog", Font.PLAIN, 10));
		tagLabel.setBounds(12, 30, 72, 17);
		contentPanel.add(tagLabel);
	}
	
	protected void tagSelected(Object eventItem) {        
        selectedTagList.add((Tag) eventItem);
        this.clearButton.setEnabled(true);
        redrawTagLbl();
	}
	
	public void clearTags() {
		selectedTagList.clear();
		this.tagComboBox.setSelectedItem(null);
		this.tagLabel.setText(TAG_LBL_NO_TAG_TEXT);
		this.todoElement.clearTags();
		this.clearButton.setEnabled(false);
	}
	
	protected void textChanged() {
		confirmButton.setEnabled(!todoTextBox.getText().trim().isEmpty());
	}
	
	public void redrawTagLbl() {
		tagLabel.setText(TAG_LBL_NO_TAG_TEXT);
		
		if(!selectedTagList.isEmpty())
			tagLabel.setText(Tag.listToString(new ArrayList<Tag>(selectedTagList)));
	}
	
	/* Getters and setters */ 
	
	public Todo getTodoElement() {
		return todoElement;
	}
	
	public void setTodoElement(Todo todo) {
		this.todoElement = todo;
		if(!todo.getTagList().isEmpty()) {
			clearButton.setEnabled(true);
			selectedTagList = getTodoElement().getTagList();
		}
		todoTextBox.setText(todo.getBody());
		redrawTagLbl();
	}
	
}
