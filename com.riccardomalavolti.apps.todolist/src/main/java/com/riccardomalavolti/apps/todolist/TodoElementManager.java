package com.riccardomalavolti.apps.todolist;

import java.util.Vector;

public class TodoElementManager {

	private Vector<TodoElement> todoCollection;
	
	public TodoElementManager() {
		todoCollection = new Vector<TodoElement>();
	}
	
	
	public int size() {
		return todoCollection.size();
	}

	public boolean addTodoElement(TodoElement todoElement) {
		return todoCollection.add(todoElement);
	}

	public TodoElement getElemByIndex(int index) {
		return todoCollection.get(index);
	}

}
