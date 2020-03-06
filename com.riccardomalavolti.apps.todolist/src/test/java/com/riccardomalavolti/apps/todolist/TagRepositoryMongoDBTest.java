package com.riccardomalavolti.apps.todolist;

import static org.assertj.core.api.Assertions.*;

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

import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.repositories.tag.TagRepository;
import com.riccardomalavolti.apps.todolist.repositories.tag.TagRepositoryMongoDB;

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
		
		assertThat(results).hasSize(2);
	}
	
	@Test
	public void testFindAllWhenIsEmpty() {
		Set<Tag> results = tagRepository.findAll();
		
		assertThat(results).hasSize(0);
	}
	
	@Test
	public void testFindById() {
		tagRepository.addTag(new Tag("0", "Foo"));
		tagRepository.addTag(new Tag("1", "Bar"));
		
		Tag result = tagRepository.findById("1");
		
		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo("1");
	}
	
	@Test
	public void testFindByIdIfNotExists() {
		tagRepository.addTag(new Tag("0", "Foo"));
		tagRepository.addTag(new Tag("1", "Bar"));
		
		Tag t = tagRepository.findById("3");
		
		assertThat(t).isNull();;
	}
	
	@Test
	public void testAddTagIfAlreadyInserted() {
		tagRepository.addTag(new Tag("0", "Foo"));
		tagRepository.addTag(new Tag("0", "Bar"));
		
		Set<Tag> results = tagRepository.findAll();
		
		assertThat(results).hasSize(1);
	}
	
	@Test
	public void testAddTag() {
		tagRepository.addTag(new Tag("0", "Foo"));
		tagRepository.addTag(new Tag("1", "Bar"));
		
		Set<Tag> results = tagRepository.findAll();
		
		assertThat(results).hasSize(2);
	}
	
	@Test
	public void testFindByText() {
		tagRepository.addTag(new Tag("0", "Foo"));
		tagRepository.addTag(new Tag("1", "Bar"));
		tagRepository.addTag(new Tag("2", "Baz Foo"));
		
		Set<Tag> results = tagRepository.findByText("Foo");
		assertThat(results).hasSize(2);
	}
	
	@Test
	public void testUpdateTag() {
		tagRepository.addTag(new Tag("0", "Foo"));
		
		tagRepository.updateTag(new Tag("0", "Bar"));
		
		Tag t = tagRepository.findById("0");
		
		assertThat(t).isNotNull();
		assertThat(t.getText()).isEqualTo("Bar");
	}
	
	@Test
	public void testClear() {
		tagRepository.addTag(new Tag("0", "Foo"));
		tagRepository.addTag(new Tag("1", "Bar"));
		
		tagRepository.clear();
		Set<Tag> collection = tagRepository.findAll();
		
		assertThat(collection).hasSize(0);	
	}
	
	@Test
	public void testComputeNewId() {
		
		Tag tagOne = new Tag("Foo body");
		tagOne.setId(tagRepository.computeNewId());
		tagRepository.addTag(tagOne);
		
		Tag tagTwo = new Tag("Bar body");
		tagTwo.setId(tagRepository.computeNewId());
		tagRepository.addTag(tagTwo);
		
		assertThat(Integer.valueOf(tagOne.getId())).isGreaterThan(-1);
		assertThat(Integer.valueOf(tagOne.getId()))
			.isLessThan(Integer.valueOf(tagTwo.getId()));
	}
	
	@Test
	public void testComputeNewIdWithUnorderedInsertions() {
		tagRepository.addTag(new Tag("1", "Baz1"));
		tagRepository.addTag(new Tag("0", "Baz2"));
		
		Tag tagOne = new Tag("Foo body");
		tagOne.setId(tagRepository.computeNewId());
		tagRepository.addTag(tagOne);
		
		assertThat(Integer.valueOf(tagOne.getId())).isGreaterThan(1);
	}
	
}
