package com.riccardomalavolti.apps.todolist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.riccardomalavolti.apps.todolist.repositories.TagRepository;
import com.riccardomalavolti.apps.todolist.repositories.TagRepositoryInMemory;

public class TagRepositoryInMemoryTest {
	
	TagRepository tagRepository;
	
	@Before
	public void setup() {
		tagRepository = new TagRepositoryInMemory();
	}
	
	@After
	public void clearUp() {
		tagRepository.clear();
	}
	
	@Test
	public void testFindAll() {
		Tag tagOne = new Tag("0", "Foo");
		Tag tagTwo = new Tag("1", "Bar");
		tagRepository.addTag(tagOne);
		tagRepository.addTag(tagTwo);
		
		Set<Tag> results = tagRepository.findAll();
		
		assertNotNull(results);
		assertEquals(2, results.size());
		assertTrue(results.contains(tagOne));
		assertTrue(results.contains(tagTwo));
	}
	
	@Test
	public void testFindAllIfEmpty() {
		Set<Tag> results = tagRepository.findAll();
		
		assertNotNull(results);
		assertEquals(0, results.size());
	}
	
	@Test
	public void testFindByText() {
		Tag tagOne = new Tag("0", "Foo");
		Tag tagTwo = new Tag("1", "Bar");
		Tag tagThree = new Tag("2", "Cooc-coo");
		tagRepository.addTag(tagOne);
		tagRepository.addTag(tagTwo);
		tagRepository.addTag(tagThree);
		String textToSearch = "oo";
		
		Set<Tag> results = tagRepository.findByText(textToSearch);
		
		assertNotNull(results);
		assertEquals(2, results.size());
		assertTrue(results.contains(tagOne));
		assertTrue(results.contains(tagThree));
	}
	
	@Test
	public void testFindByTextNoResults() {
		Tag tagOne = new Tag("0", "Foo");
		Tag tagTwo = new Tag("1", "Bar");
		Tag tagThree = new Tag("2", "Cooc-coo");
		tagRepository.addTag(tagOne);
		tagRepository.addTag(tagTwo);
		tagRepository.addTag(tagThree);
		String textToSearch = "bonobo";
		
		Set<Tag> results = tagRepository.findByText(textToSearch);
		
		assertNotNull(results);
		assertEquals(0, results.size());
	}
	
	@Test
	public void testFindByNullText() {
		Tag tagOne = new Tag("0", "Foo");
		Tag tagTwo = new Tag("1", "Bar");
		Tag tagThree = new Tag("2", "Cooc-coo");
		tagRepository.addTag(tagOne);
		tagRepository.addTag(tagTwo);
		tagRepository.addTag(tagThree);
		String textToSearch = null;
		
		Set<Tag> results = tagRepository.findByText(textToSearch);
		
		assertNotNull(results);
		assertEquals(0, results.size());
	}
	
	@Test
	public void testFindById() {
		Tag tagOne = new Tag("0", "Foo");
		Tag tagTwo = new Tag("1", "Bar");

		tagRepository.addTag(tagOne);
		tagRepository.addTag(tagTwo);
		
		Tag tag = tagRepository.findById("1");
		
		assertNotNull(tag);
		assertEquals(tag, tagTwo);
	}
	
	@Test
	public void testFindByIdWhenItDoesntExists() {
		Tag tagOne = new Tag("0", "Foo");

		tagRepository.addTag(tagOne);
		
		Tag tag = tagRepository.findById("1");
		
		assertNull(tag);
	}
	
	@Test
	public void testFindByIdWhenIDIsNull() {
		Tag tagOne = new Tag("0", "Foo");

		tagRepository.addTag(tagOne);
		
		Tag tag = tagRepository.findById(null);
		
		assertNull(tag);
	}
	
	@Test
	public void testUpdateTag() {
		Tag tag = new Tag("0","Foo");
		tagRepository.addTag(tag);
		tag.setBody("Bar");
		
		tagRepository.updateTag(tag);
		Tag res = (Tag) (tagRepository.findAll().toArray())[0];
		assertEquals("Bar", res.getText());
	}
	
	@Test 
	public void testUpdateTagIfNotPresent() {
		Tag tag = new Tag("0", "Foo");
		
		tagRepository.updateTag(tag);
		
		Set<Tag> results = tagRepository.findAll();
		assertEquals(0, results.size());
	}
	
	@Test
	public void testRemoveTag() {
		Tag tag = new Tag("0", "Foo");
		tagRepository.addTag(tag);
		
		tagRepository.removeTag(tag);
		assertEquals(0, tagRepository.findAll().size());
	}
	
	@Test
	public void testClear() {
		tagRepository.addTag(new Tag("0","Foo"));
		
		assertEquals(1, tagRepository.findAll().size());
		tagRepository.clear();
		assertEquals(0, tagRepository.findAll().size());
		
	}

}
