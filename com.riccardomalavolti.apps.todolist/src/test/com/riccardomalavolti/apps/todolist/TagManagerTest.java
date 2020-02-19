package com.riccardomalavolti.apps.todolist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TagManagerTest {
	
	@Test
	public void testNewTag() {
		TagManager tm = new TagManager();
		
		int old_tag_list_size = tm.tagListSize();
		tm.newTag("bar");
		int new_tag_list_size = tm.tagListSize();
		
		
		assertEquals(old_tag_list_size + 1, new_tag_list_size);
	}

	@Test
	public void testGetTagByName() {
		TagManager tm = new TagManager();
		String tag_text = "foo";
		Tag tag = tm.newTag(tag_text);
		
		assertEquals(tag, tm.getTagByName(tag_text));
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
		String tag_text = "foo";
		Tag tag1 = tm.newTag(tag_text);
		Tag tag2 = tm.newTag(tag_text);
		
		assertEquals(tag1.getId(), tm.getTagByName(tag_text).getId());
		assertNotEquals(tag2.getId(), tm.getTagByName(tag_text));
	}
}
