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

	public TodoElement getTodoByText(String text) {
		TodoElement te = null;
		
		for (TodoElement t : todoCollection)
			if (t.getBody().equals(text))
				te = t;
		
		return te;
	}

	public ArrayList<TodoElement> getTodoList() {
		return todoCollection;
	}

}
