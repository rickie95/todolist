package com.riccardomalavolti.apps.todolist.repositories;

import java.util.HashSet;
import java.util.Set;

import com.riccardomalavolti.apps.todolist.Tag;

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

	

}
