package com.riccardomalavolti.apps.todolist.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.riccardomalavolti.apps.todolist.Main;
import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.model.Todo;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainView extends JFrame implements TodoView{


	private static final Logger LOGGER = LogManager.getLogger(MainView.class);
	
	private JPanel contentPane;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainView frame = new MainView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainView() {
		setTitle("To Do List");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		
		// Todos panel part
		JPanel todoPanel = new JPanel();
		contentPane.add(todoPanel, BorderLayout.CENTER);
		todoPanel.setLayout(new BorderLayout(0, 0));
				
		JPanel todoControlPanel = new JPanel();
		todoPanel.add(todoControlPanel, BorderLayout.SOUTH);
		
		JButton newTodoBtn = new JButton("new To Do");
		newTodoBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LOGGER.info("'new To Do' button pressed.");
			}
		});
		todoControlPanel.add(newTodoBtn);
		
		JPanel todoLabelPanel = new JPanel();
		todoPanel.add(todoLabelPanel, BorderLayout.NORTH);
		
		JLabel lblTodo = new JLabel("Todo");
		todoLabelPanel.add(lblTodo);
		
		JPanel todoListPanel = new JPanel();
		todoListPanel.setBorder(new EmptyBorder(30, 20, 10, 20));
		todoPanel.add(todoListPanel, BorderLayout.WEST);
		
		
		Object[][] todoRows = new Object[][] {
			{false, "Do the laundry after the game (Home) (Work)"},
			{true, "Finish commit (Work)"},
			{false, "Realize todo list view."},
		};
		
		TodoTableModel todoTableModel =  new TodoTableModel(todoRows);
		
		table = new JTable();
		table.setShowVerticalLines(false);
		table.setShowGrid(false);
		table.setShowHorizontalLines(false);
		table.setModel(todoTableModel);
		table.getColumnModel().getColumn(0).setPreferredWidth(30);
		table.getColumnModel().getColumn(1).setPreferredWidth(300);
		todoListPanel.add(table);
		
		
		// Tags panel part
		JPanel tagsPanel = new JPanel();
		tagsPanel.setBorder(new LineBorder(Color.GRAY, 1, true));
		contentPane.add(tagsPanel, BorderLayout.EAST);
		tagsPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel tagLabelPanel = new JPanel();
		tagsPanel.add(tagLabelPanel, BorderLayout.NORTH);
		
		JLabel lblTags = new JLabel("Tags");
		tagLabelPanel.add(lblTags);
		
		JPanel tagListPanel = new JPanel();
		tagsPanel.add(tagListPanel, BorderLayout.CENTER);
		
		JList tagListView = new JList();
		tagListView.setFont(new Font("Dialog", Font.PLAIN, 12));
		tagListPanel.add(tagListView);
		
		JPanel tagControlPanel = new JPanel();
		tagsPanel.add(tagControlPanel, BorderLayout.SOUTH);
		
		JButton newTagBtn = new JButton("new Tag");
		tagControlPanel.add(newTagBtn);
		
		DefaultListModel tagListModel = new DefaultListModel();
		tagListModel.addElement("Casa");
		tagListModel.addElement("Lavoro");
		
		tagListView.setModel(tagListModel);
		
	}

	@Override
	public void showAllTodo(List<Todo> list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showAllTags(List<Tag> tags) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateTagView(List<Tag> tagList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateTodoView(List<Todo> todoList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateTodo(Todo todo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateTag(Tag tag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeTodo(Todo todo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeTag(Tag tag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTodo(Todo todo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTag(Tag tag) {
		// TODO Auto-generated method stub
		
	}

}
