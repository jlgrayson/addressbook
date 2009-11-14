package com.example.addressbook.internal.address.list;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.example.addressbook.AddressUtil;
import com.example.addressbook.entities.Address;
import com.example.addressbook.services.AddressbookServices;

/**
 * Handler to delete selected addresses.
 */
public class AddressDeleteHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		for (Address address : AddressUtil.getAddresses(selection)) {
			AddressbookServices.getAddressService().deleteAddress(address.getId());
		}
		return null;
	}

}