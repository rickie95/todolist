package com.riccardomalavolti.apps.todolist.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;

import com.riccardomalavolti.apps.todolist.controller.TodoController;
import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.model.Todo;

import javax.swing.JLabel;
import javax.swing.JFormattedTextField;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.SwingConstants;

public class NewTodoDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LogManager.getLogger(NewTodoDialog.class);

	public static final String TAG_LBL_NO_TAG_TEXT = "No tags.";

	private final JPanel contentPanel = new JPanel();
	private TodoController todoController;
	private JFormattedTextField todoTextField;
	private JComboBox<Tag> tagComboBox;
	private JLabel tagLabel;
	private Todo todoElement;
	private JButton insertButton;
	private JButton clearButton;
	private Set<Tag> selectedTagList;

	
	public NewTodoDialog(TodoController controller, DefaultComboBoxModel<Tag> tagModel) {
		this.todoController = controller;
		this.todoElement = new Todo();
		
		this.selectedTagList = new HashSet<>();
		
		setTitle("New To Do ");
		setBounds(100, 100, 389, 219);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setName("contentPanel");
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Insert a new To Do");
		lblNewLabel.setBounds(12, 12, 432, 17);
		contentPanel.add(lblNewLabel);
		
		
		todoTextField = new JFormattedTextField();
		todoTextField.setName("todoTextBox");
		todoTextField.getDocument().addDocumentListener(new SimpleDocumentListener() {
			@Override
			public void update(DocumentEvent e) {
				textChanged();
			}
		});
		todoTextField.setToolTipText("Write here");
		todoTextField.setBounds(12, 51, 357, 41);
		contentPanel.add(todoTextField);
		
		this.tagLabel = new JLabel(TAG_LBL_NO_TAG_TEXT);
		tagLabel.setName("tagLabel");
		tagLabel.setFont(new Font("Dialog", Font.PLAIN, 10));
		tagLabel.setBounds(12, 30, 72, 17);
		contentPanel.add(tagLabel);
		
		
		class ItemChangeListener implements ItemListener{
		    @Override
		    public void itemStateChanged(ItemEvent event) {
		       if (event.getStateChange() == ItemEvent.SELECTED) {
		    	  tagSelected(event.getItem());
		       }
		    }
		}
		
		tagComboBox = new JComboBox<>();
		tagComboBox.setName("tagComboBox");
		tagComboBox.setToolTipText("Select a tag from here");
		tagComboBox.addItemListener(new ItemChangeListener());
		tagComboBox.setModel(tagModel);
		tagComboBox.setSelectedItem(null);
		tagComboBox.setBounds(113, 109, 256, 26);
		contentPanel.add(tagComboBox);
		
		JLabel lblTags = new JLabel("Tags");
		lblTags.setBounds(22, 114, 60, 17);
		contentPanel.add(lblTags);
	
	
		JPanel buttonPane = new JPanel();
		buttonPane.setName("buttonPanel");
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
			
		insertButton = new JButton("Insert To Do");
		insertButton.setName("insertButton");
		insertButton.setEnabled(false);
		insertButton.setName("insertButton");
		insertButton.addActionListener(e -> insertToDoFromTextField());
		
		clearButton = new JButton("Clear tags");
		clearButton.setName("clearButton");
		clearButton.setEnabled(false);
		clearButton.addActionListener(e -> clearTags());
		clearButton.setHorizontalAlignment(SwingConstants.LEFT);
		buttonPane.add(clearButton);
		insertButton.setActionCommand("OK");
		buttonPane.add(insertButton);
		getRootPane().setDefaultButton(insertButton);
			
			
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setName("cancelButton");
		cancelButton.addActionListener(e -> cancelButtonAction());
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		
		this.setVisible(true);
		
	}
	
	public NewTodoDialog(TodoController todoController, DefaultComboBoxModel<Tag> tagListModel, Todo todo) {
		// TODO: that should be a different class
		initFrame(todoController, tagListModel);
		todoElement = todo;
		redrawTagLbl();
		insertButton.setText("Update Todo");
		insertButton.addActionListener(e -> updateTodo());
	}

	private void initFrame(TodoController todoController, DefaultComboBoxModel<Tag> tagListModel) {
		// TODO Auto-generated method stub
		
	}

	private void updateTodo() {
		
	}

	public void tagSelected(Object eventItem) {        
        selectedTagList.add((Tag) eventItem);
        this.clearButton.setEnabled(true);
        redrawTagLbl();
	}

	protected void textChanged() {
		insertButton.setEnabled(!todoTextField.getText().trim().isEmpty());
	}

	public void redrawTagLbl() {
		tagLabel.setText(Tag.listToString(new ArrayList<Tag>(selectedTagList)));
	}

	private void clearTags() {
		selectedTagList.clear();
		this.tagComboBox.setSelectedItem(null);
		this.tagLabel.setText(TAG_LBL_NO_TAG_TEXT);
		this.todoElement.clearTags();
		this.clearButton.setEnabled(false);
	}

	
	public void insertToDoFromTextField() {
		LOGGER.debug("Inserting {}", todoTextField.getText());
		todoElement.setBody(todoTextField.getText());
		todoController.tagTodo(todoElement, new ArrayList<>(selectedTagList));
		todoController.addTodo(todoElement);
		todoTextField.setText("");
		todoElement = new Todo();
	}
	
	public void cancelButtonAction() {
		LOGGER.debug("Exiting from New To Do dialog");
		todoController.dispose(this);
	}
}
