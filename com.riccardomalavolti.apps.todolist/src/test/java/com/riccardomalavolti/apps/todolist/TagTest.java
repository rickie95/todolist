package com.riccardomalavolti.apps.todolist;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TagTest {
	
	@Test
	public void testIdAreIncremental() {
		Tag t1 = new Tag("foo");
		Tag t2 = new Tag("bar");
		
		assertTrue(t1.getId() > 0);
		assertTrue(t2.getId() > 0);
		assertTrue(t1.getId() < t2.getId());
	}
}
