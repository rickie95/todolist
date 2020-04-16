package com.riccardomalavolti.apps.todolist.view;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.riccardomalavolti.apps.todolist.controller.TodoController;
import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.model.Todo;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;


public class MainView extends JFrame implements TodoView{

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LogManager.getLogger(MainView.class);
	

	private transient TodoController todoController;
	private DefaultComboBoxModel<Tag> tagListModel;
	private TodoTableModel todoTableModel;
	private JButton removeTodoButton;
	private JPanel contentPanel;
	private JTable todoTable;
	

	public MainView() {
		todoTableModel =  new TodoTableModel();
		
		setTitle("To Do List");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 400);
		contentPanel = new JPanel();
		contentPanel.setName("contentPanel");
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPanel);
		
		
		// TodoElement panel part
		JPanel todoPanel = new JPanel();
		todoPanel.setName("todoPanel");
		contentPanel.add(todoPanel, BorderLayout.CENTER);
		todoPanel.setLayout(new BorderLayout(0, 0));
				
		JPanel todoControlPanel = new JPanel();
		todoControlPanel.setName("todoControlPanel");
		todoPanel.add(todoControlPanel, BorderLayout.SOUTH);
		
		JButton newTodoBtn = new JButton("new To Do");
		newTodoBtn.setName("newTodoBtn");
		newTodoBtn.addActionListener(e -> newTodoAction());
		todoControlPanel.add(newTodoBtn);
		
		removeTodoButton = new JButton("remove Todo");
		removeTodoButton.setName("removeTodoButton");
		removeTodoButton.setEnabled(false);
		removeTodoButton.addActionListener(e -> removeTodoAction());
		
		todoControlPanel.add(removeTodoButton);
		
		JPanel todoLabelPanel = new JPanel();
		todoLabelPanel.setName("todoLabelPanel");
		todoPanel.add(todoLabelPanel, BorderLayout.NORTH);
		
		JLabel lblTodo = new JLabel("Todo");
		todoLabelPanel.add(lblTodo);
		
		todoTable = new JTable();
		todoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		todoTable.setName("todoTable");
		todoTable.addMouseListener(new MouseAdapter() {
			@Override
	        public void mouseClicked (MouseEvent me) {
	            if (me.getClickCount() > 1) {
	        		LOGGER.debug("Double on row.");
	                editTodo();
	            }
	            else {
	            	LOGGER.debug("Single click on row");
	            	rowSelected();
	            }
	        }
	    });
		todoTable.setShowGrid(false);
		todoTable.setBorder(new EmptyBorder(20, 20, 20, 20));
		todoTable.setShowVerticalLines(false);
		todoTable.setShowHorizontalLines(false);
		todoTable.setModel(todoTableModel);
		todoTable.setTableHeader(null);
		todoTable.getColumnModel().getColumn(0).setPreferredWidth(10);
		todoTable.getColumnModel().getColumn(1).setPreferredWidth(320);		
		
		JScrollPane todoListPanel = new JScrollPane(todoTable);
		todoListPanel.setViewportBorder(new EmptyBorder(1, 1, 1, 1));
		todoPanel.add(todoListPanel, BorderLayout.CENTER);
		
		// Tags panel part
		JPanel tagPanel = new JPanel();
		tagPanel.setName("tagPanel");
		tagPanel.setBorder(new LineBorder(Color.GRAY, 1, true));
		contentPanel.add(tagPanel, BorderLayout.EAST);
		tagPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel tagLabelPanel = new JPanel();
		tagLabelPanel.setName("tagLabelPanel");
		tagPanel.add(tagLabelPanel, BorderLayout.NORTH);
		
		JLabel lblTags = new JLabel("Tags");
		tagLabelPanel.add(lblTags);
		
		JPanel tagListPanel = new JPanel();
		tagPanel.add(tagListPanel, BorderLayout.CENTER);
		
		JList<Tag> tagListView = new JList<>();
		tagListView.setName("tagListView");
		tagListView.setFont(new Font("Dialog", Font.PLAIN, 12));
		tagListPanel.add(tagListView);
		
		JPanel tagControlPanel = new JPanel();
		tagControlPanel.setName("tagControlPanel");
		tagPanel.add(tagControlPanel, BorderLayout.SOUTH);
		
		JButton newTagBtn = new JButton("new Tag");
		newTagBtn.setName("newTagBtn");
		newTagBtn.addActionListener(e -> newTagAction());
		tagControlPanel.add(newTagBtn);
		
		tagListModel = new DefaultComboBoxModel<>();
		
		tagListView.setModel(tagListModel);
	}
	
	@Override
	public void showAllTodo(List<Todo> list) {
		list.forEach(todo -> todoTableModel.addTodo(todo));
	}

	@Override
	public void showAllTags(List<Tag> tags) {
		tags.forEach(tag -> tagListModel.addElement(tag));
	}
	
	@Override
	public void addTodo(Todo todo) {
		LOGGER.debug("Adding '{}'.", todo);
		todoTableModel.addTodo(todo);		
	}

	@Override
	public void addTag(Tag tag) {
		LOGGER.debug("Adding '{}'.", tag);
		tagListModel.addElement(tag);
	}

	@Override
	public void removeTodo(Todo todo) {
		LOGGER.debug("Removing '{}'.", todo);
		todoTableModel.removeTodo(todo);
	}

	@Override
	public void removeTag(Tag tag) {
		LOGGER.debug("Removing '{}'.", tag);
		tagListModel.removeElement(tag);
	}

	@Override
	public void error(String message) {
		LOGGER.error(message);
		JOptionPane.showMessageDialog(this, message);
	}
	
	public Todo getSelectedTodo() {
		int modelIndex = todoTable.convertRowIndexToModel(todoTable.getSelectedRow());
		return todoTableModel.getTodoAtRow(modelIndex);
	}
	
	private void editTodo() {
		todoController.editTodoDialog(tagListModel, getSelectedTodo());
	}

	private void removeTodoAction() {
		LOGGER.debug("selected todo {}", getSelectedTodo());
		todoController.removeTodo(getSelectedTodo());
	}

	private void newTagAction() {
		LOGGER.debug("'new Tag' button pressed.");
		todoController.newTagDialog();
	}
	
	private void newTodoAction(){
		LOGGER.debug("'new To Do' button pressed.");
		todoController.newTodoDialog(this.tagListModel);
	}
	
	private void rowSelected() {
		this.removeTodoButton.setEnabled(true);
	}
	
	/* Getters and Setters */
	
	public TodoController getController() {
		return this.todoController;
	}

	public void setController(TodoController controller) {
		this.todoController = controller;
		todoTableModel.setController(controller);
	}
	
	public DefaultComboBoxModel<Tag> getTagListModel() {
		return this.tagListModel;
	}

	public void setTagListModel(DefaultComboBoxModel<Tag> comboModel) {
		this.tagListModel = comboModel;
	}
}
