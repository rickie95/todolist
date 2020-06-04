package com.riccardomalavolti.apps.todolist.view;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.swing.timing.Pause.pause;
import static org.assertj.swing.timing.Timeout.timeout;
import static org.mockito.Mockito.*;

import java.awt.Dialog;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.*;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.assertj.swing.timing.Condition;

import com.riccardomalavolti.apps.todolist.controller.TodoController;
import com.riccardomalavolti.apps.todolist.model.Tag;


@RunWith(GUITestRunner.class)
public class NewTagDialogTest extends AssertJSwingJUnitTestCase{

	private static final long TIMEOUT = 5000;
	private DialogFixture dialogFixture;
	private NewTagDialog dialog;
	
	private TodoController todoController;
	
	@BeforeClass
	public static void installViolationNotifier() {
		FailOnThreadViolationRepaintManager.install();
	}
	
	@Override
	public void onSetUp() {
		todoController = mock(TodoController.class);
		
		GuiActionRunner.execute(() -> {
			dialog = new NewTagDialog(todoController);
		});
		
		dialogFixture = new DialogFixture(robot(), dialog);
		dialog.setModalityType(Dialog.ModalityType.MODELESS);
		
		GuiActionRunner.execute(() -> dialog.showDialog());
		
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
	public void onTearDown() {		
		super.onTearDown();
		// https://github.com/joel-costigliola/assertj-swing/issues/157
		if (dialogFixture != null)
            dialogFixture.cleanUp();
		GuiActionRunner.execute(() -> dialog.dispose());        
	}

	@Test @GUITest
	public void testInitialState() {		
		assertNotNull(dialogFixture.label(JLabelMatcher.withText("Insert tag name")));
		assertTrue(dialogFixture.textBox("tagTextField").isEnabled());
		String tagTextField = GuiActionRunner.execute(() -> dialogFixture.textBox("tagTextField").text());
		assertThat(tagTextField).isEmpty();

		
		JPanelFixture buttonPanel = dialogFixture.panel("buttonPanel");
		
		assertThat(buttonPanel.button("insertButton").isEnabled()).isFalse();
		assertThat(buttonPanel.button("cancelButton").isEnabled()).isTrue();
	}
	
	@Test @GUITest
	public void testInsertTag() {
		String TAG_TEXT = "Foo";
		dialogFixture.textBox("tagTextField").setText(TAG_TEXT);
		
		Tag emptyTag = new Tag(TAG_TEXT);
		JButtonFixture insertButton = dialogFixture.button("insertButton");
		insertButton.click();
		ArgumentCaptor<Tag> tagCaptor = ArgumentCaptor.forClass(Tag.class);
		
		verify(todoController).addTag(tagCaptor.capture());
		assertThat(tagCaptor.getValue()).isEqualTo(emptyTag);
		String tagTextField = GuiActionRunner.execute(() -> dialogFixture.textBox("tagTextField").text());
		assertThat(tagTextField).isEqualTo("");
	}
	
	@Test @GUITest
	public void testWritingAndCancellingText() {
		String TAG_TEXT = "Foo";
		JTextComponentFixture tagTextFixture = dialogFixture.textBox("tagTextField");
		JButtonFixture insertButton = dialogFixture.button("insertButton");
		
		// As first, we test that tagTextFixture is empty and insertButton is disabled.
		String tagTextFieldText = GuiActionRunner.execute(() -> tagTextFixture.text());
		assertThat(tagTextFieldText).isEmpty();
		assertThat(insertButton.isEnabled()).isFalse();
		
		// Then we proceed to put in some text
		tagTextFixture.setText(TAG_TEXT);
		tagTextFieldText = GuiActionRunner.execute(() -> tagTextFixture.text());
		assertThat(tagTextFieldText).isEqualTo(TAG_TEXT);
		assertThat(insertButton.isEnabled()).isTrue();
		
		// We simulate a total erasing of text, insertButton should be disabled
		tagTextFixture.deleteText();
		tagTextFieldText = GuiActionRunner.execute(() -> tagTextFixture.text());
		assertThat(tagTextFieldText).isEmpty();
		assertThat(insertButton.isEnabled()).isFalse();
	}
	
	@Test @GUITest
	public void testDisposeOnCancelClick() {
		JButtonFixture cancel = dialogFixture.button("cancelButton");
		cancel.click();
		
		assertThat(dialog.isVisible()).isFalse();
	}

}
