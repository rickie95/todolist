package com.riccardomalavolti.apps.todolist;

import java.util.ArrayList;
import java.util.List;

public class TodoElementManager {

	private List<TodoElement> todoCollection;
	
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

	public List<TodoElement> getTodoList() {
		return todoCollection;
	}

}
