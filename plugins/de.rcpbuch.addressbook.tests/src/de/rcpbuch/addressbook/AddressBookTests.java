package de.rcpbuch.addressbook;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.finders.CommandFinder;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.junit.Before;
import org.junit.Test;

public class AddressBookTests {

	private final SWTWorkbenchBot bot = new SWTWorkbenchBot();

	@Before
	public void setup() {
		UIThreadRunnable.syncExec(new VoidResult() {
			public void run() {
				resetWorkbench();
			}
		});
	}

	/**
	 * Ggf. offene Fenster schließen, alle Editoren schliessen, aktuelle
	 * Perspektive zuruecksetzen, Standard-Perspektive aktivieren, diese auch
	 * zuruecksetzen
	 */
	private void resetWorkbench() {
		try {
			IWorkbench workbench = PlatformUI.getWorkbench();
			IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
			IWorkbenchPage page = workbenchWindow.getActivePage();
			Shell activeShell = Display.getCurrent().getActiveShell();
			if (activeShell != workbenchWindow.getShell()) {
				activeShell.close();
			}
			page.closeAllEditors(false);
			page.resetPerspective();
			workbench.showPerspective(workbench.getPerspectiveRegistry().getDefaultPerspective(), workbenchWindow);
			page.resetPerspective();
		} catch (WorkbenchException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testAdresseAnlegen() throws Exception {
		bot.menu("Datei").menu("Neu").click();
		bot.shell("Neu");
		SWTBotTree tree = bot.tree();
		tree.select("Neue Adresse");
		bot.button("Weiter >").click();
		bot.text().setText("Otto Muster\nMusterstr. 12\n01234 Musterhausen");
		bot.button("Fertig stellen").click();

		assertNotNull("Angelegte Adresse nicht in Adressliste", bot.viewByTitle("Adressen").bot().table().getTableItem(
				"Otto Muster"));
	}

	@Test
	public void testAdresseEditieren() throws Exception {
		SWTBotTable table = bot.viewByTitle("Adressen").bot().table();
		table.select("Heike Winkler", "Marion Graf");
		table.contextMenu("Adresse öffnen").click();
		assertEquals("Zwei Editoren goeffnet", 2, bot.editors().size());

		SWTBotEditor editor = bot.activeEditor();
		assertEquals("Heike Winkler", editor.getTitle());
		assertFalse("Editor ohne aenderung -> nicht dirty", editor.isDirty());
		SWTBot editorBot = editor.bot();
		SWTBotText text = editorBot.textWithLabel("Name:");
		assertTrue("Eingabefokus auf Namensfeld", text.isActive());
		text.setText("Heike Muster");
		editorBot.textWithLabel("Straße:").setText("Musterstrasse");
		editorBot.textWithLabel("PLZ/Ort:").setText("01234 Musterort");

		assertTrue("Editor nach Aenderung dirty", editor.isDirty());
		new CommandFinder().findCommand(equalTo("Speichern")).get(0).click();
		assertFalse("Editor nach dem Speichern nicht dirty", editor.isDirty());
		editor.close();
		assertNotNull("Aenderung in Adressliste sichtbar", bot.viewByTitle("Adressen").bot().table().getTableItem(
				"Heike Muster"));
		bot.editorByTitle("Marion Graf").close();
	}

}