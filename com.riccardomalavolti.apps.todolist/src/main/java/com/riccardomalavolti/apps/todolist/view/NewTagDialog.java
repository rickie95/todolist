package com.riccardomalavolti.apps.todolist.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;

import com.riccardomalavolti.apps.todolist.controller.TodoController;
import com.riccardomalavolti.apps.todolist.model.Tag;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class NewTagDialog extends JDialog {


	private static final long serialVersionUID = 8748764450028878820L;
	
	private final JPanel contentPanel = new JPanel();
	
	private TodoController controller;
	private JTextField tagTextField;
	private JButton insertButton;

	public NewTagDialog(TodoController c) {
		setTitle("New Tag");
		this.controller = c;
		
		contentPanel.setName("contentPanel");
		setBounds(100, 100, 346, 119);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel lblInsertTagName = new JLabel("Insert tag name");
		lblInsertTagName.setHorizontalAlignment(SwingConstants.LEFT);
		contentPanel.add(lblInsertTagName);
		
		tagTextField = new JTextField();
		tagTextField.getDocument().addDocumentListener(new SimpleDocumentListener() {
			@Override
			public void update(DocumentEvent e) {
				textChanged();
			}
		});
		tagTextField.setName("tagTextField");
		contentPanel.add(tagTextField);
		tagTextField.setColumns(10);
	
	
		JPanel buttonPanel = new JPanel();
		buttonPanel.setName("buttonPanel");
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		insertButton = new JButton("Insert");
		insertButton.setEnabled(false);
		insertButton.setName("insertButton");
		insertButton.addActionListener(e -> insertTag());
		buttonPanel.add(insertButton);
		getRootPane().setDefaultButton(insertButton);

	
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setName("cancelButton");
		cancelButton.addActionListener(e -> closeDialog());
		cancelButton.setActionCommand("Cancel");
		buttonPanel.add(cancelButton);		
		
		this.pack();
		this.setVisible(true);
	}
	
	private void textChanged() {
		// If textField is empty => insertBtn must be disabled.
		insertButton.setEnabled(!tagTextField.getText().trim().isEmpty());
	}

	private void closeDialog() {
		this.dispose();
	}


	private void insertTag() {
		Tag t = new Tag(tagTextField.getText());
		controller.addTag(t);
		tagTextField.setText("");
	}
	
	

}
