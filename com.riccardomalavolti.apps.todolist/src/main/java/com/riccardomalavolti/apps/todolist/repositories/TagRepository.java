package com.riccardomalavolti.apps.todolist.repositories;

import java.util.Set;

import com.riccardomalavolti.apps.todolist.Tag;

public interface TagRepository {
	Set<Tag> findAll();
	Set<Tag> findByText(String text);
	
	void addTag(Tag tag);
	void updateTag(Tag tag);
	void removeTag(Tag tag);
	void clear();
}
