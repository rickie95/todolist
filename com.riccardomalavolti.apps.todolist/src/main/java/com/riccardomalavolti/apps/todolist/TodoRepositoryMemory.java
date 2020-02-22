package com.riccardomalavolti.apps.todolist;

import java.util.ArrayList;
import java.util.List;

public class TodoRepositoryMemory implements TodoRepository {

	private List<TodoElement> todoCollection;
	
	public TodoRepositoryMemory() {
		todoCollection = new ArrayList<>();
	}
	
	@Override
	public List<TodoElement> findAll() {
		return todoCollection;
	}

	@Override
	public void addTodoElement(TodoElement te) {
		todoCollection.add(te);
	}

	@Override
	public void updateTodoElement(TodoElement te) {
		if(todoCollection.remove(te))
				todoCollection.add(te);
	}

	@Override
	public List<TodoElement> findByTag(Tag tag) {
		List<TodoElement> results = new ArrayList<>();
		for(TodoElement te : todoCollection)
			if(te.isTaggedAs(tag))
				results.add(te);
		
		return results;
	}

	@Override
	public List<TodoElement> findByBody(String text) {
		List<TodoElement> results = new ArrayList<>();
		for(TodoElement te : todoCollection)
			if(te.getBody().toLowerCase().contains(text.toLowerCase()))
				results.add(te);
		
		return results;
	}

	@Override
	public void removeTodoElement(TodoElement te) {
		todoCollection.remove(te);		
	}

	@Override
	public TodoElement findById(TodoElement te) {
		for(TodoElement t : todoCollection)
			if(t.getId() == te.getId())
				return t;
		
		return null;
	}

}
