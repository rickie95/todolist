package com.riccardomalavolti.apps.todolist.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.timing.Pause.pause;
import static org.assertj.swing.timing.Timeout.timeout;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JComboBoxFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.assertj.swing.timing.Condition;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.riccardomalavolti.apps.todolist.controller.TodoController;
import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.model.Todo;

@RunWith(GUITestRunner.class)
public class TodoDialogNewActionTest extends AssertJSwingJUnitTestCase {

	private static final long TIMEOUT = 5000;

	private Tag t1;
	private Tag t2;
	private String todoText;
	private Todo todo;
	private DefaultComboBoxModel<Tag> comboBoxModel;
	private TodoDialog view;
	private DialogFixture window;
	private TodoController todoController;

	@Mock
	private NewTodoAction todoAction;

	@BeforeClass
	public static void installViolationNotifier() {
		FailOnThreadViolationRepaintManager.install();
	}

	@Override
	protected void onSetUp() {
		MockitoAnnotations.initMocks(this);

		t1 = new Tag("0", "Foo");
		t2 = new Tag("1", "Bar");

		todoText = "";
		todo = new Todo("01", todoText);

		List<Tag> tagList = new ArrayList<Tag>(Arrays.asList(t1, t2));
		tagList.sort((l, r) -> l.getText().compareTo(r.getText()));
		comboBoxModel = new DefaultComboBoxModel<Tag>(tagList.toArray(new Tag[tagList.size()]));

		when(todoAction.getTodo()).thenReturn(todo);

		GuiActionRunner.execute(() -> {
			view = new TodoDialog(todoController, comboBoxModel, todoAction, TodoController.NEW_DIALOG_TITLE);
			return view;
		});

		window = new DialogFixture(robot(), view);
		window.show();

		robot().waitForIdle();

		GuiActionRunner.execute(() -> {
			view.requestFocus();
			view.toFront();
		});

		pause(new Condition("get view ready and focusable.") {
			@Override
			public boolean test() {
				return view.isFocused() && view.isValid() && view.isShowing();
			}
		}, timeout(TIMEOUT));

	}

	@Test
	@GUITest
	public void testInitialState() {

		JTextComponentFixture textBox = window.textBox("todoTextBox");
		JComboBoxFixture comboBox = window.comboBox("tagComboBox");
		JButtonFixture clearButton = window.button("clearButton");
		JButtonFixture confirmButton = window.button("confirmButton");
		JButtonFixture cancelButton = window.button("cancelButton");

		assertThat(window.label("headingLabel").text()).isEqualTo(TodoController.NEW_DIALOG_TITLE);
		assertThat(view.getTitle()).isEqualTo(TodoController.NEW_DIALOG_TITLE);

		assertNotNull(window.label(JLabelMatcher.withText(TodoDialog.TAG_LBL_NO_TAG_TEXT)));

		assertThat(textBox.text()).isEqualTo(todo.getBody());
		// Tag selection must be void
		assertThat(comboBox.selectedItem()).isNull();

		assertThat(clearButton.isEnabled()).isFalse();
		assertThat(confirmButton.isEnabled()).isFalse();
		assertThat(cancelButton.isEnabled()).isTrue();
	}
}
