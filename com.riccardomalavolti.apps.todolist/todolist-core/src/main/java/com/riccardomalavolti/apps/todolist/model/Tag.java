package com.riccardomalavolti.apps.todolist.model;

import java.util.List;
import java.util.Set;

public class Tag {
	
	// This class is not tested since it's a simply Java Plain Object
	
	

	private String text;
	private String id;
	
	public Tag(String text) {
		if (text == null)
			text = "";
		
		this.text = text;
		this.id = null;
	}
	
	public Tag(String id, String text) {
		if (text == null)
			text = "";
		
		this.text = text;
		this.id = id;
	}

	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getText() {
		return this.text;
	}
	
	public void setBody(String string) {
		this.text = string;
	}
	
	public boolean bodyIsEqualTo(String text) {
		if(text != null)
			return this.text.equals(text);
		
		return false;
	}
	
	@Override
	public String toString() {
		return text;
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
		Tag other = (Tag) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	public static String listToString(List<Tag> tagList) {
		StringBuilder ssBld = new StringBuilder();
		for(Tag t : tagList)
			ssBld.append("("+t.getText()+")");
		return ssBld.toString();
	}
	
	public static String listToString(Set<Tag> tagList) {
		StringBuilder ssBld = new StringBuilder();
		for(Tag t : tagList)
			ssBld.append("("+t.getText()+")");
		return ssBld.toString();
	}
	
}
