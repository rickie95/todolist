package com.riccardomalavolti.apps.todolist.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.repositories.tag.TagRepositoryMongoDB;

public class TagRepositoryMongoIT {
		
	private MongoClient client;
	private TagRepositoryMongoDB tagRepository;

	@Before
	public void setUp() throws Exception {
		client = new MongoClient(
				new ServerAddress("localhost",27017));
		MongoDatabase db = client.getDatabase(TagRepositoryMongoDB.DB_NAME);
		db.drop();
		
		tagRepository = new TagRepositoryMongoDB(client);
	}

	@After
	public void tearDown() throws Exception {
		client.close();
	}
	
	@Test
	public void testAddTag() {
		tagRepository.addTag(new Tag("0", "Foo"));
		tagRepository.addTag(new Tag("1", "Bar"));
		
		Set<Tag> results = tagRepository.findAll();
		
		assertThat(results).hasSize(2);
	}

	@Test
	public void testFindAll() {
		tagRepository.addTag(new Tag("0", "Foo"));
		tagRepository.addTag(new Tag("1", "Bar"));
		
		Set<Tag> results = tagRepository.findAll();
		
		assertThat(results).hasSize(2);
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

}
