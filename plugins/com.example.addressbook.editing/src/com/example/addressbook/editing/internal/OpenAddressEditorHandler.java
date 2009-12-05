package com.example.addressbook.editing.internal;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import com.example.addressbook.editing.AddressEditorConstants;
import com.example.addressbook.editing.AddressIdEditorInput;
import com.example.addressbook.entities.Address;

/**
 * Open address editors for all selected addresses.
 */
public class OpenAddressEditorHandler extends AbstractHandler {

	@SuppressWarnings("unchecked")
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		IWorkbenchPage activePage = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();

		if (selection instanceof IStructuredSelection) {
			for (Iterator i = ((IStructuredSelection) selection).iterator(); i.hasNext();) {
				Object selectedObj = i.next();
				if (selectedObj instanceof Address) {
					Address address = (Address) selectedObj;
					try {
						activePage.openEditor(new AddressIdEditorInput(address.getId()),
								AddressEditorConstants.ADDRESS_EDITOR_ID);
					} catch (PartInitException e) {
						throw new ExecutionException(e.getMessage(), e);
					}
				}
			}
		}

		return null;
	}

}