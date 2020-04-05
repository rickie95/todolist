package com.riccardomalavolti.apps.todolist;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.riccardomalavolti.apps.todolist.controller.TodoController;
import com.riccardomalavolti.apps.todolist.controller.TodoManager;
import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.model.Todo;
import com.riccardomalavolti.apps.todolist.view.TodoView;



public class TodoControllerTest {
	
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
	}
	
	@Test
	public void testUpdateTag() {
		Tag tag = new Tag("Foo");
		
		todoController.updateTag(tag);
		
		verify(todoManager).updateTag(tag);
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
	}

	@Test
	public void testFindTodoByText() {
		String searchText = "foo";
		List<Todo> todoList = new ArrayList<Todo>();
		when(todoManager.findTodoByText(searchText)).thenReturn(todoList);

		todoController.findTodoByText(searchText);

		verify(todoManager).findTodoByText(searchText);
		//verify(todoView).showAllTodo(todoList);
	}

	@Test
	public void testFindTagByText() {
		String searchText = "foo";
		List<Tag> tagList = new ArrayList<Tag>();
		when(todoManager.findTagByText(searchText)).thenReturn(tagList);

		todoController.findTagByText(searchText);
		
		verify(todoManager).findTagByText(searchText);
	}
	
	@Test
	public void testFindTodoByTag() {
		Tag tag = new Tag("Foo");
		List<Todo> todoList = new ArrayList<Todo>();
		
		when(todoManager.findTodoByTag(tag)).thenReturn(todoList);
		todoController.findTodoByTag(tag);
		
		verify(todoManager).findTodoByTag(tag);
	}
	
	@Test
	public void testTagTodoWithAListOfTags() {
		Tag t1 = new Tag("0", "tag 1");
		Tag t2 = new Tag("1", "tag 2");
		Todo todo = new Todo("0", "Foo");
		List<Tag> tagList = new ArrayList<Tag>(Arrays.asList(t1, t2));
		
		todoController.tagTodo(todo, tagList);
		
		verify(todoManager).tagTodo(todo, t1);
		verify(todoManager).tagTodo(todo, t2);
	}
	
	@Test
	public void testNewTodoButtonPressedShouldShowANewDialog() {
		DefaultComboBoxModel<Tag> tagListModel = new DefaultComboBoxModel<Tag>();
		
		todoController.newTodoDialog(tagListModel);
		
		JDialog todoDialog = todoController.getNewTodoDialog();
		assertThat(todoDialog).isNotNull();
		assertThat(todoDialog.isVisible()).isTrue();
		
		// Repeat again, the dialog should be the same.
		todoController.newTodoDialog(tagListModel);
		assertThat(todoController.getNewTodoDialog()).isEqualTo(todoDialog);
	}
	
	@Test
	public void testNewTagButtonPressedShouldShowANewAdUniqueDialog() {
		todoController.newTagDialog();
		
		JDialog tagDialog = todoController.getNewTagDialog();
		assertThat(tagDialog).isNotNull();
		assertThat(tagDialog.isVisible()).isTrue();
		
		todoController.newTagDialog();
		assertThat(todoController.getNewTagDialog()).isEqualTo(tagDialog);
	}
	
	@Test
	public void testDisposeNewTodoDialog() {
		DefaultComboBoxModel<Tag> tagListModel = new DefaultComboBoxModel<Tag>();
		
		todoController.newTodoDialog(tagListModel);
		
		todoController.dispose(todoController.getNewTodoDialog());
		
		// The dialog should be not visible at least 
		assertThat(todoController.getNewTodoDialog().isVisible()).isFalse();
	}

}
