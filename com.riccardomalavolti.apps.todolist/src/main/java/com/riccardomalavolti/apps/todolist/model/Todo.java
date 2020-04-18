package com.riccardomalavolti.apps.todolist.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Todo {
	
	private String body;
	private boolean completed;
	private String id;
	private Set<Tag> tags;
	
	public Todo() {
		this.body = "";
		this.id = null;
		this.tags = new HashSet<>();
	}
	
	public Todo(String body) {
		this.body = body;
		this.id = null;
		this.tags = new HashSet<>();
	}
	
	public Todo(String id, String body) {
		this.body = body;
		this.id = id;
		this.tags = new HashSet<>();
	}
	
	public Todo(Todo te) {
		if(te == null)
			te = new Todo("");
		
		this.id = te.getId();
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

	public void setAsCompleted(boolean status) {
		this.completed = status;
	}

	public boolean getStatus() {
		return this.completed;
	}

	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
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
	
	public void setTagSet(Set<Tag> tagSet) {
		this.tags = tagSet;
	}

	public String toString() {
		StringBuilder ssBld = new StringBuilder(body);
		for(Tag t : tags)
			ssBld.append(" ("+t.getText()+")");
		return ssBld.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Todo other = (Todo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	public void clearTags() {
		this.tags.clear();
	}

	public String getFormattedTags() {
		return Tag.listToString(new ArrayList<Tag>(this.tags));
	}
	
	
}
