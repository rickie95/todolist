package com.riccardomalavolti.apps.todolist;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.riccardomalavolti.apps.todolist.repositories.TodoRepository;
import com.riccardomalavolti.apps.todolist.repositories.TodoRepositoryMemory;

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
		
		assertThat(postSize).isEqualTo(prevSize + 1);
	}
	
	@Test
	public void testUpdateTodoElement() {
		TodoElement te = new TodoElement("Foo");
		todoRepository.addTodoElement(te);
		String modifiedBody = "Bar";
		te.setBody(modifiedBody);
		
		todoRepository.updateTodoElement(te);
		
		assertThat(todoRepository.findById(te).getBody()).isEqualTo(modifiedBody);
	}
	
	@Test
	public void testUpdateIfTodoDoesntExist() {
		TodoElement te = new TodoElement("I will not be added");
		String modifiedBody = "Bar";
		te.setBody(modifiedBody);
		
		todoRepository.updateTodoElement(te);
		
		assertThat(todoRepository.findById(te)).isNull();
	}
	
	@Test
	public void testUpdateIfRefIsNull() {
		TodoElement te = null;
		
		todoRepository.updateTodoElement(te);
		
		assertThat(todoRepository.findById(te)).isNull();
	}
	
	

	@Test
	public void testFindByPartialBody() {
		String body = "testFindMyBody";
		String bodyToSearch = "test";
		todoRepository.addTodoElement(new TodoElement(body));
		
		List<TodoElement> results = todoRepository.findByBody(bodyToSearch);
		
		assertThat(body).contains(bodyToSearch);
		assertThat(results).hasSize(1);
		assertThat(results.get(0).getBody()).contains(bodyToSearch);		
	}
	
	@Test
	public void testFindByBodyReturnsMultipleItems() {
		String body1 = "testFindMyBody";
		String body2 = "testFindMyBody, an other one";
		String bodyToSearch = "test";
		todoRepository.addTodoElement(new TodoElement(body1));
		todoRepository.addTodoElement(new TodoElement(body2));
		
		List<TodoElement> results = todoRepository.findByBody(bodyToSearch);
		
		assertThat(body1).contains(bodyToSearch);
		assertThat(body2).contains(bodyToSearch);
		assertThat(results).hasSize(2);
		assertThat(results.get(0).getBody()).contains(bodyToSearch);
		assertThat(results.get(1).getBody()).contains(bodyToSearch);
	}
	
	@Test 
	public void testFindByNullBody() {
		String bodyToSearch = null;
		
		List<TodoElement> results = todoRepository.findByBody(bodyToSearch);
		
		assertThat(results).hasSize(0);
	}
	
	@Test
	public void testFindByBodyNoResults() {
		todoRepository.addTodoElement(new TodoElement("Foo"));
		todoRepository.addTodoElement(new TodoElement("Bar"));
		
		List<TodoElement> results = todoRepository.findByBody("Baz");
		
		assertThat(results).hasSize(0);
	}
	
	@Test
	public void testRemoveTodoElement() {
		TodoElement te = new TodoElement("To be removed");
		todoRepository.addTodoElement(te);
		int prevSize = todoRepository.findAll().size();
		
		todoRepository.removeTodoElement(te);
		int postSize = todoRepository.findAll().size();
		
		assertThat(postSize).isEqualTo(prevSize - 1);
	}
	
	@Test
	public void testFindByTag() {
		Tag tag = new Tag("0", "bar");
		TodoElement te1 = new TodoElement("todo one");
		TodoElement te2 = new TodoElement("todo two");
		te1.addTag(tag);
		te2.addTag(tag);
		todoRepository.addTodoElement(te1);
		todoRepository.addTodoElement(te2);
		
		List<TodoElement> results = todoRepository.findByTag(tag);
		
		assertThat(results).isNotNull();
		assertThat(results).hasSize(2);
		assertThat(results).contains(te1);
		assertThat(results).contains(te2);
		
	}
	
	@Test
	public void testFindByTagNoResults() {
		Tag tag = new Tag("0", "bar");
		TodoElement te1 = new TodoElement("tagged: bar");
		TodoElement te2 = new TodoElement("not tagged");
		te1.addTag(tag);
		
		todoRepository.addTodoElement(te1);
		todoRepository.addTodoElement(te2);
		
		List<TodoElement> results = todoRepository.findByTag(new Tag("1", "Foo"));
	
		assertThat(results).isNotNull();
		assertThat(results).hasSize(0);
	}
	
	@Test
	public void testFindByNullTag() {
		Tag tag = null;
		todoRepository.addTodoElement(new TodoElement("Foo"));
		
		List<TodoElement> results = todoRepository.findByTag(tag);
		
		assertThat(results).hasSize(0);
	}
	
	@Test
	public void testFindByIDWith() {
		TodoElement te = new TodoElement("This is not in repository");
		TodoElement recoveredTodo = todoRepository.findById(te);
		assertThat(recoveredTodo).isNull();
	}
	
	@Test
	public void testFindByIdNUll() {
		TodoElement te = null;
		TodoElement recoveredTodo = todoRepository.findById(te);
		assertThat(recoveredTodo).isNull();
	}
	
	@Test
	public void testFindByIdIfTodoCollectionIsEmpty() {
		TodoElement te = new TodoElement("Foo");
		TodoElement recoveredTodo = todoRepository.findById(te);
		assertThat(recoveredTodo).isNull();
	}
	
	@Test
	public void testClear() {
		todoRepository.addTodoElement(new TodoElement("Foo"));
		todoRepository.addTodoElement(new TodoElement("Bar"));
		
		todoRepository.clear();
		
		assertThat(todoRepository.findAll()).hasSize(0);
	}
}
