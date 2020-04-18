package com.riccardomalavolti.apps.todolist;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.model.Todo;
import com.riccardomalavolti.apps.todolist.repositories.todo.TodoRepository;
import com.riccardomalavolti.apps.todolist.repositories.todo.TodoRepositoryMemory;

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
		todoRepository.addTodoElement(new Todo("Foo to bar"));
		int postSize = todoRepository.findAll().size();
		
		assertThat(postSize).isEqualTo(prevSize + 1);
	}
	
	@Test
	public void testUpdateTodoElement() {
		Todo te = new Todo("1","Foo");
		todoRepository.addTodoElement(te);
		String modifiedBody = "Bar";
		te.setBody(modifiedBody);
		
		todoRepository.updateTodoElement(te);
		
		assertThat(todoRepository.findById(te).getBody()).isEqualTo(modifiedBody);
	}
	
	@Test
	public void testUpdateIfTodoDoesntExist() {
		Todo te = new Todo("I will not be added");
		String modifiedBody = "Bar";
		te.setBody(modifiedBody);
		
		todoRepository.updateTodoElement(te);
		
		assertThat(todoRepository.findById(te)).isNull();
	}
	
	@Test
	public void testUpdateIfRefIsNull() {
		Todo te = null;
		
		todoRepository.updateTodoElement(te);
		
		assertThat(todoRepository.findById(te)).isNull();
	}
	
	

	@Test
	public void testFindByPartialBody() {
		String body = "testFindMyBody";
		String bodyToSearch = "test";
		todoRepository.addTodoElement(new Todo(body));
		
		List<Todo> results = todoRepository.findByBody(bodyToSearch);
		
		assertThat(body).contains(bodyToSearch);
		assertThat(results).hasSize(1);
		assertThat(results.get(0).getBody()).contains(bodyToSearch);		
	}
	
	@Test
	public void testFindByBodyReturnsMultipleItems() {
		String body1 = "testFindMyBody";
		String body2 = "testFindMyBody, an other one";
		String bodyToSearch = "test";
		todoRepository.addTodoElement(new Todo(body1));
		todoRepository.addTodoElement(new Todo(body2));
		
		List<Todo> results = todoRepository.findByBody(bodyToSearch);
		
		assertThat(body1).contains(bodyToSearch);
		assertThat(body2).contains(bodyToSearch);
		assertThat(results).hasSize(2);
		assertThat(results.get(0).getBody()).contains(bodyToSearch);
		assertThat(results.get(1).getBody()).contains(bodyToSearch);
	}
	
	@Test 
	public void testFindByNullBody() {
		String bodyToSearch = null;
		
		List<Todo> results = todoRepository.findByBody(bodyToSearch);
		
		assertThat(results).hasSize(0);
	}
	
	@Test
	public void testFindByBodyNoResults() {
		todoRepository.addTodoElement(new Todo("Foo"));
		todoRepository.addTodoElement(new Todo("Bar"));
		
		List<Todo> results = todoRepository.findByBody("Baz");
		
		assertThat(results).hasSize(0);
	}
	
	@Test
	public void testRemoveTodoElement() {
		Todo te = new Todo("To be removed");
		todoRepository.addTodoElement(te);
		int prevSize = todoRepository.findAll().size();
		
		todoRepository.removeTodoElement(te);
		int postSize = todoRepository.findAll().size();
		
		assertThat(postSize).isEqualTo(prevSize - 1);
	}
	
	@Test
	public void testFindByTag() {
		Tag tag = new Tag("0", "bar");
		Todo te1 = new Todo("todo one");
		Todo te2 = new Todo("todo two");
		te1.addTag(tag);
		te2.addTag(tag);
		todoRepository.addTodoElement(te1);
		todoRepository.addTodoElement(te2);
		
		List<Todo> results = todoRepository.findByTag(tag);
		
		assertThat(results).isNotNull();
		assertThat(results).hasSize(2);
		assertThat(results).contains(te1);
		assertThat(results).contains(te2);
		
	}
	
	@Test
	public void testFindByTagNoResults() {
		Tag tag = new Tag("0", "bar");
		Todo te1 = new Todo("tagged: bar");
		Todo te2 = new Todo("not tagged");
		te1.addTag(tag);
		
		todoRepository.addTodoElement(te1);
		todoRepository.addTodoElement(te2);
		
		List<Todo> results = todoRepository.findByTag(new Tag("1", "Foo"));
	
		assertThat(results).isNotNull();
		assertThat(results).hasSize(0);
	}
	
	@Test
	public void testFindByNullTag() {
		Tag tag = null;
		todoRepository.addTodoElement(new Todo("Foo"));
		
		List<Todo> results = todoRepository.findByTag(tag);
		
		assertThat(results).hasSize(0);
	}
	
	@Test
	public void testFindByIDWith() {
		Todo te = new Todo("This is not in repository");
		Todo recoveredTodo = todoRepository.findById(te);
		assertThat(recoveredTodo).isNull();
	}
	
	@Test
	public void testFindByIdNUll() {
		Todo te = null;
		Todo recoveredTodo = todoRepository.findById(te);
		assertThat(recoveredTodo).isNull();
	}
	
	@Test
	public void testFindByIdNoResults() {
		todoRepository.addTodoElement(new Todo("0", "Foo"));
		todoRepository.addTodoElement(new Todo("1", "Baz"));
		
		Todo recoveredTodo = todoRepository.findById(new Todo("2"));
		
		assertThat(recoveredTodo).isNull();
	}
	
	@Test
	public void testFindByIdIfTodoCollectionIsEmpty() {
		Todo te = new Todo("Foo");
		Todo recoveredTodo = todoRepository.findById(te);
		assertThat(recoveredTodo).isNull();
	}
	
	@Test
	public void testClear() {
		todoRepository.addTodoElement(new Todo("Foo"));
		todoRepository.addTodoElement(new Todo("Bar"));
		
		todoRepository.clear();
		
		assertThat(todoRepository.findAll()).hasSize(0);
	}
	
	@Test
	public void testComputeNewId() {
		String IDNo1 = todoRepository.computeNewId();
		String IDNo2 = todoRepository.computeNewId();
		
		assertThat(IDNo1).isEqualTo("1");
		assertThat(IDNo2).isEqualTo("2");
	}
}
