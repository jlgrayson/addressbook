package de.rcpbuch.addressbook.editor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import de.rcpbuch.addressbook.entities.Address;

public class OpenAddressEditor extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {

		// Open address editors for all selected addresses
		ISelection selection = HandlerUtil.getCurrentSelection(event);

		List<Integer> adressIds = getAddressIds(selection);
		for (Integer id : adressIds) {
			try {
				HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().openEditor(new AddressIdEditorInput(id),
						AddressEditorPart.EDITOR_ID);
			} catch (PartInitException e) {
				throw new ExecutionException(e.getMessage());
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	private List<Integer> getAddressIds(ISelection selection) {
		List<Integer> ids = new ArrayList<Integer>();
		if (selection instanceof IStructuredSelection) {
			for (Iterator iterator = ((IStructuredSelection) selection).iterator(); iterator.hasNext();) {
				Object selectedObject = iterator.next();
				if (selectedObject instanceof Address) {
					ids.add(((Address) selectedObject).getId());
				}

			}
		}
		return ids;

	}

}
