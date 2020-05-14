package com.riccardomalavolti.apps.todolist.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.model.Todo;
import com.riccardomalavolti.apps.todolist.repositories.tag.TagRepository;
import com.riccardomalavolti.apps.todolist.repositories.todo.TodoRepository;

public class TodoManagerTest {

	private TodoManager todoManager;
	private TodoRepository todoRepository;
	private TagRepository tagRepository;

	@Before
	public void setup() {
		todoRepository = mock(TodoRepository.class);
		tagRepository = mock(TagRepository.class);
		todoManager = new TodoManager(todoRepository, tagRepository);
	}

	@Test
	public void testAddTagAtTodo() {
		Todo todo = mock(Todo.class);
		Tag tag = new Tag("0", "Bar");

		todoManager.tagTodo(todo, tag);
		ArgumentCaptor<Tag> tagCaptor = ArgumentCaptor.forClass(Tag.class);

		verify(todo).addTag(tagCaptor.capture());
		verify(todoRepository).updateTodoElement(todo);
		assertThat(tagCaptor.getValue()).isEqualTo(tag);
	}

	@Test
	public void testGetTodoList() {
		List<Todo> todos = new ArrayList<>();
		todos.add(new Todo());
		when(todoRepository.findAll()).thenReturn(todos);

		List<Todo> todoList = todoManager.getTodoList();

		assertThat(todos).isEqualTo(todoList);
		assertThat(todos).hasSize(1);
		assertThat(todoList).hasSize(1);
	}

	@Test
	public void testAddTodo() {
		Todo te = new Todo("Foo");

		todoManager.addTodo(te);
		ArgumentCaptor<Todo> todoCaptor = ArgumentCaptor.forClass(Todo.class);
		verify(todoRepository).addTodoElement(todoCaptor.capture());

		assertThat(todoCaptor.getValue()).isEqualTo(te);
	}

	@Test
	public void testRemoveTodo() {
		Todo todoToBeRemoved = new Todo("Foo");

		todoManager.removeTodo(todoToBeRemoved);
		ArgumentCaptor<Todo> todoCaptor = ArgumentCaptor.forClass(Todo.class);
		verify(todoRepository).removeTodoElement(todoCaptor.capture());

		assertThat(todoCaptor.getValue()).isEqualTo(todoToBeRemoved);

	}

	@Test
	public void testUpdateTodo() {
		Todo te = new Todo("Foo");

		todoManager.updateTodo(te);
		ArgumentCaptor<Todo> todoCaptor = ArgumentCaptor.forClass(Todo.class);

		verify(todoRepository).updateTodoElement(todoCaptor.capture());
		assertThat(todoCaptor.getValue()).isEqualTo(te);
	}

	@Test
	public void testGetEmptyTagList() {
		when(tagRepository.findAll()).thenReturn(new HashSet<Tag>());

		List<Tag> tagList = todoManager.getTagList();

		assertThat(tagList).isNotNull();
		assertThat(tagList).isEmpty();
	}

	@Test
	public void testGetTagList() {
		Set<Tag> tagRep = new HashSet<>();
		tagRep.add(new Tag("Foo"));
		when(tagRepository.findAll()).thenReturn(tagRep);

		List<Tag> tagList = todoManager.getTagList();

		assertThat(tagList).isNotNull();
		assertThat(tagList).hasSize(1);
	}

	@Test
	public void testAddTag() {
		Tag tag = new Tag("Foo");

		todoManager.addTag(tag);
		ArgumentCaptor<Tag> tagCaptor = ArgumentCaptor.forClass(Tag.class);

		verify(tagRepository).addTag(tagCaptor.capture());
		assertThat(tagCaptor.getValue()).isEqualTo(tag);
	}

	@Test
	public void testRemoveTag() {
		Tag tag = new Tag("Foo");

		todoManager.removeTag(tag);
		ArgumentCaptor<Tag> tagCaptor = ArgumentCaptor.forClass(Tag.class);

		verify(tagRepository).removeTag(tagCaptor.capture());
		assertThat(tagCaptor.getValue()).isEqualTo(tag);
	}

	@Test
	public void testUpdateTag() {
		Tag tag = new Tag("Foo");

		todoManager.updateTag(tag);
		ArgumentCaptor<Tag> tagCaptor = ArgumentCaptor.forClass(Tag.class);

		verify(tagRepository).updateTag(tagCaptor.capture());
		assertThat(tagCaptor.getValue()).isEqualTo(tag);
	}

	@Test
	public void testFindTodoByText() {
		String searchText = "Foo";
		Todo todo = new Todo("Baz Foo");
		List<Todo> resList = new ArrayList<>();
		resList.add(todo);
		doReturn(resList).when(todoRepository).findByBody(searchText);

		List<Todo> list = todoManager.findTodoByText(searchText);

		verify(todoRepository).findByBody(searchText);
		assertThat(list).hasSize(1);
		assertThat(list).contains(todo);
	}

	@Test
	public void testFindTagByText() {
		String searchText = "Foo";
		Tag tag = new Tag("Baz Foo");
		Set<Tag> resList = new HashSet<>();
		resList.add(tag);
		doReturn(resList).when(tagRepository).findByText(searchText);

		List<Tag> list = todoManager.findTagByText(searchText);

		verify(tagRepository).findByText(searchText);
		assertThat(list).hasSize(1);
		assertThat(list).contains(tag);
	}

	@Test
	public void testFindTodoByTag() {
		Tag tag = new Tag("Foo");
		Todo todo = new Todo("Bar");
		todo.addTag(tag);
		List<Todo> resList = new ArrayList<Todo>();
		resList.add(todo);
		doReturn(resList).when(todoRepository).findByTag(tag);

		List<Todo> results = todoManager.findTodoByTag(tag);

		verify(todoRepository).findByTag(tag);
		assertThat(results).hasSize(1);
		assertThat(results).contains(todo);
		assertThat(results.get(0).getTagList()).contains(tag);
	}
}
