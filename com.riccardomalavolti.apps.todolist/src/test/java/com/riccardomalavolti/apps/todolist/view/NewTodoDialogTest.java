package com.riccardomalavolti.apps.todolist.view;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

import static org.assertj.core.api.Assertions.*;

import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JComboBoxFixture;
import org.assertj.swing.fixture.JLabelFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.edt.GuiActionRunner;
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
		assertThat(window.label("headingLabel").text()).isEqualTo(NewTodoDialog.HEADING_LABEL_TEXT);
		assertThat(window.label("tagLabel").text()).isEqualTo(NewTodoDialog.TAG_LBL_NO_TAG_TEXT);

		// Text box must be empty
		String textBoxText = GuiActionRunner.execute(() -> window.textBox("todoTextBox").text());
		assertThat(textBoxText).isEmpty();
		// Tag selection must be void
		assertThat(window.comboBox("tagComboBox").selectedItem()).isNull();

		assertThat(window.button("clearButton").isEnabled()).isFalse();
		assertThat(window.button("confirmButton").isEnabled()).isFalse();
		assertThat(window.button("cancelButton").isEnabled()).isTrue();

	}

	@Test
	@GUITest
	public void testChangingTextInTodoTextFieldShouldEnableInsertButton() {
		JTextComponentFixture todoTextBox = window.textBox("todoTextBox");
		JButtonFixture insertButton = window.button("confirmButton");

		// Starting empty, insertButton must be disabled
		assertThat(GuiActionRunner.execute(() -> todoTextBox.text())).isEmpty();
		assertThat(insertButton.isEnabled()).isFalse();

		// Write something, insertButton must be enabled
		todoTextBox.setText("foo");
		assertThat(GuiActionRunner.execute(() -> todoTextBox.text())).isNotEmpty();
		assertThat(insertButton.isEnabled()).isTrue();

		// Cancel everything, insertButton must be disabled
		todoTextBox.setText("");
		assertThat(GuiActionRunner.execute(() -> todoTextBox.text())).isEmpty();
		assertThat(insertButton.isEnabled()).isFalse();
	}

	@Test
	@GUITest
	public void testAddingTagsShouldModifyTheTagLabel() {
		String tagLabelText;
		JLabelFixture tagLabel = window.label("tagLabel");
		JComboBoxFixture tagCombo = window.comboBox("tagComboBox");

		// Initial status, no tag selected
		tagLabelText = GuiActionRunner.execute(() -> tagLabel.text());
		assertThat(tagLabelText).isEqualTo(NewTodoDialog.TAG_LBL_NO_TAG_TEXT);
		assertThat(tagCombo.selectedItem()).isNull();

		// Selecting an element
		tagCombo.click().selectItem(0);
		assertThat(tagCombo.selectedItem()).isEqualTo("Bar");
		tagLabelText = GuiActionRunner.execute(() -> tagLabel.text());
		assertThat(tagLabelText).isEqualTo("(Bar)");

		tagCombo.click().selectItem(1);
		assertThat(tagCombo.selectedItem()).isEqualTo("Foo");
		tagLabelText = GuiActionRunner.execute(() -> tagLabel.text());
		assertThat(tagLabelText).isEqualTo("(Bar)(Foo)");

	}

	@Test
	@GUITest
	public void testClearTagsShouldRestoreTagLabel() {
		String tagLabelText, selectedTag;

		JLabelFixture tagLabel = window.label("tagLabel");
		JButtonFixture clearButton = window.button("clearButton");
		JComboBoxFixture tagCombo = window.comboBox("tagComboBox");

		// Selecting an element
		tagCombo.selectItem(0);
		selectedTag = GuiActionRunner.execute(() -> tagCombo.selectedItem());
		assertThat(selectedTag).isEqualTo("Bar");
		tagLabelText = GuiActionRunner.execute(() -> tagLabel.text());
		assertThat(tagLabelText).isEqualTo("(Bar)");

		// Click on Clear Tag
		clearButton.click();
		tagLabelText = GuiActionRunner.execute(() -> tagLabel.text());
		assertThat(tagLabelText).isEqualTo(NewTodoDialog.TAG_LBL_NO_TAG_TEXT);
		selectedTag = GuiActionRunner.execute(() -> tagCombo.selectedItem());
		assertThat(selectedTag).isNull();
	}

	@Test
	@GUITest
	public void testInsertTodo() {
		String todoText = "A new Foo todo";
		window.textBox("todoTextBox").setText(todoText);

		// add selected tags
		window.comboBox("tagComboBox").selectItem(0);
		window.comboBox("tagComboBox").selectItem(1);

		ArgumentCaptor<Todo> todoTaggedArg = ArgumentCaptor.forClass(Todo.class);

		window.button("confirmButton").click();
		verify(todoController).addTodo(todoTaggedArg.capture());

		assertThat(todoTaggedArg.getValue().getBody()).isEqualTo(todoText);
		assertThat(todoTaggedArg.getValue().getTagList()).containsAll(new ArrayList<Tag>(Arrays.asList(t1, t2)));

		String textBoxText = GuiActionRunner.execute(() -> window.textBox("todoTextBox").text());
		assertThat(textBoxText).isEmpty();
	}

	@Test
	@GUITest
	public void testCancelButtonAction() {
		window.button("cancelButton").click();
		verify(todoController).dispose(view);
	}

}
