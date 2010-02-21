package com.example.addressbook.editing.internal;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import com.example.addressbook.editing.AddressBookEditing;
import com.example.addressbook.editing.AddressIdEditorInput;
import com.example.addressbook.entities.Address;

import de.ralfebert.rcputils.selection.SelectionUtils;

/**
 * Open address editors for all selected addresses.
 */
public class OpenAddressEditorHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		IWorkbenchPage activePage = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
		for (Address address : SelectionUtils.getIterable(selection, Address.class)) {
			try {
				activePage.openEditor(new AddressIdEditorInput(address.getId()), AddressBookEditing.EDITOR_ADDRESS);
			} catch (PartInitException e) {
				throw new ExecutionException(e.getMessage(), e);
			}
		}

		return null;
	}

}