package com.riccardomalavolti.apps.todolist.repositories.todo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.model.Todo;
import com.riccardomalavolti.apps.todolist.repositories.tag.TagRepository;

public class TodoRepositoryMongoDB implements TodoRepository {

	public static final String SERVER_ADDRESS = "localhost";
	public static final String DB_NAME = "TodoListDB";
	public static final String COLLECTION_NAME = "TodoCollection";
	
	private int idCounter = -1;

	private MongoDatabase todolistDatabase;
	private MongoCollection<Document> todoCollection;
	private TagRepository tagRepository;

	public TodoRepositoryMongoDB(MongoClient mongoClient, TagRepository tagRepo) {
		todolistDatabase = mongoClient.getDatabase(DB_NAME);
		todoCollection = todolistDatabase.getCollection(COLLECTION_NAME);
		tagRepository = tagRepo;
	}

	private Todo fromDocumentToTodo(Document doc) {
		if (doc == null)
			return null;

		Todo todo = new Todo(doc.getString("id"), doc.getString("body"));
		todo.setTagSet(fromTagIdListToTagSet((List<String>) doc.get("tags")));
		return todo;
	}

	private Document fromTodoToDocument(Todo todo) {
		Document d = new Document();
		d.append("id", todo.getId());
		d.append("body", todo.getBody());
		d.append("tags", fromTagSetToStringList(todo.getTagList()));
		return d;
	}

	private List<String> fromTagSetToStringList(Set<Tag> tagSet) {
		List<String> tagList = new ArrayList<>();
		tagSet.forEach(t -> tagList.add(t.getId()));
		return tagList;
	}

	private Set<Tag> fromTagIdListToTagSet(List<String> tagIdList) {
		Set<Tag> tagSet = new HashSet<>();
		tagIdList.forEach(id -> tagSet.add(tagRepository.findById(id)));
		return tagSet;
	}

	@Override
	public List<Todo> findAll() {
		return StreamSupport.stream(todoCollection.find().spliterator(), false).map(this::fromDocumentToTodo)
				.collect(Collectors.toList());
	}

	@Override
	public List<Todo> findByTag(Tag t) {
		return StreamSupport.stream(todoCollection.find(Filters.all("tags", t.getId())).spliterator(), false)
				.map(this::fromDocumentToTodo).collect(Collectors.toList());
	}

	@Override
	public List<Todo> findByBody(String text) {
		List<Todo> todoList = new ArrayList<>();

		List<Todo> collection = StreamSupport.stream(todoCollection.find().spliterator(), false)
				.map(this::fromDocumentToTodo).collect(Collectors.toList());

		for (Todo t : collection)
			if (t.getBody().toLowerCase().contains(text.toLowerCase()))
				todoList.add(t);

		return todoList;
	}

	@Override
	public Todo findById(Todo todo) {
		Document doc = todoCollection.find(Filters.eq("id", todo.getId())).first();
		return fromDocumentToTodo(doc);
	}

	@Override
	public void addTodoElement(Todo todo) {
		if(todo.getId() == null)
			todo.setId(computeNewId());
		
		if (todoCollection.find(Filters.eq("id", todo.getId())).first() == null)
			todoCollection.insertOne(fromTodoToDocument(todo));
	}

	@Override
	public void updateTodoElement(Todo todo) {
		if (todoCollection.find(Filters.eq("id", todo.getId())).first() != null) {
			removeTodoElement(todo);
			addTodoElement(todo);
		}
	}

	@Override
	public void removeTodoElement(Todo todo) {
		todoCollection.deleteOne(Filters.eq("id", todo.getId()));
	}

	@Override
	public void clear() {
		todoCollection.drop();
	}

	@Override
	public String computeNewId() {
		if(idCounter == -1) {
			List<String> ids = StreamSupport.stream(todoCollection.find().spliterator(), false)
					.map(doc -> (String) doc.get("id")).collect(Collectors.toCollection(ArrayList::new));

			Collections.sort(ids);
			idCounter = 0;

			if (!ids.isEmpty())
				idCounter = Integer.parseInt(ids.get(ids.size() - 1));
		}
		idCounter += 1;
		return Integer.toString(idCounter);
	}

}
