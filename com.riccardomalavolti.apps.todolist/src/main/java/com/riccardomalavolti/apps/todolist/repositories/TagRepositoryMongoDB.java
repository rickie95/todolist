package com.riccardomalavolti.apps.todolist.repositories;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.TextSearchOptions;
import com.riccardomalavolti.apps.todolist.Tag;

public class TagRepositoryMongoDB implements TagRepository {
	
	public static final String SERVER_ADDRESS = "localhost";
	public static final String DB_NAME = "TodoListDB";
	public static final String COLLECTION_NAME = "TagCollection";
	public static final TextSearchOptions NO_CASE_SEARCH = new TextSearchOptions().caseSensitive(false);
	
	private MongoDatabase todolistDatabase;
	private MongoCollection<Document> tagCollection;
	
	public TagRepositoryMongoDB(MongoClient mongoClient) {
		todolistDatabase = mongoClient.getDatabase(DB_NAME);
		tagCollection = todolistDatabase.getCollection(COLLECTION_NAME);
	}
	
	private Document fromTagToDocument(Tag tag) {
		return new Document().append("id", tag.getId()).append("text", tag.getText());
	}
	
	private Tag fromDocumentToTag(Document doc) {
		if(doc != null)
			return new Tag(doc.get("id").toString(), doc.get("text").toString());
		return null;
	}
	
	@Override
	public Set<Tag> findAll() {
		return StreamSupport.stream(tagCollection.find().spliterator(), false)
				.map(this::fromDocumentToTag).collect(Collectors.toSet());
	}
	
	@Override
	public Set<Tag> findByText(String text) {
		Set<Tag> results = new HashSet<>();
		
		// MongoServer implementation doesn't support the $search operator
		
		Set<Tag> collection = StreamSupport.stream(tagCollection.find().spliterator(), false)
		.map(this::fromDocumentToTag).collect(Collectors.toSet());
		
		for(Tag t : collection) {
			if(t.getText().toLowerCase().contains(text.toLowerCase()))
				results.add(t);
		}
		
		return results;
	}
	
	@Override
	public Tag findById(String id) {
		return fromDocumentToTag(tagCollection.find(Filters.eq("id", id)).first());
	}

	@Override
	public void addTag(Tag tag) {
		if(tagCollection.find(Filters.eq("id", tag.getId())).first() == null)
			tagCollection.insertOne(fromTagToDocument(tag));
	}

	@Override
	public void updateTag(Tag tag) {
			removeTag(tag);
			addTag(tag);
	}

	@Override
	public void removeTag(Tag tag) {
		tagCollection.deleteOne(Filters.eq("id", tag.getId()));
	}

	@Override
	public void clear() {
		tagCollection.drop();
		
	}
}
