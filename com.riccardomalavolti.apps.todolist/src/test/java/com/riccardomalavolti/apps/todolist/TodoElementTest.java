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
	public void testIdIsIncremental() {
		assertTrue(new TodoElement("").getId() < new TodoElement("").getId());
	}
}
