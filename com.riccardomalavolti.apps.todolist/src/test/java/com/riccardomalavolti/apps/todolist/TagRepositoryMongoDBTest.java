package com.riccardomalavolti.apps.todolist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.InetSocketAddress;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

import com.riccardomalavolti.apps.todolist.repositories.*;

public class TagRepositoryMongoDBTest {
	
	private static MongoServer mongoServer;
	private static InetSocketAddress serverAddress;
	
	private MongoClient client;
	private TagRepository tagRepository;
	// MongoDB in memory is used for testing.
	
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
		MongoDatabase db = client.getDatabase(TagRepositoryMongoDB.DB_NAME);
		db.drop();
	}
	
	@After
	public void onExiting() {
		client.close();
	}
	
	@Test
	public void testFindAll() {
		tagRepository.addTag(new Tag("0", "Foo"));
		tagRepository.addTag(new Tag("1", "Bar"));
		
		Set<Tag> results = tagRepository.findAll();
		
		assertEquals(2, results.size());
	}
	
	@Test
	public void testFindAllWhenIsEmpty() {
		Set<Tag> results = tagRepository.findAll();
		
		assertEquals(0, results.size());
	}
	
	@Test
	public void testFindById() {
		tagRepository.addTag(new Tag("0", "Foo"));
		tagRepository.addTag(new Tag("1", "Bar"));
		
		Tag results = tagRepository.findById("1");
		
		assertNotNull(results);
		assertEquals("1", results.getId());
	}
	
	@Test
	public void testFindByIdIfNotExists() {
		tagRepository.addTag(new Tag("0", "Foo"));
		tagRepository.addTag(new Tag("1", "Bar"));
		
		Tag t = tagRepository.findById("3");
		
		assertNull(t);
	}
	
	@Test
	public void testAddTagIfAlreadyInserted() {
		tagRepository.addTag(new Tag("0", "Foo"));
		tagRepository.addTag(new Tag("0", "Bar"));
		
		Set<Tag> results = tagRepository.findAll();
		
		assertEquals(1, results.size());
	}
	
	@Test
	public void testFindByText() {
		tagRepository.addTag(new Tag("0", "Foo"));
		tagRepository.addTag(new Tag("1", "Bar"));
		tagRepository.addTag(new Tag("2", "Baz Foo"));
		
		Set<Tag> results = tagRepository.findByText("Foo");
		assertEquals(2, results.size());
	}
	
	@Test
	public void testUpdateTag() {
		tagRepository.addTag(new Tag("0", "Foo"));
		
		tagRepository.updateTag(new Tag("0", "Bar"));
		
		Tag t = tagRepository.findById("0");
		
		assertNotNull(t);
		assertEquals("Bar", t.getText());
	}
	
	@Test
	public void testClear() {
		tagRepository.addTag(new Tag("0", "Foo"));
		tagRepository.addTag(new Tag("1", "Bar"));
		
		tagRepository.clear();
		Set<Tag> collection = tagRepository.findAll();
		
		assertEquals(0, collection.size());	
		
	}
	
}
