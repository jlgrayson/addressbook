package de.rcpbuch.addressbook;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.finders.CommandFinder;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotList;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTableItem;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.junit.Before;
import org.junit.Test;

public class AddressBookTests {

	private final SWTWorkbenchBot bot = new SWTWorkbenchBot();

	/**
	 * Vor der Ausfuehrung jeder Testmethode: ggf. offene Fenster schließen,
	 * alle Editoren schliessen, aktuelle Perspektive zuruecksetzen,
	 * Standard-Perspektive aktivieren, diese auch zuruecksetzen
	 */
	@Before
	public void setup() {
		UIThreadRunnable.syncExec(new VoidResult() {
			public void run() {
				try {
					IWorkbench wb = PlatformUI.getWorkbench();
					IWorkbenchWindow ww = wb.getActiveWorkbenchWindow();
					Shell activeShell = Display.getDefault().getActiveShell();
					if (activeShell != ww.getShell()) {
						activeShell.close();
					}
					ww.getActivePage().closeAllEditors(false);
					ww.getActivePage().resetPerspective();
					wb.showPerspective(wb.getPerspectiveRegistry()
							.getDefaultPerspective(), ww);
					ww.getActivePage().resetPerspective();
				} catch (WorkbenchException e) {
					throw new RuntimeException(e);
				}
			}
		});
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

		assertNotNull("Angelegte Adresse nicht in Adressliste", bot
				.viewByTitle("Adressen").bot().table().getTableItem(
						"Otto Muster"));
	}

	@Test
	public void testAddresseEditieren() throws Exception {
		SWTBotTable table = bot.viewByTitle("Adressen").bot().table();
		table.select("Heike Winkler", "Marion Graf");
		table.contextMenu("Adresse öffnen").click();
		assertEquals("Zwei Editoren muessen goeffnet sein", 2, bot.editors()
				.size());

		SWTBotEditor editor = bot.activeEditor();
		assertEquals("Heike Winkler", editor.getTitle());
		assertFalse("Editor unbearbeitet nicht dirty", editor.isDirty());
		SWTBot editorContents = editor.bot();
		SWTBotText text = editorContents.textWithLabel("Name:");
		assertTrue("Standard-Eingabefokus Editor auf Namensfeld", text
				.isActive());
		text.setText("Heike Muster");
		editorContents.textWithLabel("Straße:").setText("Musterstrasse");
		editorContents.textWithLabel("PLZ/Ort:").setText("01234 Musterort");

		assertTrue("Editor nach Aenderung dirty", editor.isDirty());
		new CommandFinder().findCommand(equalTo("Speichern")).get(0).click();
		assertFalse("Editor nach dem Speichern nicht dirty", editor.isDirty());
		editor.close();
		assertNotNull("Aenderung nicht sichtbar", bot.viewByTitle("Adressen")
				.bot().table().getTableItem("Heike Muster"));
		bot.editorByTitle("Marion Graf").close();
	}

	@Test
	public void testVisitenkarten() throws Exception {
		bot.perspectiveByLabel("Visitenkarten").activate();
		assertTrue("Visitenkarten-View nicht aktiv", bot.viewByTitle(
				"Visitenkarten").isActive());
	}

}