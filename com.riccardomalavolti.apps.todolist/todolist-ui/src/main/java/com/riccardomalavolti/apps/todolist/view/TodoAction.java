package com.riccardomalavolti.apps.todolist.view;

import java.util.Set;

import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.model.Todo;

public interface TodoAction {
	
	public void sendToController(String body, Set<Tag> tagList);
	
	public Todo getTodo();

}
