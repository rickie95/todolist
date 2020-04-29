package com.riccardomalavolti.apps.todolist.view;

import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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
public class EditTodoDialogTest extends AssertJSwingJUnitTestCase {

	private DialogFixture window;
	private EditTodoDialog view;
	private DefaultComboBoxModel<Tag> comboBoxModel;
	private Tag t1, t2;
	private Todo todo;

	@Mock
	private TodoController todoController;
	@Captor
	private ArgumentCaptor<ArrayList<Tag>> tagListCaptor;
	private String todoText;

	@BeforeClass
	public static void setupOnce() {
		FailOnThreadViolationRepaintManager.install();
	}

	@Override
	protected void onSetUp() {
		MockitoAnnotations.initMocks(this);

		t1 = new Tag("0", "Foo");
		t2 = new Tag("1", "Bar");
		todoText = "A foo todo to be edited.";

		todo = new Todo("01", todoText);
		todo.addTag(t1);

		List<Tag> tagList = new ArrayList<Tag>(Arrays.asList(t1, t2));
		tagList.sort((l, r) -> l.getText().compareTo(r.getText()));
		comboBoxModel = new DefaultComboBoxModel<Tag>(tagList.toArray(new Tag[tagList.size()]));

		GuiActionRunner.execute(() -> {
			view = new EditTodoDialog(todoController, comboBoxModel, todo);
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
		JTextComponentFixture textBox = window.textBox("todoTextBox");
		JComboBoxFixture comboBox = window.comboBox("tagComboBox");
		JButtonFixture clearButton = window.button("clearButton");
		JButtonFixture confirmButton = window.button("clearButton");
		JButtonFixture cancelButton = window.button("clearButton");

		assertNotNull(window.label(JLabelMatcher.withText(EditTodoDialog.HEADING_LABEL_TEXT)));
		assertNotNull(window.label(JLabelMatcher.withText(Tag.listToString(new ArrayList<Tag>(todo.getTagList())))));
		// Text box must be empty
		assertThat(textBox.text()).isEqualTo(todoText);
		// Tag selection must be void
		assertThat(comboBox.selectedItem()).isNull();

		assertThat(clearButton.isEnabled()).isTrue();
		assertThat(confirmButton.isEnabled()).isTrue();
		assertThat(cancelButton.isEnabled()).isTrue();

	}

	@Test
	@GUITest
	public void testRemovingAllTagsFromATodoShouldBeAllowed() {
		// We have a todo with a tag
		JButtonFixture clearButton = window.button("clearButton");
		assertThat(clearButton.isEnabled()).isTrue();
		clearButton.click();

		Set<Tag> tagList = GuiActionRunner.execute(() -> view.getTodoElement().getTagList());
		assertThat(tagList).isEmpty();
		String tagLabelText = GuiActionRunner.execute(() -> window.label("tagLabel").text());
		assertThat(tagLabelText).isEqualTo(EditTodoDialog.TAG_LBL_NO_TAG_TEXT);
	}

	@Test
	@GUITest
	public void testEditAndCommitUpdate() {
		String new_text = "Updated todo";
		JTextComponentFixture textBox = window.textBox("todoTextBox");

		textBox.setText(new_text);

		ArgumentCaptor<Todo> todoCaptor = ArgumentCaptor.forClass(Todo.class);

		window.button("confirmButton").click();

		verify(todoController).updateTodo(todoCaptor.capture());
		Set<Tag> tagList = GuiActionRunner.execute(() -> view.getTodoElement().getTagList());
		assertThat(todoCaptor.getValue().getTagList()).containsExactlyElementsOf(tagList);
		assertThat(todoCaptor.getValue().getBody()).isEqualTo(new_text);
	}

}
