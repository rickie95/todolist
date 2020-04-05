package com.riccardomalavolti.apps.todolist.view;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import com.riccardomalavolti.apps.todolist.controller.TodoController;
import com.riccardomalavolti.apps.todolist.model.Todo;

public class TodoTableModel extends DefaultTableModel {
	
	private static final long serialVersionUID = 1L;
	
	private final Class<?>[] columnClass = new Class[] {
			Boolean.class, Todo.class };
	
	private TodoController controller;
		
	
	public TodoTableModel() {
		super(null, new String[] {"", ""});
	}
	
	@Override
    public Class<?> getColumnClass(int column){
        return columnClass[column];
    }
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// setValue is used only for the tick component
		if(columnIndex == 0) {
			super.setValueAt(aValue, rowIndex, columnIndex);
			Vector<?> row = (Vector<?>) this.dataVector.get(rowIndex);
			Todo todo = (Todo) row.get(1);
			todo.setAsCompleted((boolean)aValue);
			controller.updateTodo(todo);
		}
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
	
	public Todo getTodoAtRow(int rowIndex) {
		return (Todo) this.getValueAt(rowIndex, 1);
	}
	
	public void addTodo(Todo todo) {
		this.addRow(new Object[]{todo.getStatus(), todo});
	}

	public void setController(TodoController controller) {
		this.controller = controller;
	}
	
}
