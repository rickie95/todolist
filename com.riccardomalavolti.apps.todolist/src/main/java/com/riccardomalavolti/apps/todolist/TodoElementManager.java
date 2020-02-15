package com.riccardomalavolti.apps.todolist;

import java.util.ArrayList;

public class TodoElementManager {

	private ArrayList<TodoElement> todoCollection;
	
	public TodoElementManager() {
		todoCollection = new ArrayList<>();
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
