package com.riccardomalavolti.apps.todolist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TagManagerTest {
	
	private TagManager tagManager;
	
	@Before
	public void setup() {
		tagManager = TagManager.getInstance();
	}
	
	@After
	public void cleanUp() {
		tagManager.clear();
	}
	
	@Test
	public void testNewTag() {		
		int oldTagListSize = tagManager.tagListSize();
		tagManager.newTag("bar");
		int newTagListSize = tagManager.tagListSize();
		
		assertEquals(oldTagListSize + 1, newTagListSize);
	}

	@Test
	public void testGetTagList() {
		Tag t1 = tagManager.newTag("foo");
		Tag t2 = tagManager.newTag("bar");
		
		Set<Tag> tagList = tagManager.getTagList();
		
		assertNotNull(tagList);
		assertTrue(tagList.size() == 2);
		assertTrue(tagList.contains(t1));
		assertTrue(tagList.contains(t2));
	}
	
	@Test
	public void testGetTagByName() {
		String tagText = "foo";
		Tag tag = tagManager.newTag(tagText);
		
		assertEquals(tag, tagManager.getTagByName(tagText));
	}
	
	@Test
	public void testGetTagByNameWhenTheresNoTags() {
		assertNull(tagManager.getTagByName("foo"));
	}
	
	@Test
	public void testGetTagByNameWithANonExistentTagText() {
		tagManager.newTag("foo");
		
		assertNull(tagManager.getTagByName("bar"));
	}
	
	@Test
	public void testGetTagByNameIfAlreadyExist() {
		String tagText = "foo";
		Tag tag1 = tagManager.newTag(tagText);
		Tag tag2 = tagManager.newTag(tagText);
		
		assertTrue(tagManager.tagListSize() == 1);
		assertEquals(tag1.getId(), tagManager.getTagByName(tagText).getId());
		assertNotEquals(tag2.getId(), tagManager.getTagByName(tagText));
	}
	
	@Test
	public void testGetTagByNameIfTagListIsEmpty() {
		assertNull(tagManager.getTagByName("foo"));
	}
	
	@Test
	public void testGetTagByNameWithASimilarName() {
		assertNull(tagManager.getTagByName("fo"));
	}
}
