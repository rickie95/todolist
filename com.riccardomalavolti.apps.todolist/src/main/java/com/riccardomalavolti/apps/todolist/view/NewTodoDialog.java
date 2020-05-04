package com.riccardomalavolti.apps.todolist.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
import javax.swing.JTextField;
import javax.swing.JComboBox;

public class NewTodoDialog extends JDialog {

	private static final long serialVersionUID = 2366306011589695297L;
	public static final String TITLE_TEXT = "New To Do";
	public static final String TAG_LBL_NO_TAG_TEXT = "No tags.";
	
	private final JPanel contentPanel = new JPanel();
	private transient TodoController controller;
	private JTextField todoTextField;
	private JButton confirmButton;
	private JComboBox<Tag> comboBox;
	private JLabel tagLabel;
	private JButton clearButton;
	private transient Set<Tag> selectedTags;
	private boolean ready = false;

	public static void main(String[] args) {
		try {
			new NewTodoDialog();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public NewTodoDialog(TodoController todoController, DefaultComboBoxModel<Tag> comboBoxModel) {
		this();
		this.selectedTags = new HashSet<>();
		this.controller = todoController;
		setComboBoxModel(comboBoxModel);
		setVisible(true);
		this.ready  = true;
	}

	public void setComboBoxModel(DefaultComboBoxModel<Tag> comboBoxModel) {
		comboBox.setModel(comboBoxModel);
		comboBox.setSelectedItem(null);
	}
	
	public boolean isReady() {
		return ready;
	}

	public NewTodoDialog() {
		setTitle(TITLE_TEXT);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(150, 150, 340, 180);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		createTagLabel();
		
		{
			todoTextField = new JTextField();
			todoTextField.getDocument().addDocumentListener(new SimpleDocumentListener() {
				@Override
				public void update(DocumentEvent e) { textChanged(); }
			});
			todoTextField.setName("todoTextField");
			contentPanel.add(todoTextField, BorderLayout.CENTER);
			todoTextField.setColumns(10);
		}
		{
			class ItemChangeListener implements ItemListener{
			    @Override
			    public void itemStateChanged(ItemEvent event) {
			       if (event.getStateChange() == ItemEvent.SELECTED) {
			    	  tagSelected(event.getItem());
			       }
			    }
			}
			comboBox = new JComboBox<>();
			comboBox.setName("tagComboBox");
			contentPanel.add(comboBox, BorderLayout.SOUTH);
			comboBox.addItemListener(new ItemChangeListener());
			
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				clearButton = new JButton("Clear");
				buttonPane.add(clearButton);
				clearButton.setName("clearButton");
				clearButton.addActionListener(e -> tagLabel.setText(TAG_LBL_NO_TAG_TEXT));
				clearButton.setEnabled(false);
			}
			{
				confirmButton = new JButton("Insert");
				confirmButton.setActionCommand("Insert");
				confirmButton.setName("confirmButton");
				confirmButton.addActionListener(e -> confirmTodo());
				confirmButton.setEnabled(false);
				buttonPane.add(confirmButton);
				getRootPane().setDefaultButton(confirmButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setName("cancelButton");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(e -> close());
				buttonPane.add(cancelButton);
			}
		}
		validate();
		setVisible(true);
	}
	
	

	public void tagSelected(Object tag) {
		selectedTags.add((Tag)tag);
		clearButton.setEnabled(true);
        restoreTagLabel();
	}

	private void confirmTodo() {
		Todo todo = new Todo();
		todo.setBody(todoTextField.getText());
		todo.setTagSet(selectedTags);
		todo.setAsCompleted(false);
		controller.addTodo(todo);
		resetDialog();
	}

	private void resetDialog() {
		todoTextField.setText("");
		restoreTagLabel();
		confirmButton.setEnabled(false);
		clearButton.setEnabled(false);
	}

	private void restoreTagLabel() {
		tagLabel.setText(TAG_LBL_NO_TAG_TEXT);
		if(!selectedTags.isEmpty())
			tagLabel.setText(Tag.listToString(new ArrayList<Tag>(selectedTags)));
	}

	private void close() {
		controller.dispose(this);
	}

	protected void textChanged() {
		confirmButton.setEnabled(!todoTextField.getText().isEmpty());
	}

	private void createTagLabel() {
		tagLabel = new JLabel(NewTodoDialog.TAG_LBL_NO_TAG_TEXT);
		tagLabel.setName("tagLabel");
		contentPanel.add(tagLabel, BorderLayout.NORTH);
	}

	public void setTagLabel(String string) {
		tagLabel.setText(string);
	}

	public void enableClearButton() {
		clearButton.setEnabled(true);
	}

	
}
