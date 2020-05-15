package com.riccardomalavolti.apps.todolist.repositories.tag;

import java.util.Set;

import com.riccardomalavolti.apps.todolist.model.Tag;

public interface TagRepository {
	Set<Tag> findAll();
	Set<Tag> findByText(String text);
	Tag findById(String id);
	String computeNewId();
	
	void addTag(Tag tag);
	void updateTag(Tag tag);
	void removeTag(Tag tag);
	void clear();
}
