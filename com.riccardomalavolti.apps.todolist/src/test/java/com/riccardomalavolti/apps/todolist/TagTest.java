package com.riccardomalavolti.apps.todolist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TagTest {
	
	private Tag tag;
	
	
	@Test
	public void testCreateTag() {
		String text = "foo";
		tag = new Tag(text);
		
		assertEquals(text, tag.getText());
	}
	
	@Test
	public void testCreateTagWithNullText() {
		String text = null;
		tag = new Tag(text);
		
		assertEquals("", tag.getText());
	}
	
	@Test
	public void testBodyIsEqualTo() {
		String text = "foo";
		Tag tt = new Tag(text);
		
		assertTrue(tt.bodyIsEqualTo(text));
	}
	
	@Test
	public void testBodyIsNotEqualToNullText() {
		String text = null;
		Tag tt = new Tag(text);
		
		assertFalse(tt.bodyIsEqualTo(text));
	}
	
	@Test
	public void testIdsAreIncremental() {
		tag = new Tag("bar");
		Tag t1 = new Tag("foo");
		
		assertTrue(tag.getId() > 0);
		assertTrue(t1.getId() > 0);
		assertTrue(tag.getId()+1 == t1.getId());
		assertTrue(tag.getId() < t1.getId());
	}
	
	@Test
	public void testHashCode() {
		String text = "foo";
		tag = new Tag(text);
		
		assertEquals(31 + tag.getId(), tag.hashCode());
	}
}
