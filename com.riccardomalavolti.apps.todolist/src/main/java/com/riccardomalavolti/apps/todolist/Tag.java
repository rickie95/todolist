package com.riccardomalavolti.apps.todolist;

public class Tag {
	
	// This class is not tested since it's a simply Java Plain Object

	static int counter = 0;
	
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
	
	public void setBody(String string) {
		this.text = string;
	}
	
	public boolean bodyIsEqualTo(String text) {
		if(text != null)
			return this.text.equals(text);
		
		return false;
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
		Tag other = (Tag) obj;
		if (id != other.id)
			return false;
		return true;
	}
	

}
