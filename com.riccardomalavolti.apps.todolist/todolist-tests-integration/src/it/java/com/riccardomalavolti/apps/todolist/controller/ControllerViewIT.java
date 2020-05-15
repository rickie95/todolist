package com.riccardomalavolti.apps.todolist.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JListFixture;
import org.assertj.swing.fixture.JTableFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.riccardomalavolti.apps.todolist.manager.TodoManager;
import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.model.Todo;
import com.riccardomalavolti.apps.todolist.repositories.tag.TagRepository;
import com.riccardomalavolti.apps.todolist.repositories.tag.TagRepositoryMongoDB;
import com.riccardomalavolti.apps.todolist.repositories.todo.TodoRepository;
import com.riccardomalavolti.apps.todolist.repositories.todo.TodoRepositoryMongoDB;
import com.riccardomalavolti.apps.todolist.view.MainView;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

@RunWith(GUITestRunner.class)
public class ControllerViewIT extends AssertJSwingJUnitTestCase {

	private static MongoServer mongoServer;
	private static InetSocketAddress serverAddress;
	
	private TodoController todoController;
	private MainView todoView;
	private TodoManager todoManager;
	private TodoRepository todoRepo;
	private TagRepository tagRepo;
	private MongoClient mongoClient;
	private FrameFixture window;
	
	@BeforeClass
	public static void setupServer() {
		mongoServer = new MongoServer(new MemoryBackend()); 
		serverAddress = mongoServer.bind();
	}
	
	@AfterClass
	public static void closeServer() {
		mongoServer.shutdown();
	}

	@Override
	protected void onSetUp() {
		mongoClient = new MongoClient(new ServerAddress(serverAddress));
		tagRepo = new TagRepositoryMongoDB(mongoClient);
		todoRepo = new TodoRepositoryMongoDB(mongoClient, tagRepo);
		todoManager = new TodoManager(todoRepo, tagRepo);
		
		todoRepo.findAll().forEach(todo -> todoRepo.removeTodoElement(todo));
		tagRepo.findAll().forEach(tag -> tagRepo.removeTag(tag));
		
		GuiActionRunner.execute(() -> {
			todoView = new MainView();
			todoController = new TodoController(todoView, todoManager);
			todoView.setController(todoController);
			return todoView;
		});
		
		window = new FrameFixture(robot(), todoView);
		window.show();
	}
	
	@Override
	public void onTearDown() {
		mongoClient.close();
	}

	@Test @GUITest
	public void testShowAllTodos() {
		Todo t1 = new Todo("0", "Foo");
		Todo t2 = new Todo("1", "Bar");
		
		todoRepo.addTodoElement(t1);
		todoRepo.addTodoElement(t2);
		
		GuiActionRunner.execute(() -> todoController.showTodos());
		
		Object[][] tableContent = GuiActionRunner.execute(() -> window.table("todoTable").contents());
		
		assertThat(tableContent)
		.containsExactlyInAnyOrder(new Object[][] {
									{"false", t1.toString()},
									{"false", t2.toString()}
									});
	}
	
	@Test @GUITest
	public void testShowAllTags() {
		Tag t1 = new Tag("Foo");
		Tag t2 = new Tag("Bar");
		
		tagRepo.addTag(t1);
		tagRepo.addTag(t2);
		
		GuiActionRunner.execute(() -> todoController.showTags());
		
		Object[] listContent = GuiActionRunner.execute(() -> window.list("tagListView").contents());
		
		assertThat(listContent).containsExactlyInAnyOrder(
				new Object[]{ t1.getText(), t2.getText() });
	}
	
	@Test @GUITest
	public void testAddTodo() {
		JTableFixture todoListPanel = window.table("todoTable");
		
		Todo t = new Todo("Foo");
		
		GuiActionRunner.execute(() -> todoController.addTodo(t));
		
		Object[][] tableContent = GuiActionRunner.execute(() -> todoListPanel.contents());
		
		assertThat(tableContent)
			.containsExactly(new Object[][] {{"false", t.getBody()}});
	}
	
	@Test @GUITest
	public void testAddTag() {
		JListFixture todoListPanel = window.list("tagListView");
		Tag t1 = new Tag("Foo");
		Tag t2 = new Tag("Bar");
		
		GuiActionRunner.execute(() -> {
			todoController.addTag(t1);
			todoController.addTag(t2);
		});
	
		Object[] listContent = GuiActionRunner.execute(() ->  todoListPanel.contents());
		assertThat(listContent).containsExactly(
				new Object[]{ t1.getText(), t2.getText() });
	}
	
	@Test @GUITest
	public void testRemoveTodo() {
		Todo t1 = new Todo("2","Foo");
		Todo t2 = new Todo("4","Bar");
		
		GuiActionRunner.execute(() -> {
			todoController.addTodo(t1);
			todoController.addTodo(t2);
		});
		
		GuiActionRunner.execute(() -> todoController.removeTodo(t2));
		
		assertThat(window.table("todoTable").contents()).containsExactly(new String[][] {
			{ "false", t1.getBody()}
		});
		
	}
	
	@Test @GUITest
	public void testRemoveTag() {
		JListFixture todoListPanel = window.panel("contentPanel").list("tagListView");
		Tag t1 = new Tag("Foo");
		Tag t2 = new Tag("Bar");
		
		GuiActionRunner.execute(() -> {
			todoController.addTag(t1);
			todoController.addTag(t2);
		});
	
		Object[] listContent = todoListPanel.contents();
		assertThat(listContent).containsExactly(
				new Object[]{ t1.getText(), t2.getText() });
		
		GuiActionRunner.execute(() -> todoController.removeTag(t1));
		
		listContent = todoListPanel.contents();
		assertThat(listContent).containsExactly(
				new Object[]{ t2.getText() });
	}
	
	@Test
	public void testUpdateTodo() {
		Todo t1 = new Todo("2","Foo");
		Todo t2 = new Todo("4","Bar");
		
		GuiActionRunner.execute(() -> {
			todoController.addTodo(t1);
			todoController.addTodo(t2);
		});
		
		t1.setBody("Modified Foo");
		
		todoController.updateTodo(t1);
		
		assertThat(window.table("todoTable").contents()).containsExactly(new String[][] {
			{ "false", t1.getBody()},
			{ "false", t2.getBody()}
		});
	}
	
	
}
