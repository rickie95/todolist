package com.riccardomalavolti.apps.todolist;

import java.util.ArrayList;

public class TodoElement {
	
	private static int counter = 0;

	private String body;
	private boolean completed;
	private int id;
	private ArrayList<Tag> tags;
	
	public TodoElement(String body) {
		this.body = body;
		this.id = counter++;
		tags = new ArrayList<>();
	}

	public String getBody() {
		return this.body;
	}

	public void setAsCompleted() {
		this.completed = true;
	}
	
	public void setAsUncompleted() {
		this.completed = false;
	}

	public boolean getStatus() {
		return this.completed;
	}

	public int getId() {
		return this.id;
	}

	public int getTagSize() {
		return tags.size();
	}

	public void addTag(Tag tag) {
		tags.add(tag);	
	}

}
