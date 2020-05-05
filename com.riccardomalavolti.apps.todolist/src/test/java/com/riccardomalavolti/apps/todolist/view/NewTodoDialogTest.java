package com.riccardomalavolti.apps.todolist.view;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultComboBoxModel;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertNotNull;

import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.awaitility.Awaitility;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JComboBoxFixture;
import org.assertj.swing.fixture.JLabelFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.BeforeClass;
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
	
	@BeforeClass
	public static void installViolationNotifier() {
		FailOnThreadViolationRepaintManager.install();
	}

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
			Awaitility.await().atMost(5, TimeUnit.SECONDS).until(dialogIsBloodyReady());
			return view;
		});

		window = new DialogFixture(robot(), view);
		window.show();
		
		robot().waitForIdle();

		GuiActionRunner.execute(() -> {
			view.requestFocus();
			view.toFront();
		});
		
	}

	private Callable<Boolean> dialogIsBloodyReady() {
		return () -> view.isValid() && view.isVisible() && view.isShowing();
				//&& view.isVisible();
				//&& view.isReady();
	}
	
	@Override
	protected void onTearDown() {
		super.onTearDown();
		if (window.button("clearButton").isEnabled())
			window.button("clearButton").click();

		GuiActionRunner.execute(() -> view.dispose());
	}

	@Test
	@GUITest
	public void testInitialState() {
		JTextComponentFixture textBox = window.textBox("todoTextField");
		JComboBoxFixture comboBox = window.comboBox("tagComboBox");
		JButtonFixture clearButton = window.button("clearButton");
		JButtonFixture confirmButton = window.button("confirmButton");
		JButtonFixture cancelButton = window.button("cancelButton");

		assertNotNull(window.label(JLabelMatcher.withText(NewTodoDialog.HEADING_LABEL_TEXT)));
		assertNotNull(window.label(JLabelMatcher.withText(NewTodoDialog.TAG_LBL_NO_TAG_TEXT)));
		// Text box must be empty
		assertThat(textBox.text()).isEqualTo("");
		// Tag selection must be void
		assertThat(comboBox.selectedItem()).isNull();

		assertThat(clearButton.isEnabled()).isFalse();
		assertThat(confirmButton.isEnabled()).isFalse();
		assertThat(cancelButton.isEnabled()).isTrue();

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
		// Selecting an element
		window.comboBox("tagComboBox").selectItem(0);
		assertThat(window.comboBox("tagComboBox").selectedItem()).isEqualTo("Bar");
		assertThat(window.label("tagLabel").text()).isEqualTo("(Bar)");

		// Click on Clear Tag
		window.button("clearButton").click();
		assertThat(window.label("tagLabel").text()).isEqualTo(NewTodoDialog.TAG_LBL_NO_TAG_TEXT);
		assertThat(window.comboBox("tagComboBox").selectedItem()).isNull();
	}

	@Test
	@GUITest
	public void testInsertTodo() {
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
		window.button("cancelButton").click();
		verify(todoController).dispose(view);
	}

}
