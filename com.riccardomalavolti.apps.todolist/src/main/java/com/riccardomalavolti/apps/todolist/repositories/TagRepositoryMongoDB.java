package com.riccardomalavolti.apps.todolist.repositories;

import java.util.Set;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import com.riccardomalavolti.apps.todolist.Tag;

public class TagRepositoryMongoDB implements TagRepository {
	
	private static TagRepositoryMongoDB instance = null;
	
	private MongoClient dbClient;
	private MongoDatabase todolistDatabase;
	private MongoCollection tagCollection;
	
	
	private TagRepositoryMongoDB() {
		dbClient = new MongoClient("localhost");
		todolistDatabase = dbClient.getDatabase("TodoListDB");
		tagCollection = todolistDatabase.getCollection("TagCollection");
	}
	
	public static TagRepositoryMongoDB getInstance() {
		if(instance == null)
			instance = new TagRepositoryMongoDB();
		
		return instance;
	}

	@Override
	public Set<Tag> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Tag> findByText(String text) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addTag(Tag tag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTag(Tag tag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeTag(Tag tag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

}
