package com.riccardomalavolti.apps.todolist.view;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.*;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;

import com.riccardomalavolti.apps.todolist.controller.TodoController;
import com.riccardomalavolti.apps.todolist.model.Tag;


@RunWith(GUITestRunner.class)
public class NewTagDialogTest extends AssertJSwingJUnitTestCase{

	private DialogFixture window;
	private NewTagDialog view;
	
	private TodoController todoController;
	
	@Override
	public void onSetUp() {
		todoController = mock(TodoController.class);
		GuiActionRunner.execute(() -> {
			view = new NewTagDialog(todoController);
			return view;
		});
		window = new DialogFixture(robot(), view);
		window.show();
		
		robot().waitForIdle();
	}
	
	@Override
	public void onTearDown() {		
		super.onTearDown();
		// https://github.com/joel-costigliola/assertj-swing/issues/157
		if (window != null)
            window.cleanUp();
		GuiActionRunner.execute(() -> view.dispose());        
	}

	@Test @GUITest
	public void testInitialState() {		
		assertNotNull(window.label(JLabelMatcher.withText("Insert tag name")));
		assertTrue(window.textBox("tagTextField").isEnabled());
		String tagTextField = GuiActionRunner.execute(() -> window.textBox("tagTextField").text());
		assertThat(tagTextField).isEmpty();

		
		JPanelFixture buttonPanel = window.panel("buttonPanel");
		
		assertThat(buttonPanel.button("insertButton").isEnabled()).isFalse();
		assertThat(buttonPanel.button("cancelButton").isEnabled()).isTrue();
	}
	
	@Test @GUITest
	public void testInsertTag() {
		String TAG_TEXT = "Foo";
		window.textBox("tagTextField").setText(TAG_TEXT);
		
		Tag emptyTag = new Tag(TAG_TEXT);
		JButtonFixture insertButton = window.button("insertButton");
		insertButton.click();
		ArgumentCaptor<Tag> tagCaptor = ArgumentCaptor.forClass(Tag.class);
		
		verify(todoController).addTag(tagCaptor.capture());
		assertThat(tagCaptor.getValue()).isEqualTo(emptyTag);
		String tagTextField = GuiActionRunner.execute(() -> window.textBox("tagTextField").text());
		assertThat(tagTextField).isEqualTo("");
	}
	
	@Test @GUITest
	public void testWritingAndCancellingText() {
		String TAG_TEXT = "Foo";
		JTextComponentFixture tagTextFixture = window.textBox("tagTextField");
		JButtonFixture insertButton = window.button("insertButton");
		
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
		JButtonFixture cancel = window.button("cancelButton");
		cancel.click();
		
		assertThat(view.isVisible()).isFalse();
	}

}