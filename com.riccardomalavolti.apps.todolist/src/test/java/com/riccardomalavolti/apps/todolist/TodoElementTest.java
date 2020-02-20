package com.riccardomalavolti.apps.todolist;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TodoElementTest {

	private TodoElement todoelem;

	@Test
	public void testTodoElement() {
		todoelem = new TodoElement();
		assertEquals("", todoelem.getBody());
	}
	
	@Test
	public void testTodoElementWithEmptyText() {
		todoelem = new TodoElement("");
		assertEquals("", todoelem.getBody());
	}
	
	@Test
	public void testTodoElementCopyConstructor() {
		todoelem = new TodoElement("foo");
		TodoElement copy = new TodoElement(todoelem);
		
		assertEquals(todoelem.getBody(), copy.getBody());
		
		TodoElement copyWithNull = new TodoElement((TodoElement)null);
		
		assertEquals("", copyWithNull.getBody());
	}
	
	@Test
	public void testTodoElementWithStringText() {
		String ss = "fooo, bar. @ ì";
		todoelem = new TodoElement(ss);
		assertEquals(ss, todoelem.getBody());
	}
	
	@Test
	public void testSetBody() {
		todoelem = new TodoElement();
		String text = "foo";
		todoelem.setBody(text);
		
		assertEquals(text, todoelem.getBody());
		
		text = null;
		todoelem.setBody(text);
		
		assertEquals("", todoelem.getBody());
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
		assertTrue(new TodoElement().getId() < new TodoElement().getId());
	}
	
	@Test
	public void testGetTagSize() {
		todoelem = new TodoElement();
		TagManager tm = new TagManager();
		todoelem.addTag(tm.newTag("foo"));
		todoelem.addTag(tm.newTag("bar"));
		
		assertEquals(2, todoelem.getTagSize());
	}
	
	@Test
	public void testGetTagList() {
		todoelem = new TodoElement();
		TagManager tm = new TagManager();
		Tag tFoo = tm.newTag("foo");
		Tag tBar = tm.newTag("bar");
		todoelem.addTag(tFoo);
		todoelem.addTag(tBar);
		
		List<Tag> tagList = todoelem.getTagList();
		
		assertEquals(2, tagList.size());
		assertEquals(tFoo, tagList.get(0));
		assertEquals(tBar, tagList.get(1));
	}
	
	@Test
	public void testHashCode() {
		todoelem = new TodoElement();
		assertEquals(31 * 1 + todoelem.getId(), todoelem.hashCode());
	}
	
	@Test
	public void testTodoAreEqualsOnlyIfIDsAreEqual() {
		todoelem = new TodoElement("foo");
		TodoElement todo2 = new TodoElement("foo");
		
		assertEquals(todoelem, todoelem);
		assertNotEquals(todoelem, todo2);
		assertNotEquals(todoelem, (TodoElement)null);
		assertNotEquals(todoelem, new String("foo"));
	}
}
