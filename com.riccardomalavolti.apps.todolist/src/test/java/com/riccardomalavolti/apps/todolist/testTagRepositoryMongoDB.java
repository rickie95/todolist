package com.riccardomalavolti.apps.todolist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.riccardomalavolti.apps.todolist.repositories.*;

public class testTagRepositoryMongoDB {
	
	TagRepository tagRepository;
	
	@Before
	public void setup() {
		tagRepository = TagRepositoryMongoDB.getInstance();
		/*
		Document tagDocument = new Document("id", "001").append("text", "Home");
		tagCollection.insertOne(tagDocument);
		
		Document dd = tagCollection.find().first();
		System.out.print(dd.get("id"));
		
		tagCollection.drop();
		client.close();
		*/
	}
	
	@Test
	public void testClear() {
		tagRepository.addTag(new Tag("Foo"));
		
		tagRepository.clear();
		
		assertEquals(0, tagRepository.findAll().size());
	}
	
	@After
	public void OnExiting() {
		tagRepository.clear();
	}

}
