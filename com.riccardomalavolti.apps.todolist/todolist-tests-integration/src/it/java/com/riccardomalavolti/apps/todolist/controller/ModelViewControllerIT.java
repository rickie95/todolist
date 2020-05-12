package com.riccardomalavolti.apps.todolist.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.data.TableCell;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.riccardomalavolti.apps.todolist.manager.TodoManager;
import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.model.Todo;
import com.riccardomalavolti.apps.todolist.repositories.tag.TagRepository;
import com.riccardomalavolti.apps.todolist.repositories.tag.TagRepositoryMongoDB;
import com.riccardomalavolti.apps.todolist.repositories.todo.TodoRepository;
import com.riccardomalavolti.apps.todolist.repositories.todo.TodoRepositoryMongoDB;
import com.riccardomalavolti.apps.todolist.view.MainView;

@RunWith(GUITestRunner.class)
public class ModelViewControllerIT extends AssertJSwingJUnitTestCase {

	private static final String TODO_TEXT = "This is a foo todo";
	private static final String TODO_TEXT_2 = "Foo";
	private static final String EMPTY_TEXT = "";
	private static final String TODO_ID = "0";
	private static final String TAG_TEXT = "Foo";

	private MainView todoView;
	private TodoRepository todoRepo;
	private TagRepository tagRepo;
	private FrameFixture window;
	private MongoClient client;
	private TodoManager todoManager;
	private TodoController todoController;

	private MongoDatabase todolistDatabase;
	private MongoCollection<Document> todoCollection;
	private MongoDatabase tagDatabase;
	private MongoCollection<Document> tagCollection;

	@Override
	protected void onSetUp() {
		client = new MongoClient(new ServerAddress("localhost", 27017));

		tagRepo = new TagRepositoryMongoDB(client);
		todoRepo = new TodoRepositoryMongoDB(client, tagRepo);
		todoManager = new TodoManager(todoRepo, tagRepo);

		GuiActionRunner.execute(() -> {
			todoView = new MainView();
			todoController = new TodoController(todoView, todoManager);
			todoView.setController(todoController);
		});

		todolistDatabase = client.getDatabase(TodoRepositoryMongoDB.DB_NAME);
		todolistDatabase.drop();
		todoCollection = todolistDatabase.getCollection(TodoRepositoryMongoDB.COLLECTION_NAME);

		tagDatabase = client.getDatabase(TagRepositoryMongoDB.DB_NAME);
		tagDatabase.drop();
		tagCollection = tagDatabase.getCollection(TagRepositoryMongoDB.COLLECTION_NAME);

		window = new FrameFixture(robot(), todoView);
		window.show();

	}

	@Override
	protected void onTearDown() {
		client.close();
	}

	@Test
	@GUITest
	public void testAddTagShouldOpenADialogAndSaveItOnMongoDb() {
		// Open a "new Tag" dialog and create a fixture for it.
		JButtonFixture newTagBtn = window.button("newTagBtn");
		assertThat(newTagBtn).isNotNull();
		newTagBtn.click();
		DialogFixture tagWindow = new DialogFixture(robot(), todoController.getNewTagDialog());

		tagWindow.textBox("tagTextField").setText(TAG_TEXT);
		tagWindow.button("insertButton").click();

		assertThat(dbContainsTagWithText(TAG_TEXT)).isTrue();
	}

	@Test
	@GUITest
	public void testAddTodoShouldOpenADialogAndSaveItOnMongoDb() {
		JButtonFixture newTodoBtn = window.button("newTodoBtn");
		assertThat(newTodoBtn).isNotNull();
		newTodoBtn.click();

		DialogFixture todoWindow = new DialogFixture(robot(), todoController.getNewTodoDialog());

		todoWindow.textBox("todoTextBox").setText(TODO_TEXT);
		todoWindow.button("confirmButton").click();

		assertThat(dbContainsTodoWithText(TODO_TEXT)).isTrue();
	}

	@Test
	@GUITest
	public void testDoubleClickOnTodoShouldOpenTheEditDialogAndCommitChangesOnMongoDb() {
		GuiActionRunner.execute(() -> todoController.addTodo(new Todo(TODO_ID, TODO_TEXT_2)));
		window.table("todoTable").selectCell(TableCell.row(0).column(1)).doubleClick();

		DialogFixture todoWindow = new DialogFixture(robot(), todoController.getEditTodoDialog());

		todoWindow.textBox("todoTextBox").setText(EMPTY_TEXT);
		todoWindow.textBox("todoTextBox").setText(TODO_TEXT);
		todoWindow.button("confirmButton").click();

		assertThat(dbContainsTodoWithText(TODO_TEXT)).isTrue();
	}

	@Test
	@GUITest
	public void testRemoveTodo() {
		GuiActionRunner.execute(() -> todoController.addTodo(new Todo(TODO_ID, TODO_TEXT)));

		window.table("todoTable").selectCell(TableCell.row(0).column(1)).click();
		window.button("removeTodoButton").click();

		assertThat(getAllTodosFromDB()).isEmpty();
	}

	@Test
	@GUITest
	public void testSetTodoAsCompleted() {
		GuiActionRunner.execute(() -> todoController.addTodo(new Todo(TODO_ID, TODO_TEXT)));

		window.table("todoTable").selectCell(TableCell.row(0).column(0)).click();

		Todo todo = getAllTodosFromDB().get(0);

		assertThat(todo.getBody()).isEqualTo(TODO_TEXT);
		assertThat(todo.getStatus()).isTrue();
	}

	private List<Todo> getAllTodosFromDB() {
		return StreamSupport.stream(todoCollection.find().spliterator(), false)
				.map(doc -> new Todo(doc.getString(TodoRepositoryMongoDB.MONGO_KEY_FOR_ID),
						doc.getString(TodoRepositoryMongoDB.MONGO_KEY_FOR_BODY),
						getSelectedTag(doc.getList(TodoRepositoryMongoDB.MONGO_KEY_FOR_TAGLIST, String.class)),
						doc.getBoolean(TodoRepositoryMongoDB.MONGO_KEY_FOR_STATUS)))
				.collect(Collectors.toList());
	}

	private List<Tag> getSelectedTag(List<String> tagList) {
		return StreamSupport.stream(tagCollection.find().spliterator(), false)
				.map(doc -> new Tag(doc.getString("id"), doc.getString("text")))
				.filter(tag -> tagList.contains(tag.getId())).collect(Collectors.toList());
	}

	private List<Tag> getAllTagsFromDB() {
		return StreamSupport.stream(tagCollection.find().spliterator(), false)
				.map(doc -> new Tag(doc.getString("id"), doc.getString("text"))).collect(Collectors.toList());
	}

	private Boolean dbContainsTagWithText(String text) {
		List<Tag> tagList = getAllTagsFromDB();
		for (Tag t : tagList)
			if (t.getText().equals(text))
				return true;
		return false;
	}

	private Boolean dbContainsTodoWithText(String todoText) {
		List<Todo> todoList = getAllTodosFromDB();
		for (Todo t : todoList)
			if (t.getBody().equals(todoText))
				return true;
		return false;
	}
}
