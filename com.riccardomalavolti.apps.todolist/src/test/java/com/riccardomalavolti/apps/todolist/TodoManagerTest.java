package com.riccardomalavolti.apps.todolist;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.riccardomalavolti.apps.todolist.repositories.TodoRepository;

public class TodoManagerTest {

	private TodoManager todoManager;
	private TodoRepository todoRepository;
	
	@Before
	public void setup() {
		todoRepository = mock(TodoRepository.class);
		todoManager = new TodoManager(todoRepository);
	}
		
	@Test
	public void testGetTodoList() {
		List<TodoElement> todos = new ArrayList<>();
		todos.add(new TodoElement());
		when(todoRepository.findAll()).thenReturn(todos);
		
		List<TodoElement> todoList = todoManager.getTodoList();
		
		assertEquals(todos, todoList);
		assertEquals(1, todos.size());
		assertEquals(1, todoList.size());
		
	}
	
	@Test
	public void testAddTodo() {
		TodoElement te = new TodoElement("Foo");
		
		todoManager.addTodo(te);
		ArgumentCaptor<TodoElement> todoCaptor = ArgumentCaptor.forClass(TodoElement.class);
		verify(todoRepository).addTodoElement(todoCaptor.capture());
		
		assertEquals(te, todoCaptor.getValue());
	}
	
	@Test
	public void testRemoveTodo() {
		TodoElement te = new TodoElement("Foo");
		
		todoManager.removeTodo(te);
		ArgumentCaptor<TodoElement> todoCaptor = ArgumentCaptor.forClass(TodoElement.class);
		verify(todoRepository).removeTodoElement(todoCaptor.capture());
		
		assertEquals(te, todoCaptor.getValue());
		
	}
	
	@Test
	public void testUpdateTodo() {
		TodoElement te = new TodoElement("Foo");
		
		todoManager.updateTodo(te);
		ArgumentCaptor<TodoElement> todoCaptor = ArgumentCaptor.forClass(TodoElement.class);
		
		verify(todoRepository).updateTodoElement(todoCaptor.capture());
		assertEquals(te, todoCaptor.getValue());
	}
	
	@Test
	public void testAddTagAtTodo() {
		TodoElement todo = mock(TodoElement.class);
		Tag tag = new Tag("0", "Bar");
		
		todoManager.tagTodo(todo, tag);
		//ArgumentCaptor<TodoElement> todoCaptor = ArgumentCaptor.forClass(TodoElement.class);
		ArgumentCaptor<Tag> tagCaptor = ArgumentCaptor.forClass(Tag.class);
		
		verify(todo).addTag(tagCaptor.capture());
		assertEquals(tag, tagCaptor.getValue());
	}
	
}
