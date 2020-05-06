package com.riccardomalavolti.apps.todolist.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.timing.Pause.pause;
import static org.assertj.swing.timing.Timeout.timeout;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultComboBoxModel;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.assertj.swing.timing.Condition;
import org.awaitility.Awaitility;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.riccardomalavolti.apps.todolist.controller.TodoController;
import com.riccardomalavolti.apps.todolist.model.Tag;
import com.riccardomalavolti.apps.todolist.model.Todo;

@RunWith(GUITestRunner.class)
public class NewTodoDialogTest extends AssertJSwingJUnitTestCase {

	private static final int TIMEOUT = 5000;

	@BeforeClass
	public static void installViolationNotifier() {
		FailOnThreadViolationRepaintManager.install();
	}

	private Tag t1;
	private Tag t2;
	private DialogFixture window;
	private NewTodoDialog view;
	private DefaultComboBoxModel<Tag> comboBoxModel;

	@Mock
	private TodoController todoController;

	@Captor
	private ArgumentCaptor<Todo> todoCaptor;

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

		window = new DialogFixture(robot(), view);
		window.show();

		robot().waitForIdle();

		GuiActionRunner.execute(() -> {
			view.requestFocus();
			view.toFront();
		});
		
		pause(
				new Condition("") {
					@Override
					public boolean test() {
						return view.isFocused() && view.isValid() && view.isShowing();
					}
				}, timeout(TIMEOUT));
	}

	@Override
	protected void onTearDown() {
		super.onTearDown();
		GuiActionRunner.execute(() -> view.dispose());
	}

	@Test
	@GUITest
	public void testInsertTodo() {
		// Put some text and click confirm
		String todoText = "A foo todo";
		window.textBox("todoTextBox").enterText(todoText);

		window.button("confirmButton").click();

		verify(todoController).addTodo(todoCaptor.capture());
		assertThat(todoCaptor.getValue().getBody()).isEqualTo(todoText);
	}

	

}
