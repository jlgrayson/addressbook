package de.rcpbuch.addressbook.internal.address.list;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import de.rcpbuch.addressbook.entities.Address;
import de.rcpbuch.addressbook.services.AddressbookServices;

public class AddressDeleteHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			Iterator<?> it = ((IStructuredSelection) selection).iterator();
			while (it.hasNext()) {
				Object obj = it.next();
				if (obj instanceof Address) {
					AddressbookServices.getAddressService().deleteAddress(((Address) obj).getId());
				}
			}
		}
		return null;
	}

}