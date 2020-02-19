package com.riccardomalavolti.apps.todolist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

public class TagManagerTest {
	
	@Test
	public void testNewTag() {
		TagManager tm = new TagManager();
		
		int oldTagListSize = tm.tagListSize();
		tm.newTag("bar");
		int newTagListSize = tm.tagListSize();
		
		
		assertEquals(oldTagListSize + 1, newTagListSize);
	}

	@Test
	public void testGetTagList() {
		TagManager tm = new TagManager();
		Tag t1 = tm.newTag("foo");
		Tag t2 = tm.newTag("bar");
		
		Set<Tag> tagList = tm.getTagList();
		
		assertNotNull(tagList);
		assertTrue(tagList.size() == 2);
		assertTrue(tagList.contains(t1));
		assertTrue(tagList.contains(t2));
		
	}
	
	@Test
	public void testGetTagByName() {
		TagManager tm = new TagManager();
		String tagText = "foo";
		Tag tag = tm.newTag(tagText);
		
		assertEquals(tag, tm.getTagByName(tagText));
	}
	
	@Test
	public void testGetTagByNameWhenTheresNoTags() {
		TagManager tm = new TagManager();
		
		assertNull(tm.getTagByName("foo"));
	}
	
	@Test
	public void testGetTagByNameWithANonExistentTagText() {
		TagManager tm = new TagManager();
		tm.newTag("foo");
		
		assertNull(tm.getTagByName("bar"));
	}
	
	@Test
	public void testGetTagByNameIfAlreadyExist() {
		TagManager tm = new TagManager();
		String tagText = "foo";
		Tag tag1 = tm.newTag(tagText);
		Tag tag2 = tm.newTag(tagText);
		
		assertTrue(tm.tagListSize() == 1);
		assertEquals(tag1.getId(), tm.getTagByName(tagText).getId());
		assertNotEquals(tag2.getId(), tm.getTagByName(tagText));
	}
	
	@Test
	public void testGetTagByNameIfTagListIsEmpty() {
		TagManager tm = new TagManager();
		
		assertNull(tm.getTagByName("foo"));
	}
	
	@Test
	public void testGetTagByNameWithASimilarName() {
		TagManager tm = new TagManager();
		
		assertNull(tm.getTagByName("fo"));
	}
}
