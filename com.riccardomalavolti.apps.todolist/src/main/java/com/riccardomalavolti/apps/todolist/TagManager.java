package com.riccardomalavolti.apps.todolist;

import java.util.HashSet;
import java.util.Set;

public class TagManager {

	private Set<Tag> tagPool;
	
	public TagManager() {
		tagPool = new HashSet<>();
	}
	
	public Tag getTagByName(String string) {
		if(!tagPool.isEmpty())
			for(Tag t : tagPool)
				if(t.getText().equals(string))
					return t;
		
		return null;
	}

	public int tagListSize() {
		return tagPool.size();
	}

	
	// Return the reference to the tag if already exist, otherwise it is created.
	public Tag newTag(String text) {
		Tag newTag = getTagByName(text);
			
		if (newTag == null) {
			newTag = new Tag(text);
			tagPool.add(newTag);
		}
		
		return newTag;
	}

}