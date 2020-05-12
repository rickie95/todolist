package com.riccardomalavolti.apps.todolist.controller;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.riccardomalavolti.apps.todolist.manager.TodoManager;
import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.model.Todo;
import com.riccardomalavolti.apps.todolist.repositories.tag.TagRepository;
import com.riccardomalavolti.apps.todolist.repositories.tag.TagRepositoryMongoDB;
import com.riccardomalavolti.apps.todolist.repositories.todo.TodoRepository;
import com.riccardomalavolti.apps.todolist.repositories.todo.TodoRepositoryMongoDB;
import com.riccardomalavolti.apps.todolist.view.TodoView;

public class ControllerRepositoryMongoIT {
		
	private MongoClient client;
	private TodoManager todoManager;
	private TodoController todoController;
	
	private TodoRepository todoRepo;
	private MongoDatabase todolistDatabase;
	private MongoCollection<Document> todoCollection;


	private TagRepository tagRepo;
	private MongoDatabase tagDatabase;
	private MongoCollection<Document> tagCollection;
	
	@Mock
	private TodoView todoView;
	
	@Before	
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		client = new MongoClient(
				new ServerAddress("localhost",27017));
		
		tagRepo = new TagRepositoryMongoDB(client);
		todoRepo = new TodoRepositoryMongoDB(client, tagRepo);
		todoManager = new TodoManager(todoRepo, tagRepo);
		todoController = new TodoController(todoView, todoManager);
		
		todolistDatabase = client.getDatabase(TodoRepositoryMongoDB.DB_NAME);
		todolistDatabase.drop();
		todoCollection = todolistDatabase.getCollection(TodoRepositoryMongoDB.COLLECTION_NAME);
		
		tagDatabase = client.getDatabase(TagRepositoryMongoDB.DB_NAME);
		tagDatabase.drop();
		tagCollection = tagDatabase.getCollection(TagRepositoryMongoDB.COLLECTION_NAME);
	}

	@After
	public void tearDown() throws Exception {
		client.close();
	}

	@Test
	public void testAddTodo() {
		Todo todo = new Todo("3", "Foo");

		todoController.addTodo(todo);

		assertThat(getAllTodosFromDB()).contains(todo);
	}
	
	@Test
	public void testAddTag() {
		Tag tag = new Tag("1", "Foo");

		todoController.addTag(tag);

		assertThat(getAllTagsFromDB()).contains(tag);
	}
	
	@Test
	public void testUpdateTodo() {
		Todo todo = new Todo("3", "Foo");

		todoController.addTodo(todo);

		assertThat(todo.getBody()).isEqualTo("Foo");
		assertThat(getAllTodosFromDB()).contains(todo);
		
		todo.setBody("Bar");
		
		todoController.updateTodo(todo);
		
		assertThat(todo.getBody()).isEqualTo("Bar");
		assertThat(getAllTodosFromDB()).contains(todo);
	}
	
	@Test
	public void testUpdateTag() {
		Tag tag = new Tag("3", "Foo");

		todoController.addTag(tag);

		assertThat(getAllTagsFromDB()).contains(tag);
		
		tag.setBody("Bar");
		
		todoController.updateTag(tag);
		
		assertThat(getAllTagsFromDB()).contains(tag);
	}
	
	@Test
	public void testRemoveTodo() {
		Todo todo = new Todo("2", "Foo");
		todoController.addTodo(todo);
		
		assertThat(getAllTodosFromDB()).contains(todo);
		
		todoController.removeTodo(todo);
		
		assertThat(getAllTodosFromDB()).isEmpty();
	}
	
	@Test
	public void testRemoveTag() {
		Tag tag = new Tag("0", "Foo");
		todoController.addTag(tag);
		
		assertThat(getAllTagsFromDB()).contains(tag);
		
		todoController.removeTag(tag);
		
		assertThat(getAllTagsFromDB()).isEmpty();
	}
	
	@Test
	public void testTagATodo() {
		Todo todo = new Todo("Foo");
		Tag tag = new Tag("Bar");
		todoController.addTodo(todo);
		todoController.addTag(tag);
		
		todoController.tagTodo(todo, tag);
		
		Todo retrivedTodo = getAllTodosFromDB().get(0);
		assertThat(retrivedTodo.getTagList()).contains(tag);
	}
	
	private List<Tag> getAllTagsFromDB(){
		return StreamSupport
				.stream(tagCollection.find().spliterator(), false)
				.map(doc -> new Tag(doc.getString("id"), doc.getString("text")))
				.collect(Collectors.toList());
	}
	
	private List<Todo> getAllTodosFromDB(){
		return StreamSupport
				.stream(todoCollection.find().spliterator(), false)
				.map(doc -> new Todo(doc.getString("id"), doc.getString("text"), getSelectedTag(doc.getList("tags", String.class))))
				.collect(Collectors.toList());
	}
	
	private List<Tag> getSelectedTag(List<String> tagList){
		return StreamSupport
				.stream(tagCollection.find().spliterator(), false)
				.map(doc -> new Tag(doc.getString("id"), doc.getString("text")))
				.filter(tag -> tagList.contains(tag.getId()))
				.collect(Collectors.toList());
	}

}
