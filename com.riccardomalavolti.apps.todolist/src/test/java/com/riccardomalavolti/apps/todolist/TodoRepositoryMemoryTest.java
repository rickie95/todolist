package com.riccardomalavolti.apps.todolist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TodoRepositoryMemoryTest {

	private TodoRepository todoRepository;
	
	@Before
	public void setup() {
		todoRepository = new TodoRepositoryMemory();
	}
	
	@After
	public void cleanUp() {
		todoRepository.clear();
	}
	
	@Test
	public void testAddTodoElement() {
		int prevSize = todoRepository.findAll().size();
		todoRepository.addTodoElement(new TodoElement("Foo to bar"));
		int postSize = todoRepository.findAll().size();
		
		assertEquals(prevSize + 1, postSize);
	}
	
	@Test
	public void testUpdateTodoElement() {
		TodoElement te = new TodoElement("Foo");
		todoRepository.addTodoElement(te);
		String modifiedBody = "Bar";
		te.setBody(modifiedBody);
		
		todoRepository.updateTodoElement(te);
		
		assertEquals(modifiedBody, todoRepository.findById(te).getBody());
	}
	
	@Test
	public void testUpdateIfTodoDoesntExist() {
		TodoElement te = new TodoElement("I will not be added");
		String modifiedBody = "Bar";
		te.setBody(modifiedBody);
		
		todoRepository.updateTodoElement(te);
		
		assertNull(todoRepository.findById(te));
	}
	
	@Test
	public void testUpdateIfRefIsNull() {
		TodoElement te = null;
		
		todoRepository.updateTodoElement(te);
		
		assertNull(todoRepository.findById(te));
	}
	
	

	@Test
	public void testFindByPartialBody() {
		String body = "testFindMyBody";
		String bodyToSearch = "test";
		todoRepository.addTodoElement(new TodoElement(body));
		
		List<TodoElement> results = todoRepository.findByBody(bodyToSearch);
		
		assertTrue(body.contains(bodyToSearch));
		assertEquals(1, results.size());
		assertTrue(results.get(0).getBody().contains(bodyToSearch));		
	}
	
	@Test
	public void testFindByBodyReturnsMultipleItems() {
		String body1 = "testFindMyBody";
		String body2 = "testFindMyBody, an other one";
		String bodyToSearch = "test";
		todoRepository.addTodoElement(new TodoElement(body1));
		todoRepository.addTodoElement(new TodoElement(body2));
		
		List<TodoElement> results = todoRepository.findByBody(bodyToSearch);
		
		assertTrue(body1.contains(bodyToSearch));
		assertTrue(body2.contains(bodyToSearch));
		assertEquals(2, results.size());
		assertTrue(results.get(0).getBody().contains(bodyToSearch));
		assertTrue(results.get(1).getBody().contains(bodyToSearch));
	}
	
	@Test 
	public void testFindByNullBody() {
		String body1 = "testFindMyBody";
		String bodyToSearch = null;
		
		List<TodoElement> results = todoRepository.findByBody(bodyToSearch);
		
		assertEquals(0, results.size());
	}
	
	@Test
	public void testFindByBodyNoResults() {
		todoRepository.addTodoElement(new TodoElement("Foo"));
		todoRepository.addTodoElement(new TodoElement("Bar"));
		
		List<TodoElement> results = todoRepository.findByBody("Baz");
		
		assertEquals(0, results.size());
	}
	
	@Test
	public void testRemoveTodoElement() {
		TodoElement te = new TodoElement("To be removed");
		todoRepository.addTodoElement(te);
		int prevSize = todoRepository.findAll().size();
		
		todoRepository.removeTodoElement(te);
		int postSize = todoRepository.findAll().size();
		
		assertEquals(prevSize - 1, postSize);
	}
	
	@Test
	public void testFindByTag() {
		Tag tag = new Tag("bar");
		TodoElement te1 = new TodoElement("todo one");
		TodoElement te2 = new TodoElement("todo two");
		te1.addTag(tag);
		te2.addTag(tag);
		todoRepository.addTodoElement(te1);
		todoRepository.addTodoElement(te2);
		
		List<TodoElement> results = todoRepository.findByTag(tag);
		
		assertNotNull(results);
		assertEquals(2, results.size());
		assertTrue(results.contains(te1));
		assertTrue(results.contains(te2));
		
	}
	
	@Test
	public void testFindByTagNoResults() {
		Tag tag = new Tag("bar");
		TodoElement te1 = new TodoElement("tagged: bar");
		TodoElement te2 = new TodoElement("not tagged");
		te1.addTag(tag);
		
		todoRepository.addTodoElement(te1);
		todoRepository.addTodoElement(te2);
		
		List<TodoElement> results = todoRepository.findByTag(new Tag("Foo"));
	
		assertNotNull(results);
		assertEquals(0, results.size());
	}
	
	@Test
	public void testFindByNullTag() {
		Tag tag = null;
		todoRepository.addTodoElement(new TodoElement("Foo"));
		
		List<TodoElement> results = todoRepository.findByTag(tag);
		
		assertEquals(0, results.size());
	}
	
	@Test
	public void testFindByIDWith() {
		TodoElement te = new TodoElement("This is not in repository");
		
		assertNull(todoRepository.findById(te));
	}
	
	@Test
	public void testFindByIdNUll() {
		TodoElement te = null;
		
		assertNull(todoRepository.findById(te));
	}
	
	@Test
	public void testFindByIdIfTodoCollectionIsEmpty() {
		TodoElement te = new TodoElement("Foo");
		
		assertNull(todoRepository.findById(te));
	}
	
	@Test
	public void testClear() {
		todoRepository.addTodoElement(new TodoElement("Foo"));
		todoRepository.addTodoElement(new TodoElement("Bar"));
		
		todoRepository.clear();
		
		assertEquals(0, todoRepository.findAll().size());
	}
}
