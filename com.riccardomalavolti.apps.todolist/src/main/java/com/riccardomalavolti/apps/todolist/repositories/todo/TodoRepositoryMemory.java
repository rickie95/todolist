package com.riccardomalavolti.apps.todolist.repositories.todo;

import java.util.ArrayList;
import java.util.List;

import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.model.Todo;

public class TodoRepositoryMemory implements TodoRepository {

	private List<Todo> todoCollection;
	
	public TodoRepositoryMemory() {
		todoCollection = new ArrayList<>();
	}
	
	@Override
	public List<Todo> findAll() {
		return todoCollection;
	}

	@Override
	public void addTodoElement(Todo te) {
		todoCollection.add(te);
	}

	@Override
	public void updateTodoElement(Todo te) {
		if(todoCollection.remove(te))
				todoCollection.add(te);
	}

	@Override
	public List<Todo> findByTag(Tag tag) {
		List<Todo> results = new ArrayList<>();
		
		if (tag != null)
			for(Todo te : todoCollection)
				if(te.isTaggedAs(tag))
					results.add(te);
		
		return results;
	}

	@Override
	public List<Todo> findByBody(String text) {
		List<Todo> results = new ArrayList<>();
		
		if(text != null)
			for(Todo te : todoCollection)
				if(te.getBody().toLowerCase().contains(text.toLowerCase()))
					results.add(te);
		
		return results;
	}

	@Override
	public void removeTodoElement(Todo te) {
		todoCollection.remove(te);		
	}

	@Override
	public Todo findById(Todo te) {
		for(Todo t : todoCollection)
			if(
					t
					.getId() 
					== 
				te
				.getId()
				)
				return t;
		
		return null;
	}

	@Override
	public void clear() {
		todoCollection.clear();
		
	}

}
