package com.riccardomalavolti.apps.todolist;

import java.util.Set;

public interface TagRepository {
	Set<Tag> findAll();
	Set<Tag> findByText(String text);
	
	void addTag(Tag tag);
	void updateTag(Tag tag);
	void removeTag(Tag tag);
}
