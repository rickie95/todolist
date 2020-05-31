package com.riccardomalavolti.apps.todolist.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.riccardomalavolti.apps.todolist.manager.TodoManager;
import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.model.Todo;
import com.riccardomalavolti.apps.todolist.view.DialogController;
import com.riccardomalavolti.apps.todolist.view.TodoView;

public class TodoControllerTest {

	TodoView todoView;
	TodoManager todoManager;

	TodoController todoController;

	@BeforeClass
	public static void installViolationNotifier() {
		FailOnThreadViolationRepaintManager.install();
	}

	@Before
	public void setup() {
		todoView = mock(TodoView.class);
		todoManager = mock(TodoManager.class);

		todoController = new TodoController(todoView, todoManager);
	}

	@Test
	public void testInitialState() {
		List<Todo> todoList = new ArrayList<>();
		when(todoManager.getTodoList()).thenReturn(todoList);
		List<Tag> tagList = new ArrayList<>();
		when(todoManager.getTagList()).thenReturn(tagList);

		verify(todoManager).getTodoList();
		verify(todoManager).getTagList();
		verify(todoView).showAllTodo(todoList);
		verify(todoView).showAllTags(tagList);
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
		String errorMessage = "A problem as occurred " + "while insering a new todo: java.lang.RuntimeException: "
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
		String errorMessage = "A problem as occurred " + "while insering a new tag: java.lang.RuntimeException: "
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
		verify(todoView, times(2)).showAllTodo(todoList);
	}

	@Test
	public void testFindTagByText() {
		String searchText = "foo";
		List<Tag> tagList = new ArrayList<Tag>();
		when(todoManager.findTagByText(searchText)).thenReturn(tagList);

		todoController.findTagByText(searchText);

		verify(todoManager).findTagByText(searchText);
		verify(todoView, times(2)).showAllTags(tagList);
	}

	@Test
	public void testFindTodoByTag() {
		Tag tag = new Tag("Foo");
		List<Todo> todoList = new ArrayList<Todo>();

		when(todoManager.findTodoByTag(tag)).thenReturn(todoList);
		todoController.findTodoByTag(tag);

		verify(todoManager).findTodoByTag(tag);
		verify(todoView, times(2)).showAllTodo(todoList);
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
	public void testPressingNewTodoButtonShouldShowANewDialog() {
		DialogController dialogController = mock(DialogController.class);
		todoController.setDialogController(dialogController);
		DefaultComboBoxModel<Tag> tagListModel = new DefaultComboBoxModel<Tag>();

		GuiActionRunner.execute(() -> todoController.newTodoDialog(tagListModel));

		verify(dialogController).todoDialog(todoController, tagListModel, null);
	}

	@Test
	public void testPressingEditTodoButtonShouldShowANewAdUniqueDialog() {
		DialogController dialogController = mock(DialogController.class);
		todoController.setDialogController(dialogController);

		DefaultComboBoxModel<Tag> tagListModel = new DefaultComboBoxModel<Tag>();
		Todo todo = new Todo("0", "foo");

		GuiActionRunner.execute(() -> todoController.editTodoDialog(tagListModel, todo));

		verify(dialogController).todoDialog(todoController, tagListModel, todo);
	}

	@Test
	public void testPressingNewTagButtonShouldShowANewAdUniqueDialog() {
		DialogController dialogController = mock(DialogController.class);
		todoController.setDialogController(dialogController);

		GuiActionRunner.execute(() -> todoController.newTagDialog());

		verify(dialogController).newTagDialog();
	}

	@Test
	public void testDisposeTodoDialog() {
		DialogController dialogController = mock(DialogController.class);
		todoController.setDialogController(dialogController);

		JDialog aDialog = GuiActionRunner.execute(() -> new JDialog());

		todoController.dispose(aDialog);

		verify(dialogController).disposeDialog(aDialog);
	}

}
