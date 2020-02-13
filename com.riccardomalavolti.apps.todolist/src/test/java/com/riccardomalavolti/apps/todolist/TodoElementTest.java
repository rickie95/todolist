package com.riccardomalavolti.apps.todolist;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TodoElementTest {

	private TodoElement todoelem;

	@Test
	public void testTodoElementWithNullText() {
		todoelem = new TodoElement(null);
		assertNull(todoelem.getBody());
	}
	
	@Test
	public void testTodoElementWithStringText() {
		String ss = "fooo, bar. @ ì";
		todoelem = new TodoElement(ss);
		assertEquals(ss, todoelem.getBody());
	}
	
	@Test
	public void testTodoElementCompletedFlag() {
		todoelem = new TodoElement("foo");
		
		// True Version
		todoelem.setAsCompleted();
		assertTrue(todoelem.getStatus());
		
		//False Version
		todoelem.setAsUncompleted();
		assertFalse(todoelem.getStatus());
	}
	
	@Test
	public void testAddTag() {
		todoelem = new TodoElement("foo");
		
		int prev_tag_size = todoelem.getTagSize();
		Tag tag = new Tag("Tag #1");
		todoelem.addTag(tag);
		
		int actual_tag_size = todoelem.getTagSize();
		
		assertEquals(prev_tag_size + 1, actual_tag_size);
	}
	
	@Test
	public void testIdIsIncremental() {
		TodoElement te1 = new TodoElement("foo");
		TodoElement te2 = new TodoElement("bar");
		
		assertTrue(te1.getId() > 0);
		assertTrue(te2.getId() > 0);
		assertTrue(te1.getId() < te2.getId());
	}
}
