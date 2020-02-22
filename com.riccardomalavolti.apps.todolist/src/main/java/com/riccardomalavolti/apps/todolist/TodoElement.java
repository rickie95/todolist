package com.riccardomalavolti.apps.todolist;

import java.util.HashSet;
import java.util.Set;

public class TodoElement {
	
	private static int counter = 1;

	private String body;
	private boolean completed;
	private int id;
	private Set<Tag> tags;
	
	public static int computeId() {
		return counter++;
	}
	
	public TodoElement() {
		this.body = "";
		this.id = computeId();
		this.tags = new HashSet<>();
	}
	
	public TodoElement(String body) {
		this.body = body;
		this.id = computeId();
		this.tags = new HashSet<>();
	}
	
	public TodoElement(TodoElement te) {
		if(te == null)
			te = new TodoElement("");
		
		this.id = computeId();
		this.body = te.getBody();
		this.tags = te.getTagList();
		this.completed = te.getStatus();
		
	}
	
	public boolean isTaggedAs(Tag tag) {
		return tags.contains(tag);
	}

	public String getBody() {
		return this.body;
	}
	
	public void setBody(String text) {
		if (text == null)
			text = "";
		this.body = text;
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
	
	public Set<Tag> getTagList() {
		return tags;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		return false;
	}

	

}
