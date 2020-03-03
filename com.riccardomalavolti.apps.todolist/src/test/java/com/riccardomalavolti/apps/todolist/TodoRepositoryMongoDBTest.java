package com.riccardomalavolti.apps.todolist;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.model.Todo;
import com.riccardomalavolti.apps.todolist.repositories.tag.TagRepository;
import com.riccardomalavolti.apps.todolist.repositories.tag.TagRepositoryMongoDB;
import com.riccardomalavolti.apps.todolist.repositories.todo.TodoRepository;
import com.riccardomalavolti.apps.todolist.repositories.todo.TodoRepositoryMongoDB;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

public class TodoRepositoryMongoDBTest {
	
	private static MongoServer mongoServer;
	private static InetSocketAddress serverAddress;
	
	private MongoClient client;
	private TodoRepository todoRepository;
	private TagRepository tagRepository;

	@BeforeClass
	public static void setupServer() {
		mongoServer = new MongoServer(new MemoryBackend());
		serverAddress = mongoServer.bind();
	}
	
	@AfterClass
	public static void stopServer() {
		mongoServer.shutdown();
	}
	
	@Before
	public void setup() {
		client = new MongoClient(new ServerAddress(serverAddress));

		tagRepository = new TagRepositoryMongoDB(client);
		todoRepository = new TodoRepositoryMongoDB(client, tagRepository);
		MongoDatabase db = client.getDatabase(TodoRepositoryMongoDB.DB_NAME);
		db.drop();
	}
	
	@After
	public void onExiting() {
		client.close();
	}
	
	@Test
	public void testFindAll() {
		todoRepository.addTodoElement(new Todo("0", "Foo"));
		todoRepository.addTodoElement(new Todo("1", "Bar"));
		
		List<Todo> results = todoRepository.findAll();
		
		assertThat(results).hasSize(2);
	}
	
	@Test
	public void testFindAllWhenIsEmpty() {
		List<Todo> results = todoRepository.findAll();
		
		assertThat(results).hasSize(0);
	}
	
	@Test
	public void testAddTodo() {
		Todo todo = new Todo("0", "Foo");
		Tag tag = new Tag("0", "Bar");
		todo.addTag(tag);
		tagRepository.addTag(tag);
		todoRepository.addTodoElement(todo);
		
		Todo recoveredTodo = todoRepository.findById(todo);
		
		assertThat(recoveredTodo).isEqualTo(todo);
		Set<Tag> tagList = recoveredTodo.getTagList();
		assertThat(tagList).contains(tag);
	}
	
	@Test
	public void testAddTodoAlreadyInserted() {
		todoRepository.addTodoElement(new Todo("0", "Foo"));
		todoRepository.addTodoElement(new Todo("0", "Bar"));
		List<Todo> results = todoRepository.findAll();
		
		assertThat(results).hasSize(1);
	}
	
	@Test
	public void testFindByBody() {
		todoRepository.addTodoElement(new Todo("0", "Foo"));
		todoRepository.addTodoElement(new Todo("1", "Bar"));
		todoRepository.addTodoElement(new Todo("2", "Bar"));
		String textToBeSearched = "Bar";
		
		List<Todo> results = todoRepository.findByBody(textToBeSearched);
		
		assertThat(results).hasSize(2);
	}
	
	@Test
	public void testFindById() {
		todoRepository.addTodoElement(new Todo("0", "Foo"));
		Todo todoToBeRecovedered = new Todo("1", "Bar");
		todoRepository.addTodoElement(todoToBeRecovedered);
		todoRepository.addTodoElement(new Todo("2", "Bar"));
		
		Todo recoveredTodo = todoRepository.findById(new Todo("1", "Empty"));
		
		assertThat(recoveredTodo).isNotNull();
		assertThat(recoveredTodo).isEqualTo(todoToBeRecovedered);
	}
	
	@Test
	public void testFindByTag() {
		Todo foo = new Todo("1", "Foo");
		Todo bar = new Todo("2", "Bar");
		Todo jee = new Todo("3", "Jee");
		
		Tag tagHome = new Tag("1", "Home");
		Tag tagShop = new Tag("2", "Shop");
		Tag tagCar = new Tag("3", "Car");
		
		
		tagRepository.addTag(tagHome);
		tagRepository.addTag(tagShop);
		tagRepository.addTag(tagCar);
		
		foo.addTag(tagHome);
		foo.addTag(tagShop);
		
		bar.addTag(tagHome);
		bar.addTag(tagCar);
		
		jee.addTag(tagShop);
		jee.addTag(tagCar);
		
		todoRepository.addTodoElement(foo);
		todoRepository.addTodoElement(bar);
		todoRepository.addTodoElement(jee);
		
		// Foo (Home, Shop)
		// Bar (Home, Car)
		// Jee (Shop, Car)
		
		List<Todo> results = todoRepository.findByTag(tagShop);
		
		assertThat(results).hasSize(2);
		assertThat(results).contains(foo);
		assertThat(results).contains(jee);
		
	}
	
	@Test
	public void testFindByTagWhenATodoHasNotThatTag() {
		Todo todo = new Todo("1", "Foo");		
		Tag tag = new Tag("1", "Home");
		todoRepository.addTodoElement(todo);
		
		List<Todo> results = todoRepository.findByTag(tag);
		
		assertThat(results).hasSize(0);
	}
	
	@Test
	public void testClear() {
		todoRepository.addTodoElement(new Todo("0", "Foo"));
		todoRepository.addTodoElement(new Todo("1", "Bar"));
		todoRepository.addTodoElement(new Todo("2", "Tez"));
		
		assertThat(todoRepository.findAll()).hasSize(3);
		todoRepository.clear();
		assertThat(todoRepository.findAll()).hasSize(0);
	}
	
	@Test
	public void testRemoveTodo() {
		todoRepository.addTodoElement(new Todo("0", "Foo"));
		Todo todoToBeRemoved = new Todo("1", "Bar");
		todoRepository.addTodoElement(todoToBeRemoved);
		todoRepository.addTodoElement(new Todo("2", "Tez"));
		
		assertThat(todoRepository.findAll()).hasSize(3);
		todoRepository.removeTodoElement(todoToBeRemoved);
		assertThat(todoRepository.findAll()).hasSize(2);
		assertThat(todoRepository.findById(todoToBeRemoved)).isNull();
	}
	
	@Test
	public void testUpdateTodo() {
		Todo todo = new Todo("0", "Foo");
		todoRepository.addTodoElement(todo);
		
		todo.setBody("Bar");
		todoRepository.updateTodoElement(todo);
		Todo recoveredTodo = todoRepository.findById(new Todo("0", "null"));
		
		assertThat(recoveredTodo.getBody()).isEqualTo("Bar");
		
	}
}
