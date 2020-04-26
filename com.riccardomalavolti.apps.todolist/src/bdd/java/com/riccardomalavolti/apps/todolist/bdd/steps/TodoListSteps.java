package com.riccardomalavolti.apps.todolist.bdd.steps;

import static org.assertj.swing.launcher.ApplicationLauncher.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.*;

import javax.swing.JFrame;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.riccardomalavolti.apps.todolist.repositories.todo.TodoRepositoryMongoDB;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class TodoListSteps {
	
	private final static String DB_NAME = TodoRepositoryMongoDB.DB_NAME;
	private final static String COLLECTION_NAME = TodoRepositoryMongoDB.COLLECTION_NAME;
	

	private static final Logger LOGGER = LogManager.getLogger(TodoListSteps.class);

	private MongoClient mongoClient;
	private FrameFixture window;
	

	@Before
	public void setupMongo() {
		mongoClient = new MongoClient();
		mongoClient.getDatabase(DB_NAME).drop();
	}
	
	@After
	public void closeMongoClient() {
		mongoClient.close();
		if(window != null)
			window.cleanUp();
	}
	
	@Given("the db contains a todo with ID {string} and text {string}")
	public void db_contains_todo_ID_and_text(String string, String string2) {
		mongoClient.getDatabase(DB_NAME).getCollection(COLLECTION_NAME)
			.insertOne(new Document().append("id", string).append("body", string2));
		
		
		LOGGER.debug("I should wrote something on the db");
		List<Document> list = StreamSupport.stream(mongoClient.getDatabase(DB_NAME)
				.getCollection(COLLECTION_NAME).find().spliterator(), false).collect(Collectors.toList());
		list.forEach(doc -> LOGGER.debug(doc.toString()));
	}
	
	@When("MainView is shown")
	public void mainView_is_shown() {
		application("com.riccardomalavolti.apps.todolist.TodoList")
			.withArgs("--db-name=" + DB_NAME, "--collection-name=" + COLLECTION_NAME, "-v=" + "debug").start();
		
		window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "To Do List".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(BasicRobot.robotWithCurrentAwtHierarchy());
	}
	
	@Then("the todo list contains a todo with ID {string} and text {string}")
	public void todolist_should_contain_todo_with_id_and_text(String id, String text) {
		Object[][] tableContent = GuiActionRunner.execute(() -> window.table("todoTable").contents());
		assertThat(tableContent).contains(new Object[][] {
				{"false", text}
			});
	}
	
}
