package com.riccardomalavolti.apps.todolist.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.riccardomalavolti.apps.todolist.controller.TodoController;
import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.model.Todo;

public class NewTodoActionTest {

	@Mock
	private TodoController todoController;
	@Captor
	private ArgumentCaptor<Todo> todoCaptor;

	private NewTodoAction todoAction;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		todoAction = new NewTodoAction(todoController);
	}

	@Test
	public void testSendToController() {
		Set<Tag> tagList = new HashSet<>();
		String body = "Foo";
		todoAction.sendToController(body, tagList);
		verify(todoController).addTodo(todoCaptor.capture());
		assertThat(todoCaptor.getValue().getBody()).isEqualTo(body);
		assertThat(todoCaptor.getValue().getTagList()).isEqualTo(tagList);
	}

	@Test
	public void testGetTodo() {
		Todo todo = todoAction.getTodo();
		assertThat(todo).isNotNull();
		assertThat(todo.getBody()).isEqualTo("");
	}

	@Test
	public void testGetTitle() {
		assertThat(todoAction.getTitle()).isEqualTo(NewTodoAction.TITLE_TEXT);
	}

	@Test
	public void testGetHeadingLabelText() {
		assertThat(todoAction.getHeading()).isEqualTo(NewTodoAction.HEADING_LABEL_TEXT);
	}

	@Test
	public void testGetController() {
		assertThat(todoAction.getController()).isEqualTo(todoController);
	}

}
