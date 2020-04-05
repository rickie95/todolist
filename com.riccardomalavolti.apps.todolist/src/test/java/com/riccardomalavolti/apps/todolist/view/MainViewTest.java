package com.riccardomalavolti.apps.todolist.view;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JListFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.assertj.swing.fixture.JTableFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.riccardomalavolti.apps.todolist.controller.TodoController;
import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.model.Todo;

@RunWith(GUITestRunner.class)
public class MainViewTest extends AssertJSwingJUnitTestCase{
	
	private FrameFixture window;
	private MainView view;
	
	private TodoController todoController;
	
	@Override
	protected void onSetUp() {
		GuiActionRunner.execute(() -> {
			view = new MainView();
			return view;
		});
		
		window = new FrameFixture(robot(), view);
		window.show();
		
	}
	
	@Override
	protected void onTearDown() {
		super.onTearDown();
		view.dispose();
	}
	
	@Test @GUITest
	public void testInitialState() {
		JPanelFixture todoPanel = window.panel("contentPanel").panel("todoPanel");
		
		todoPanel.panel("todoLabelPanel").label(JLabelMatcher.withText("Todo"));
		assertTrue(todoPanel.panel("todoControlPanel").button(JButtonMatcher.withText("new To Do")).isEnabled());
		
		JPanelFixture tagPanel = window.panel("contentPanel").panel("tagPanel");
		
		tagPanel.panel("tagLabelPanel").label(JLabelMatcher.withText("Tags"));
		assertTrue(tagPanel.panel("tagControlPanel").button(JButtonMatcher.withText("new Tag")).isEnabled());
	}
	
	@Test @GUITest
	public void testShowAllTodo() {
		JTableFixture todoListPanel = window.panel("contentPanel").table("todoTable");
		
		Todo t1 = new Todo("2","Foo");
		Todo t2 = new Todo("4","Bar");
		List<Todo> todoList = new ArrayList<Todo>(Arrays.asList(t1, t2));
		
		GuiActionRunner.execute(() -> view.showAllTodo(todoList));
		
		Object[][] tableContent = todoListPanel.contents();
		
		assertThat(tableContent)
		.containsExactly(new Object[][] {
									{"false", t1.toString()},
									{"false", t2.toString()}
									});
	}
	
	@Test @GUITest
	public void testShowAllTags() {
		JListFixture todoListPanel = window.panel("contentPanel").list("tagListView");
		Tag t1 = new Tag("Foo");
		Tag t2 = new Tag("Bar");
		List<Tag> tagList = new ArrayList<Tag>(Arrays.asList(t1, t2));
		
		GuiActionRunner.execute(() -> view.showAllTags(tagList));
		
		Object[] listContent = todoListPanel.contents();
		assertThat(listContent).containsExactly(
				new Object[]{ t1.getText(), t2.getText() });
	}
	
	@Test @GUITest
	public void testAddTodo() {
		JTableFixture todoListPanel = window.panel("contentPanel").table("todoTable");
		
		Todo t = new Todo("Foo");
		
		GuiActionRunner.execute(() -> view.addTodo(t));
		
		Object[][] tableContent = todoListPanel.contents();
		
		assertThat(tableContent)
			.containsExactly(new Object[][] {{"false", t.getBody()}});
	}
	
	@Test @GUITest
	public void testAddTag() {
		JListFixture todoListPanel = window.panel("contentPanel").list("tagListView");
		Tag t1 = new Tag("Foo");
		Tag t2 = new Tag("Bar");
		
		GuiActionRunner.execute(() -> view.addTag(t1));
		GuiActionRunner.execute(() -> view.addTag(t2));
	
		Object[] listContent = todoListPanel.contents();
		assertThat(listContent).containsExactly(
				new Object[]{ t1.getText(), t2.getText() });
	}
	
	@Test @GUITest
	public void testRemoveTodo(){
		JTableFixture todoListPanel = window.panel("contentPanel").table("todoTable");
		
		Todo t1 = new Todo("2","Foo");
		Todo t2 = new Todo("4","Bar");
		
		GuiActionRunner.execute(() -> view.addTodo(t1));
		GuiActionRunner.execute(() -> view.addTodo(t2));
		
		GuiActionRunner.execute(() -> view.removeTodo(t1));
		
		Object[][] tableContent = todoListPanel.contents();
		
		assertThat(tableContent).containsExactly(new Object[][] {{"false", t2.getBody()}});
	}
	
	@Test @GUITest
	public void testRemoveTag() {
		JListFixture todoListPanel = window.panel("contentPanel").list("tagListView");
		Tag t1 = new Tag("Foo");
		Tag t2 = new Tag("Bar");
		
		GuiActionRunner.execute(() -> view.addTag(t1));
		GuiActionRunner.execute(() -> view.addTag(t2));
	
		Object[] listContent = todoListPanel.contents();
		assertThat(listContent).containsExactly(
				new Object[]{ t1.getText(), t2.getText() });
		
		GuiActionRunner.execute(() -> view.removeTag(t1));
		
		listContent = todoListPanel.contents();
		assertThat(listContent).containsExactly(
				new Object[]{ t2.getText() });
	}
	
	@Test @GUITest
	public void testRemoveNonExistentTag() {
		JListFixture todoListPanel = window.panel("contentPanel").list("tagListView");
		Tag t1 = new Tag("2", "Foo");
		Tag t2 = new Tag("9", "Bar");
		
		GuiActionRunner.execute(() -> view.addTag(t1));
		GuiActionRunner.execute(() -> view.addTag(t2));
		
		GuiActionRunner.execute(() -> view.removeTag(new Tag("3", "baz")));
	
		Object[] listContent = todoListPanel.contents();
		assertThat(listContent).containsExactly(
				new Object[]{ t1.getText(), t2.getText() });
	}
	
	@Test @GUITest
	public void testUpdateTodo() {
		JTableFixture todoListPanel = window.panel("contentPanel").table("todoTable");
		
		Todo t1 = new Todo("2","Foo");
		Todo t2 = new Todo("4","Bar");
		
		GuiActionRunner.execute(() -> view.addTodo(t1));
		GuiActionRunner.execute(() -> view.addTodo(t2));
		
		t2.setBody("new Bar");
		
		Object[][] tableContent = todoListPanel.contents();
		
		assertThat(t2.getBody()).isEqualTo("new Bar");
		assertThat(tableContent)
			.contains(new Object[][] {{"false", t2.getBody()}});
	}
	
	@Test @GUITest
	public void testNewTodoButtonClick() {
		todoController = mock(TodoController.class);
		DefaultComboBoxModel<Tag> comboTagModel = new DefaultComboBoxModel<>();
		JButtonFixture newTodoBtn = window.button("newTodoBtn");
		view.setController(todoController);
		view.setTagListModel(comboTagModel);
		
		newTodoBtn.click();
		
		verify(todoController).newTodoDialog(comboTagModel);
	}
	
	@Test @GUITest
	public void testNewTagAction() {
		todoController = mock(TodoController.class);
		JButtonFixture newTagBtn = window.button("newTagBtn");
		view.setController(todoController);
		
		newTagBtn.click();
		
		verify(todoController).newTagDialog();
	}
	
	@Test
	public void testSetController() {
		TodoController controller = mock(TodoController.class);
		
		view.setController(controller);
		
		assertThat(view.getController()).isNotNull();
		assertThat(view.getController()).isEqualTo(controller);
	}
	
}
