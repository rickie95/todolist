package com.riccardomalavolti.apps.todolist.view;

import javax.swing.table.DefaultTableModel;

import com.riccardomalavolti.apps.todolist.model.Todo;

public class TodoTableModel extends DefaultTableModel {
	
	private static final long serialVersionUID = 1L;
	
	public TodoTableModel(Object[][] todoRows) {
		super(todoRows, new String[] {null, null});
	}
	
	@Override
    public Class getColumnClass(int column)
    {
        if(column == 0)
        	return Boolean.class;
        
        return String.class;
    }
	
	public void addTodo(Todo todo) {
		this.addRow(new Object[]{todo.getStatus(), todo.getBody()});
	}
	
}
