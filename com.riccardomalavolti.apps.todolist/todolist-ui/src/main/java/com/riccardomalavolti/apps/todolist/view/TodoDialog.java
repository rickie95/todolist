package com.riccardomalavolti.apps.todolist.view;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.riccardomalavolti.apps.todolist.controller.TodoController;
import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.model.Todo;

public class TodoDialog extends JDialog {

	private static final long serialVersionUID = 5443345577609514544L;

	private static final Logger LOGGER = LogManager.getLogger(TodoDialog.class);
	
	public static final String TAG_LBL_NO_TAG_TEXT = "No tags.";

	protected JComponent contentPanel;
	
	protected transient Set<Tag> selectedTagList;
	protected transient TodoController todoController;
	protected JTextField todoTextBox;
	protected JComboBox<Tag> tagComboBox;
	protected JButton confirmButton;
	protected JButton clearButton;
	protected JButton cancelButton;
	protected JLabel tagLabel;
	protected JLabel headingLabel;
	protected String headingLabelText;

	private DefaultComboBoxModel<Tag> tagModel;

	private transient TodoAction todoAction;
	private transient Todo todo;

	private String dialogTitle;


	public TodoDialog(TodoController todoController, DefaultComboBoxModel<Tag> tagModel, TodoAction todoAction, String dialogTitle) {
		super();
		this.tagModel = tagModel;
		this.selectedTagList = new HashSet<>();
		this.todoAction = todoAction;
		this.todoController = todoController;
		this.todo = todoAction.getTodo();
		this.dialogTitle = dialogTitle;
		initFrame();
	}
	
	private void initFrame() {
		contentPanel = new JPanel();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 389, 219);
		setMinimumSize(new Dimension(389, 219));
		contentPanel.setMinimumSize(new Dimension(350, 200));
		setTitle(dialogTitle);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setName("contentPanel");
		contentPanel.setLayout(null);
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		headingLabel = new JLabel();
		headingLabel.setName("headingLabel");
		headingLabel.setText(dialogTitle);
		headingLabel.setBounds(12, 12, 432, 17);
		contentPanel.add(headingLabel);
		
		createTagLabel();
		createTextField(todo.getBody());
		
		JLabel lblTags = new JLabel("Tags");
		lblTags.setBounds(22, 114, 60, 17);
		contentPanel.add(lblTags);
		
		createComboBox();
		createButtonsPanel();

		validate();
		repaint();
	}
	
	public void showDialog() {
		pack();
		setVisible(true);
		toFront();
		requestFocus();
	}
	
	private void createButtonsPanel() {
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
	}
	
	private void createCancelButton() {
		cancelButton = new JButton("Cancel");
		cancelButton.setName("cancelButton");
		cancelButton.setEnabled(true);
		cancelButton.addActionListener(e -> cancelButtonAction());
		cancelButton.setActionCommand("Cancel");
	}

	private void createConfirmButton() {
		confirmButton = new JButton("Save");
		confirmButton.setName("confirmButton");
		confirmButton.setEnabled(true);
		if(todo.getBody().isEmpty())
			confirmButton.setEnabled(false);
		confirmButton.addActionListener(e -> confirmButtonAction());
	}
	
	private void createClearButton() {
		clearButton = new JButton("Clear tags");
		clearButton.setName("clearButton");
		clearButton.setEnabled(false);
		if(!todoAction.getTodo().getTagList().isEmpty())
			clearButton.setEnabled(true);
		clearButton.addActionListener(e -> clearTags());
		clearButton.setHorizontalAlignment(SwingConstants.LEFT);
	}
	
	private void confirmButtonAction() {
		LOGGER.info("Sending todo");
		String body = todoTextBox.getText();
		todoAction.sendToController(body, selectedTagList);
		todoController.dispose(this);
	}

	private void cancelButtonAction() {
		LOGGER.debug("Exiting from New To Do dialog");
		todoController.dispose(this);
	}
	
	private void createComboBox() {
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
	
	private void createTextField(String text) {
		todoTextBox = new JTextField(text);
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
	
	protected void textChanged() {
		Boolean isTextEmpty = todoTextBox.getText().isEmpty();
		confirmButton.setEnabled(true);
		
		if(Boolean.TRUE.equals(isTextEmpty)) {
			confirmButton.setEnabled(false);
		}
		
	}

	private void createTagLabel() {
		String text = Tag.listToString(todo.getTagList());
		if (text.equals(""))
			text = TAG_LBL_NO_TAG_TEXT;
		tagLabel = new JLabel(text);
		tagLabel.setName("tagLabel");
		tagLabel.setFont(new Font("Dialog", Font.PLAIN, 10));
		tagLabel.setBounds(12, 30, 72, 17);
		contentPanel.add(tagLabel);
	}
	
	private void tagSelected(Object eventItem) {        
        selectedTagList.add((Tag) eventItem);
        clearButton.setEnabled(true);
        updateTagLabelText();
	}
	
	public void clearTags() {
		selectedTagList.clear();
		tagComboBox.setSelectedItem(null);
		clearButton.setEnabled(false);
		updateTagLabelText();
	}
	
	private void updateTagLabelText() {
		tagLabel.setText(TAG_LBL_NO_TAG_TEXT);
		
		if(!selectedTagList.isEmpty())
			tagLabel.setText(Tag.listToString(new ArrayList<>(selectedTagList)));
	}

	public Set<Tag> getTagList() {
		return selectedTagList;
	}
	
}
