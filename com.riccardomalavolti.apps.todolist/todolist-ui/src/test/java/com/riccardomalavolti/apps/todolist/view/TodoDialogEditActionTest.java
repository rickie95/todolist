package com.riccardomalavolti.apps.todolist.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.timing.Pause.pause;
import static org.assertj.swing.timing.Timeout.timeout;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.Dialog;
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
import org.assertj.swing.timing.Condition;
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
public class TodoDialogEditActionTest extends AssertJSwingJUnitTestCase {

	private static final long TIMEOUT = 5000;
	private DialogFixture dialogFixture;
	private TodoDialog dialog;
	private DefaultComboBoxModel<Tag> comboBoxModel;
	private Tag t1, t2;
	private Todo todo;

	@Mock
	private TodoAction todoAction;
	@Mock
	private TodoController todoController;
	@Captor
	private ArgumentCaptor<ArrayList<Tag>> tagListCaptor;
	private String todoText;

	@BeforeClass
	public static void installViolationNotifier() {
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
		
		when(todoAction.getTodo()).thenReturn(todo);
		
		GuiActionRunner.execute(() -> {
			dialog = new TodoDialog(todoController, comboBoxModel, todoAction, TodoController.EDIT_DIALOG_TITLE);
		});

		dialogFixture = new DialogFixture(robot(), dialog);
		dialog.setModalityType(Dialog.ModalityType.MODELESS);
		
		GuiActionRunner.execute(() -> dialogFixture.show());
		
		robot().waitForIdle();
		
		pause(
				new Condition("get view focuses+valid+showing+active+visible") {
					@Override
					public boolean test() {
						return dialog.isFocused() && dialog.isValid() && dialog.isShowing() && dialog.isActive() && dialog.isVisible();
					}
				}, timeout(TIMEOUT));

	}


	@Override
	protected void onTearDown() {
		super.onTearDown();
		if (dialogFixture != null)
            dialogFixture.cleanUp();
		GuiActionRunner.execute(() -> dialog.dispose());  
	}

	@Test
	@GUITest
	public void testInitialState() {
		
		JTextComponentFixture textBox = dialogFixture.textBox("todoTextBox");
		JComboBoxFixture comboBox = dialogFixture.comboBox("tagComboBox");
		JButtonFixture clearButton = dialogFixture.button("clearButton");
		JButtonFixture confirmButton = dialogFixture.button("confirmButton");
		JButtonFixture cancelButton = dialogFixture.button("cancelButton");
		
		assertThat(dialogFixture.label("headingLabel").text()).isEqualTo(TodoController.EDIT_DIALOG_TITLE);
		assertThat(dialog.getTitle()).isEqualTo(TodoController.EDIT_DIALOG_TITLE);
		
		assertNotNull(dialogFixture.label(JLabelMatcher.withText(Tag.listToString(todo.getTagList()))));
		
		assertThat(textBox.text()).isEqualTo(todo.getBody());
		// Tag selection must be void
		assertThat(comboBox.selectedItem()).isNull();

		assertThat(clearButton.isEnabled()).isTrue();
		assertThat(confirmButton.isEnabled()).isTrue();
		assertThat(cancelButton.isEnabled()).isTrue();

	}
	
	@Test
	@GUITest
	public void testSelectingTagShouldUpdateTagLabel() {
		dialogFixture.comboBox("tagComboBox").selectItem(0);

		assertThat(dialogFixture.comboBox("tagComboBox").selectedItem()).isEqualTo("Bar");
		Set<Tag> tagList = GuiActionRunner.execute(() -> dialog.getTagList());
		assertThat(dialogFixture.label("tagLabel").text()).isEqualTo(Tag.listToString(new ArrayList<Tag>(tagList)));
	}

	@Test
	@GUITest
	public void testRemovingAllTagsFromATodoShouldBeAllowedIfThereAreTagSelected() {
		JButtonFixture clearButton = dialogFixture.button("clearButton");
		assertThat(clearButton.isEnabled()).isTrue();

		clearButton.click();

		assertThat(dialogFixture.label("tagLabel").text()).isEqualTo(TodoDialog.TAG_LBL_NO_TAG_TEXT);
	}

	@Test
	@GUITest
	public void testEditAndCommitUpdate() {
		String new_text = "Updated todo";
		JTextComponentFixture textBox = dialogFixture.textBox("todoTextBox");

		textBox.setText("");
		assertThat(dialogFixture.button("confirmButton").isEnabled()).isFalse();
		textBox.enterText(new_text);

		assertThat(dialogFixture.button("confirmButton").isEnabled()).isTrue();
		dialogFixture.button("confirmButton").click();
		
		assertThat(textBox.text()).isEqualTo(new_text);
		Set<Tag> tagList = GuiActionRunner.execute(() -> dialog.getTagList());
		verify(todoAction).sendToController(new_text, tagList);
	}

	@Test
	@GUITest
	public void testCancelButtonAction() {
		dialogFixture.button("cancelButton").click();
		verify(todoController).dispose(dialog);
	}
	
	

}
