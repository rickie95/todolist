package com.riccardomalavolti.apps.todolist.repositories.tag;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import com.riccardomalavolti.apps.todolist.model.Tag;

public class TagRepositoryInMemory implements TagRepository{
	
	Set<Tag> tagCollection;
	
	public TagRepositoryInMemory() {
		tagCollection = new HashSet<>();
	}

	@Override
	public Set<Tag> findAll() {
		return tagCollection;
	}

	@Override
	public Set<Tag> findByText(String text) {
		Set<Tag> results = new HashSet<>();
		
		if(text != null)
			for(Tag t : tagCollection)
				if( t.getText().toLowerCase().contains( text.toLowerCase() ) )
					results.add(t);
		
		return results;
	}
	
	@Override
	public Tag findById(String id) {		
		if(id != null)
			for(Tag t : tagCollection)
				if(t.getId().equals(id))
					return t;
		
		return null;
	}

	@Override
	public void addTag(Tag tag) {
		tagCollection.add(tag);
		
	}

	@Override
	public void updateTag(Tag tag) {
		if(tagCollection.remove(tag))
			tagCollection.add(tag);
	}

	@Override
	public void removeTag(Tag tag) {
		tagCollection.remove(tag);
		
	}

	@Override
	public void clear() {
		tagCollection.clear();
		
	}

	@Override
	public String computeNewId() {
		Tag maxIdTag;
		try {
			maxIdTag = Collections.max(tagCollection, 
					Comparator.comparing(Tag::getId));
		}catch(NoSuchElementException ex) {
			return "0";
		}
		
		int nextId = Integer.parseInt(maxIdTag.getId()) + 1;
		return Integer.toString(nextId);
	}

	

}
