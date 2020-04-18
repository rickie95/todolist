package com.riccardomalavolti.apps.todolist;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.repositories.tag.TagRepository;
import com.riccardomalavolti.apps.todolist.repositories.tag.TagRepositoryInMemory;

public class TagRepositoryInMemoryTest {
	
	TagRepository tagRepository;
	
	@Before
	public void setup() {
		tagRepository = new TagRepositoryInMemory();
	}
	
	@After
	public void clearUp() {
		tagRepository.clear();
	}
	
	@Test
	public void testFindAll() {
		Tag tagOne = new Tag("0", "Foo");
		Tag tagTwo = new Tag("1", "Bar");
		tagRepository.addTag(tagOne);
		tagRepository.addTag(tagTwo);
		
		Set<Tag> results = tagRepository.findAll();
		
		assertThat(results).isNotNull();
		assertThat(results.size()).isEqualTo(2);
		assertThat(results).contains(tagOne);
		assertThat(results).contains(tagTwo);
	}
	
	@Test
	public void testFindAllIfEmpty() {
		Set<Tag> results = tagRepository.findAll();
		
		assertThat(results).isNotNull();
		assertThat(results.size()).isEqualTo(0);
	}
	
	@Test
	public void testFindByText() {
		Tag tagOne = new Tag("0", "Foo");
		Tag tagTwo = new Tag("1", "Bar");
		Tag tagThree = new Tag("2", "Cooc-coo");
		tagRepository.addTag(tagOne);
		tagRepository.addTag(tagTwo);
		tagRepository.addTag(tagThree);
		String textToSearch = "oo";
		
		Set<Tag> results = tagRepository.findByText(textToSearch);
		
		assertThat(results).isNotNull();
		assertThat(results.size()).isEqualTo(2);
		assertThat(results).contains(tagOne);
		assertThat(results).contains(tagThree);
	}
	
	@Test
	public void testFindByTextNoResults() {
		Tag tagOne = new Tag("0", "Foo");
		Tag tagTwo = new Tag("1", "Bar");
		Tag tagThree = new Tag("2", "Cooc-coo");
		tagRepository.addTag(tagOne);
		tagRepository.addTag(tagTwo);
		tagRepository.addTag(tagThree);
		String textToSearch = "bonobo";
		
		Set<Tag> results = tagRepository.findByText(textToSearch);
		
		assertThat(results).isNotNull();
		assertThat(results.size()).isEqualTo(0);
	}
	
	@Test
	public void testFindByNullText() {
		Tag tagOne = new Tag("0", "Foo");
		Tag tagTwo = new Tag("1", "Bar");
		Tag tagThree = new Tag("2", "Cooc-coo");
		tagRepository.addTag(tagOne);
		tagRepository.addTag(tagTwo);
		tagRepository.addTag(tagThree);
		String textToSearch = null;
		
		Set<Tag> results = tagRepository.findByText(textToSearch);
		
		assertThat(results).isNotNull();
		assertThat(results.size()).isEqualTo(0);
	}
	
	@Test
	public void testFindById() {
		Tag tagOne = new Tag("0", "Foo");
		Tag tagTwo = new Tag("1", "Bar");

		tagRepository.addTag(tagOne);
		tagRepository.addTag(tagTwo);
		
		Tag tag = tagRepository.findById("1");
		
		assertThat(tag).isNotNull();
		assertThat(tagTwo).isEqualTo(tag);
	}
	
	@Test
	public void testFindByIdWhenItDoesntExists() {
		Tag tagOne = new Tag("0", "Foo");

		tagRepository.addTag(tagOne);
		
		Tag tag = tagRepository.findById("1");
		
		assertThat(tag).isNull();
	}
	
	@Test
	public void testFindByIdWhenIDIsNull() {
		Tag tagOne = new Tag("0", "Foo");

		tagRepository.addTag(tagOne);
		
		Tag tag = tagRepository.findById(null);
		
		assertThat(tag).isNull();
	}
	
	@Test
	public void testUpdateTag() {
		Tag tag = new Tag("0","Foo");
		tagRepository.addTag(tag);
		tag.setBody("Bar");
		
		tagRepository.updateTag(tag);
		Tag res = (Tag) (tagRepository.findAll().toArray())[0];
		
		assertThat(res.getText()).isEqualTo("Bar");
	}
	
	@Test 
	public void testUpdateTagIfNotPresent() {
		Tag tag = new Tag("0", "Foo");
		
		tagRepository.updateTag(tag);
		
		Set<Tag> results = tagRepository.findAll();
		assertThat(results.size()).isEqualTo(0);
	}
	
	@Test
	public void testRemoveTag() {
		Tag tag_to_be_removed = new Tag("0", "Foo");
		Tag another_tag = new Tag("1", "Bar");
		tagRepository.addTag(tag_to_be_removed);
		tagRepository.addTag(another_tag);
		assertThat(tagRepository.findAll().size()).isEqualTo(2);
		
		tagRepository.removeTag(tag_to_be_removed);
		assertThat(tagRepository.findAll().size()).isEqualTo(1);
		Tag recoveredTag = tagRepository.findById("1");
		assertThat(recoveredTag).isEqualTo(another_tag);
	}
	
	@Test
	public void testClear() {
		tagRepository.addTag(new Tag("0","Foo"));
		
		assertThat(tagRepository.findAll().size()).isEqualTo(1);
		tagRepository.clear();
		assertThat(tagRepository.findAll().size()).isEqualTo(0);
		
	}
	
	@Test
	public void testComputeId() {
	
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
