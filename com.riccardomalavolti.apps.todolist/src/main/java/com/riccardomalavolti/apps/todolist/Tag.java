package com.riccardomalavolti.apps.todolist;

public class Tag {
	
	// This class is not tested since it's a simply Java Plain Object

	static int counter = 1;
	
	private String text;
	private int id;
	
	public Tag(String text) {
		if (text == null)
			text = "";
		
		this.text = text;
		this.id = counter++;
	}

	public int getId() {
		return this.id;
	}
	
	public String getText() {
		return this.text;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
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
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text)) {
			return false;
		}
		
		return true;
	}
	

}
