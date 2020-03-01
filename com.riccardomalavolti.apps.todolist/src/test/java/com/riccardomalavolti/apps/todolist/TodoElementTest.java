package com.riccardomalavolti.apps.todolist;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

public class TodoElementTest {

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
		todoelem.setAsCompleted();
		assertTrue(todoelem.getStatus());
		
		//False Version
		todoelem.setAsUncompleted();
		assertFalse(todoelem.getStatus());
	}
	
	@Test
	public void testIdIsIncremental() {
		assertTrue(new Todo().getId() < new Todo().getId());
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
	
	@Test
	public void testHashCode() {
		todoelem = new Todo();
		assertEquals(31 * 1 + todoelem.getId(), todoelem.hashCode());
	}
	
	@Test
	public void testTodoAreEqualsOnlyIfIDsAreEqual() {
		todoelem = new Todo("foo");
		Todo todo2 = new Todo("foo");
		
		assertEquals(todoelem, todoelem);
		assertNotEquals(todoelem, todo2);
		assertNotEquals(todoelem, (Todo)null);
		assertNotEquals(todoelem, new String("foo"));
	}
}
