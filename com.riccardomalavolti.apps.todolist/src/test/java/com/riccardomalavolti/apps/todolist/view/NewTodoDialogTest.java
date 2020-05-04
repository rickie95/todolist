package com.riccardomalavolti.apps.todolist.view;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultComboBoxModel;

import static org.assertj.core.api.Assertions.*;

import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.awaitility.Awaitility;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JComboBoxFixture;
import org.assertj.swing.fixture.JLabelFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.edt.GuiActionRunner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.swing.annotation.GUITest;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.Test;
import java.util.Arrays;

import com.riccardomalavolti.apps.todolist.controller.TodoController;
import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.model.Todo;

@RunWith(GUITestRunner.class)
public class NewTodoDialogTest extends AssertJSwingJUnitTestCase {

	private static final Logger LOGGER = LogManager.getLogger(NewTodoDialog.class);

	private static int count = 0;

	private DialogFixture window;
	private NewTodoDialog view;
	private DefaultComboBoxModel<Tag> comboBoxModel;
	private Tag t1, t2;

	@Mock
	private TodoController todoController;
	@Captor
	private ArgumentCaptor<ArrayList<Tag>> tagListCaptor;

	@Override
	protected void onSetUp() {
		MockitoAnnotations.initMocks(this);
		t1 = new Tag("0", "Foo");
		t2 = new Tag("1", "Bar");

		List<Tag> tagList = new ArrayList<Tag>(Arrays.asList(t1, t2));
		tagList.sort((l, r) -> l.getText().compareTo(r.getText()));
		comboBoxModel = new DefaultComboBoxModel<Tag>(tagList.toArray(new Tag[tagList.size()]));

		GuiActionRunner.execute(() -> {
			view = new NewTodoDialog(todoController, comboBoxModel);
			return view;
		});

		Awaitility.await().atMost(5, TimeUnit.SECONDS).until(dialogIsBloodyReady());

		window = new DialogFixture(robot(), view);
		window.show();
	}

	private Callable<Boolean> dialogIsBloodyReady() {
		return () -> view.isValid() && view.isActive() && view.isEnabled() && view.isFocused() && view.isShowing()
				&& view.isVisible() && view.isReady();
	}

	@Test
	@GUITest
	public void testInitialState() {
		count += 1;
		LOGGER.info("initialState {}", count);
		assertThat(window.label("tagLabel").text()).isEqualTo(NewTodoDialog.TAG_LBL_NO_TAG_TEXT);

		// Text box must be empty
		assertThat(window.textBox("todoTextField").text()).isEmpty();
		// Tag selection must be void
		assertThat(window.comboBox("tagComboBox").selectedItem()).isNull();

		assertThat(window.button("clearButton").isEnabled()).isFalse();
		assertThat(window.button("confirmButton").isEnabled()).isFalse();
		assertThat(window.button("cancelButton").isEnabled()).isTrue();

	}

	@Test
	@GUITest
	public void testChangingTextInTodoTextFieldShouldEnableInsertButton() {
		JTextComponentFixture todoTextBox = window.textBox("todoTextField");
		JButtonFixture insertButton = window.button("confirmButton");

		// Starting empty, insertButton must be disabled
		assertThat(todoTextBox.text()).isEmpty();
		assertThat(insertButton.isEnabled()).isFalse();

		// Write something, insertButton must be enabled
		todoTextBox.setText("foo");
		assertThat(todoTextBox.text()).isNotEmpty();
		assertThat(insertButton.isEnabled()).isTrue();

		// Cancel everything, insertButton must be disabled
		todoTextBox.setText("");
		assertThat(todoTextBox.text()).isEmpty();
		assertThat(insertButton.isEnabled()).isFalse();
	}

	@Test
	@GUITest
	public void testAddingTagsShouldModifyTheTagLabel() {
		count += 1;
		LOGGER.info("testAddingTagsShouldModifyTheTagLabel {}", count);
		JLabelFixture tagLabel = window.label("tagLabel");
		JComboBoxFixture tagCombo = window.comboBox("tagComboBox");

		// Initial status, no tag selected
		assertThat(tagLabel.text()).isEqualTo(NewTodoDialog.TAG_LBL_NO_TAG_TEXT);
		assertThat(tagCombo.selectedItem()).isNull();

		// Selecting an element
		tagCombo.click().selectItem(0);
		assertThat(tagCombo.selectedItem()).isEqualTo("Bar");
		assertThat(tagLabel.text()).isEqualTo("(Bar)");

		tagCombo.click().selectItem(1);
		assertThat(tagCombo.selectedItem()).isEqualTo("Foo");
		assertThat(tagLabel.text()).isEqualTo("(Bar)(Foo)");

	}

	@Test
	@GUITest
	public void testClearTagsShouldRestoreTagLabel() {
		count += 1;
		LOGGER.info("testClearTagsShouldRestoreTagLabel {}", count);

		// Selecting an element
		GuiActionRunner.execute(() -> view.setTagLabel("(Bar)"));
		GuiActionRunner.execute(() -> view.enableClearButton());
		// assertThat(window.comboBox("tagComboBox").selectedItem()).isEqualTo("Bar");
		assertThat(window.label("tagLabel").text()).isEqualTo("(Bar)");

		// Click on Clear Tag
		window.button("clearButton").click();
		assertThat(window.label("tagLabel").text()).isEqualTo(NewTodoDialog.TAG_LBL_NO_TAG_TEXT);
		assertThat(window.comboBox("tagComboBox").selectedItem()).isNull();
	}

	@Test
	@GUITest
	public void testInsertTodo() {
		count += 1;
		LOGGER.info("testInsertTodo {}", count);
		String todoText = "A new Foo todo";
		window.textBox("todoTextField").setText(todoText);

		ArgumentCaptor<Todo> todoTaggedArg = ArgumentCaptor.forClass(Todo.class);

		window.button("confirmButton").click();
		verify(todoController).addTodo(todoTaggedArg.capture());

		assertThat(todoTaggedArg.getValue().getBody()).isEqualTo(todoText);

		// Check also that components were restored after insertion
		assertThat(window.textBox("todoTextField").text()).isEmpty();
		assertThat(window.button("confirmButton").isEnabled()).isFalse();
		assertThat(window.button("clearButton").isEnabled()).isFalse();
	}

	@Test
	@GUITest
	public void testCancelButtonAction() {
		count += 1;
		LOGGER.info("testCancelButtonAction {}", count);
		window.button("cancelButton").click();
		verify(todoController).dispose(view);
	}

}
