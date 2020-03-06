package com.riccardomalavolti.apps.todolist;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.riccardomalavolti.apps.todolist.controller.TodoController;
import com.riccardomalavolti.apps.todolist.controller.TodoManager;
import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.model.Todo;
import com.riccardomalavolti.apps.todolist.view.TodoView;

public class TodoControllerTest {
	
	@Mock
	TodoView todoView;
	TodoManager todoManager;

	TodoController todoController;

	@Before
	public void setup() {
		todoView = mock(TodoView.class);
		todoManager = mock(TodoManager.class);

		todoController = new TodoController(todoView, todoManager);
	}

	@Test
	public void testShowTodos() {
		List<Todo> todos = new ArrayList<>();
		when(todoManager.getTodoList()).thenReturn(todos);

		todoController.showTodos();

		verify(todoView).showAllTodo(todos);
	}

	@Test
	public void testShowTags() {
		List<Tag> tags = new ArrayList<>();
		when(todoManager.getTagList()).thenReturn(tags);

		todoController.showTags();

		verify(todoView).showAllTags(tags);
	}

	@Test
	public void testAddTodo() {
		Todo todo = new Todo("Foo");

		todoController.addTodo(todo);

		verify(todoManager).addTodo(todo);
		verify(todoView).addTodo(todo);
	}
	
	@Test
	public void testAddTodoWhenAnExIsThrow() {
		String exceptionMessage = "Exception error";
		String errorMessage = "A problem as occurred "
				+ "while insering a new todo: java.lang.RuntimeException: " 
				+ exceptionMessage;

		Todo todo = new Todo("Foo");

		doThrow(new RuntimeException(exceptionMessage)).when(todoManager).addTodo(todo);
		todoController.addTodo(todo);
	
		verify(todoView).error(errorMessage);
	}

	@Test
	public void testAddTag() {
		Tag tag = new Tag("Foo");

		todoController.addTag(tag);

		verify(todoManager).addTag(tag);
		verify(todoView).addTag(tag);
	}
	
	@Test
	public void testAddTagWhenAnExIsThrow() {
		String exceptionMessage = "Exception error";
		String errorMessage = "A problem as occurred "
				+ "while insering a new tag: java.lang.RuntimeException: " 
				+ exceptionMessage;
		
		Tag tag = new Tag("Foo");

		doThrow(new RuntimeException(exceptionMessage)).when(todoManager).addTag(tag);
		todoController.addTag(tag);
	
		verify(todoView).error(errorMessage);
	}
	
	@Test
	public void testUpdateTodo() {
		Todo todo = new Todo("Foo");
		
		todoController.updateTodo(todo);
		
		verify(todoManager).updateTodo(todo);
		verify(todoView).updateTodo(todo);
	}
	
	@Test
	public void testUpdateTag() {
		Tag tag = new Tag("Foo");
		
		todoController.updateTag(tag);
		
		verify(todoManager).updateTag(tag);
		verify(todoView).updateTag(tag);
	}
	
	@Test
	public void testRemoveTodo() {
		Todo todo = new Todo();
		
		todoController.removeTodo(todo);
		
		verify(todoManager).removeTodo(todo);
		verify(todoView).removeTodo(todo);
	}
	
	@Test
	public void testRemoveTag() {
		Tag tag = new Tag("Foo");
		
		todoController.removeTag(tag);
		
		verify(todoManager).removeTag(tag);
		verify(todoView).removeTag(tag);
	}
	
	@Test
	public void testTagATodo() {
		Todo todo = new Todo("Foo");
		Tag tag = new Tag("Bar");
		
		todoController.tagTodo(todo, tag);
		
		verify(todoManager).tagTodo(todo, tag);
		verify(todoView).updateTodo(todo);
		
	}

	@Test
	public void testFindTodoByText() {
		String searchText = "foo";
		List<Todo> todoList = new ArrayList<Todo>();
		when(todoManager.findTodoByText(searchText)).thenReturn(todoList);

		todoController.findTodoByText(searchText);

		verify(todoManager).findTodoByText(searchText);
		verify(todoView).showAllTodo(todoList);
	}

	@Test
	public void testFindTagByText() {
		String searchText = "foo";
		List<Tag> tagList = new ArrayList<Tag>();
		when(todoManager.findTagByText(searchText)).thenReturn(tagList);

		todoController.findTagByText(searchText);
		
		verify(todoManager).findTagByText(searchText);
		verify(todoView).showAllTags(tagList);
	}
	
	@Test
	public void testFindTodoByTag() {
		Tag tag = new Tag("Foo");
		List<Todo> todoList = new ArrayList<Todo>();
		
		when(todoManager.findTodoByTag(tag)).thenReturn(todoList);
		todoController.findTodoByTag(tag);
		
		verify(todoManager).findTodoByTag(tag);
		verify(todoView).showAllTodo(todoList);
	}

}
