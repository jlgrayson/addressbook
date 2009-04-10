package de.rcpbuch.addressbook;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.finders.CommandFinder;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.junit.Test;

public class AddressBookTests {

	private final SWTWorkbenchBot bot = new SWTWorkbenchBot();

	@Test
	public void testNew() throws Exception {
		bot.menu("File").menu("New").click();
		bot.shell("New").close();
	}

	@Test
	public void testPerspectiveSwitch() throws Exception {
		for (int i = 0; i < 10; i++) {
			bot.perspectiveByLabel("Visitenkarten").activate();
			bot.perspectiveByLabel("Adressen").activate();
		}
	}

	@Test
	public void testEditAddress() throws Exception {

		// slow down swtbot
		// System.setProperty("org.eclipse.swtbot.playback.delay", "100");

		SWTBotTable table = bot.viewByTitle("Adressen").bot().table();
		table.select("Heike Winkler", "Marion Graf");
		table.contextMenu("Adresse öffnen").click();

		assertEquals(2, bot.editors().size());

		SWTBotEditor editor = bot.activeEditor();
		assertEquals("Heike Winkler", editor.getTitle());
		assertFalse(editor.isDirty());
		SWTBot editorContents = editor.bot();
		SWTBotText text = editorContents.textWithLabel("Name:");
		assertTrue(text.isActive());
		text.setText("TESTNAME");
		editorContents.textWithLabel("Straße:").setText("Street");
		editorContents.textWithLabel("PLZ/Ort:").setText("123");

		assertTrue(editor.isDirty());
		// editor.save();

		new CommandFinder().findCommand(equalTo("Save")).get(0).click();

		assertFalse(editor.isDirty());
		editor.close();

		bot.viewByTitle("Adressen").bot().table().getTableItem("TESTNAME");

		bot.editorByTitle("Marion Graf").close();

	}

}
