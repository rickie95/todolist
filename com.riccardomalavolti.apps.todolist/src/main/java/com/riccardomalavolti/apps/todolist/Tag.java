package com.riccardomalavolti.apps.todolist;

public class Tag {

	static int counter = 1;
	
	private String text;
	private int id;
	
	public Tag(String text) {
		this.text = text;
		this.id = counter++;
	}

	public int getId() {
		return this.id;
	}

}
