package com.riccardomalavolti.apps.todolist;

public class TodoElement {
	
	private static int counter = 0;

	private String body;
	private boolean completed;
	private int id;
	
	public TodoElement(String body) {
		this.body = body;
		this.id = counter++;
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

}
