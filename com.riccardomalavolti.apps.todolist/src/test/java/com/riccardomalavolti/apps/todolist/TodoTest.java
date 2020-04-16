package com.riccardomalavolti.apps.todolist;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.model.Todo;

public class TodoTest {

	private Todo todoelem;

	@Test
	public void testTodoElement() {
		todoelem = new Todo();
		assertEquals("", todoelem.getBody());
	}
	
	@Test
	public void testTodoElementWithEmptyText() {
		todoelem = new Todo("");
		assertEquals("", todoelem.getBody());
	}
	
	@Test
	public void testTodoElementCopyConstructor() {
		todoelem = new Todo("foo");
		Todo copy = new Todo(todoelem);
		
		assertEquals(todoelem.getBody(), copy.getBody());
		
		Todo copyWithNull = new Todo((Todo)null);
		
		assertEquals("", copyWithNull.getBody());
	}
	
	@Test
	public void testTodoElementWithStringText() {
		String ss = "fooo, bar. @ ì";
		todoelem = new Todo(ss);
		assertEquals(ss, todoelem.getBody());
	}
	
	@Test
	public void testSetBody() {
		todoelem = new Todo();
		String text = "foo";
		todoelem.setBody(text);
		
		assertEquals(text, todoelem.getBody());
		
		text = null;
		todoelem.setBody(text);
		
		assertEquals("", todoelem.getBody());
	}
	
	@Test
	public void testTodoElementCompletedFlag() {
		todoelem = new Todo("foo");
		
		// True Version
		todoelem.setAsCompleted(true);
		assertTrue(todoelem.getStatus());
		
		//False Version
		todoelem.setAsCompleted(false);
		assertFalse(todoelem.getStatus());
	}
	
	@Test
	public void testGetTagSize() {
		todoelem = new Todo();
		todoelem.addTag(new Tag("0", "foo"));
		todoelem.addTag(new Tag("1","bar"));
		
		assertEquals(2, todoelem.getTagSize());
	}
	
	@Test
	public void testGetTagList() {
		todoelem = new Todo();
		Tag tFoo = new Tag("0", "foo");
		Tag tBar = new Tag("1", "bar");
		todoelem.addTag(tFoo);
		todoelem.addTag(tBar);
		
		Set<Tag> tagList = todoelem.getTagList();
		
		assertEquals(2, tagList.size());
		assertTrue(tagList.contains(tFoo));
		assertTrue(tagList.contains(tBar));
	}
	
}
