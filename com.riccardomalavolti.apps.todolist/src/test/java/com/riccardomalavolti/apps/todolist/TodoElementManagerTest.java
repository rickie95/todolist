package com.riccardomalavolti.apps.todolist;

import static org.junit.Assert.*;

import org.junit.Test;

public class TodoElementManagerTest {

	private TodoElementManager todoManager;
	
	@Test
	public void testAddTodoElement() {
		todoManager = new TodoElementManager();
		TodoElement tElem = new TodoElement("foo");
		int prev_size = todoManager.size();
		
		assertTrue(todoManager.addTodoElement(tElem));
		
		assertEquals(tElem, todoManager.getElemByIndex(prev_size));
		
		int actual_size = todoManager.size();
		
		assertEquals(prev_size + 1, actual_size);
	}
	
	@Test
	public void testAddTagToATodoElement() {
		todoManager = new TodoElementManager();
		TodoElement tElem = new TodoElement("foo");
		TagManager tManager = new TagManager();
		
		Tag tag = tManager.getTagByName("bar tag");
		tElem.addTag(tag);
		
		assertEquals(1, tElem.getTagSize());
		assertTrue(tag != null);
		Tag lastInsertedTag = tElem.getTagList().get(tElem.getTagSize() - 1);
		assertTrue(tag.getId() == lastInsertedTag.getId());
	}
	
	@Test
	public void testSize() {
		todoManager = new TodoElementManager();
		assertTrue(todoManager.addTodoElement(new TodoElement("foo")));
		assertTrue(todoManager.addTodoElement(new TodoElement("bar")));
		assertTrue(todoManager.addTodoElement(new TodoElement("spez")));
		
		assertEquals(3, todoManager.size());
		
	}
}
