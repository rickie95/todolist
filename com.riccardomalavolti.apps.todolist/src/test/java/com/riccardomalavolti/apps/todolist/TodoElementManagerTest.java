package com.riccardomalavolti.apps.todolist;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class TodoElementManagerTest {

	private TodoElementManager todoManager;
	
	@Test
	public void testAddTodoElement() {
		todoManager = new TodoElementManager();
		TodoElement tElem = new TodoElement("foo");
		int prevSize = todoManager.size();
		
		assertTrue(todoManager.addTodoElement(tElem));
		
		assertEquals(tElem, todoManager.getElemByIndex(prevSize));
		
		int actualSize = todoManager.size();
		
		assertEquals(prevSize + 1, actualSize);
	}
	
	@Test 
	public void testGetTodoElementByText() {
		todoManager = new TodoElementManager();
		TodoElement tElem1 = new TodoElement("foo");
		TodoElement tElem2 = new TodoElement("bar");
		TodoElement tElem3 = new TodoElement("cox");
		todoManager.addTodoElement(tElem1);
		todoManager.addTodoElement(tElem2);
		todoManager.addTodoElement(tElem3);
		
		TodoElement recoveredTe = todoManager.getTodoByText("bar");
		
		assertNotNull(recoveredTe);
		assertEquals(tElem2, recoveredTe);
	}
	
	@Test
	public void testGetTodoElementList() {
		todoManager = new TodoElementManager();
		TodoElement tElem1 = new TodoElement("foo");
		TodoElement tElem2 = new TodoElement("bar");
		TodoElement tElem3 = new TodoElement("cox");
		todoManager.addTodoElement(tElem1);
		todoManager.addTodoElement(tElem2);
		todoManager.addTodoElement(tElem3);
		
		ArrayList<TodoElement> collection = todoManager.getTodoList();
		assertTrue(collection.contains(tElem1));
		assertTrue(collection.contains(tElem2));
		assertTrue(collection.contains(tElem3));
		
	}
	
	@Test
	public void testAddTagToATodoElement() {
		todoManager = new TodoElementManager();
		TodoElement tElem = new TodoElement("foo");
		TagManager tManager = new TagManager();
		
		Tag tag = tManager.newTag("bar tag");
		tElem.addTag(tag);
		
		assertEquals(1, tElem.getTagSize());
		assertNotNull(tag);
		Tag lastInsertedTag = tElem.getTagList().get(tElem.getTagSize() - 1);
		assertEquals(tag, lastInsertedTag);
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
