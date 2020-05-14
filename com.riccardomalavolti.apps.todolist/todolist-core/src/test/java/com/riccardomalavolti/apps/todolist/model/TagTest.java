package com.riccardomalavolti.apps.todolist.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TagTest {

	private Tag tag;

	@Test
	public void testCreateTag() {
		String text = "foo";
		tag = new Tag("0", text);

		assertEquals(text, tag.getText());
	}

	@Test
	public void testCreateTagWithNullText() {
		String text = null;
		tag = new Tag("0", text);

		assertEquals("", tag.getText());
	}

	@Test
	public void testBodyIsEqualTo() {
		String text = "foo";
		Tag tt = new Tag("0", text);

		assertTrue(tt.bodyIsEqualTo(text));
	}

	@Test
	public void testBodyIsNotEqual() {
		String text = "Bar";
		Tag tt = new Tag("0", "Foo");

		assertFalse(tt.bodyIsEqualTo(text));
	}

	@Test
	public void testBodyIsNotEqualToNullText() {
		String text = null;
		Tag tt = new Tag("0", text);

		assertFalse(tt.bodyIsEqualTo(text));
	}

}
