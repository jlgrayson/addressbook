package com.example.addressbook.uitests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.finders.CommandFinder;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import com.example.addressbook.editing.AddressBookEditing;
import com.example.addressbook.editing.AddressIdEditorInput;

public class AddressBookTests {

    private final SWTWorkbenchBot bot = new SWTWorkbenchBot();

    @Before
    public void setup() {
        // Zugriffe auf das UI / SWT-Komponenten muss im
        // UI-Thread erfolgen
        UIThreadRunnable.syncExec(new VoidResult() {
            public void run() {
                resetWorkbench();
            }
        });
    }

    /**
     * Ggf. offene Fenster schließen, alle Editoren schliessen, aktuelle
     * Perspektive zuruecksetzen, Standard-Perspektive aktivieren, diese auch
     * zurücksetzen
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
            String defaultPerspectiveId = workbench.getPerspectiveRegistry().getDefaultPerspective();
            workbench.showPerspective(defaultPerspectiveId, workbenchWindow);
            page.resetPerspective();
        } catch (WorkbenchException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testOpenAddress() throws Exception {
        final SWTBotTable table = bot.viewByTitle("Addresses").bot().table();
        
        bot.waitUntil(new DefaultCondition() {
			
			@Override
			public boolean test() throws Exception {
				return table.rowCount()>0;
			}
			
			@Override
			public String getFailureMessage() {
				return "Empty table";
			}
		});
        
        table.select("Heike Winkler", "Marion Graf");
        table.contextMenu("&Open").click();
        assertEquals("Two editors opened", 2, bot.editors().size());
    }
    
    @Test
    public void testEditAddress() throws Exception {
    	UIThreadRunnable.syncExec(new VoidResult() {

			@Override
			public void run() {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					page.openEditor(new AddressIdEditorInput(5), AddressBookEditing.EDITOR_ADDRESS);
				} catch (PartInitException e) {
					fail(e.getMessage());
				}
			}
    		
    	});
    	
        SWTBotEditor editor = bot.activeEditor();
        assertEquals("Matthias Schmitt", editor.getTitle());
        assertFalse("Editor without change -> not dirty", editor.isDirty());
        SWTBot editorBot = editor.bot();
        SWTBotText nameText = editorBot.textWithLabel("Name:");
        assertTrue("Name field focused", nameText.isActive());
        nameText.setText("Otto Mustermann");
        editorBot.textWithLabel("Street:").setText("Musterstrasse");
        editorBot.textWithLabel("Zip, City:").setText("01234 Musterort");

        assertTrue("Editor after change -> dirty", editor.isDirty());
        new CommandFinder().findCommand(CoreMatchers.equalTo("Save")).get(0).click();
        assertFalse("Editor after save -> not dirty", editor.isDirty());
        editor.close();
    }

}
