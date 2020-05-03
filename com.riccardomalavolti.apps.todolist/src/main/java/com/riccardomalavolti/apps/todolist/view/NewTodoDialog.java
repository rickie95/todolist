package com.riccardomalavolti.apps.todolist.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
		setTodoElement(new Todo());
	}

	@Override
	protected void initFrame(DefaultComboBoxModel<Tag> tagModel) {
		// removed call to super initFrame(tagModel) 
		
		contentPanel = new JPanel();
		
		setBounds(200, 200, 389, 219);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setName("contentPanel");
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		headingLabel = new JLabel();
		headingLabel.setName("headingLabel");
		headingLabel.setBounds(12, 12, 432, 17);
		contentPanel.add(headingLabel);
		
		todoTextBox = new JFormattedTextField();
		todoTextBox.setName("todoTextBox");
		todoTextBox.getDocument().addDocumentListener(new SimpleDocumentListener() {
			@Override
			public void update(DocumentEvent e) {
				textChanged();
			}
		});
		
		todoTextBox.setToolTipText("Write here");
		todoTextBox.setBounds(12, 51, 357, 41);
		contentPanel.add(todoTextBox);
		
		tagLabel = new JLabel(TAG_LBL_NO_TAG_TEXT);
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
	
		JPanel buttonPanel = new JPanel();
		buttonPanel.setName("buttonPanel");
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
			
		confirmButton = new JButton();
		confirmButton.setName("confirmButton");
		confirmButton.setEnabled(false);
		confirmButton.setActionCommand("OK");
		
		clearButton = new JButton("Clear tags");
		clearButton.setName("clearButton");
		clearButton.setEnabled(false);
		clearButton.addActionListener(e -> clearTags());
		clearButton.setHorizontalAlignment(SwingConstants.LEFT);
		
		cancelButton = new JButton("Cancel");
		cancelButton.setName("cancelButton");
		cancelButton.addActionListener(e -> super.cancelButtonAction());
		cancelButton.setActionCommand("Cancel");
		
		buttonPanel.add(clearButton);
		buttonPanel.add(confirmButton);
		buttonPanel.add(cancelButton);	
		getRootPane().setDefaultButton(confirmButton);
		
		
		/* END */
		setTitle("New To Do ");
		setBounds(400, 400, 389, 219);

		headingLabel.setText(HEADING_LABEL_TEXT);
		confirmButton.setText("Add new Todo");
		confirmButton.addActionListener(e -> addNewTodo());

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		repaint();
		setVisible(true);
		requestFocus();
	}

	protected void addNewTodo() {
		LOGGER.debug("Inserting {}", todoTextBox.getText());
		Todo todo = getTodoElement();
		todo.setBody(todoTextBox.getText());
		todo.setTagSet(selectedTagList);
		todoController.addTodo(todo);
		setTodoElement(new Todo());
	}

}
