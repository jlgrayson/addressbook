package com.example.addressbook.internal.address.list;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.example.addressbook.entities.Address;
import com.example.addressbook.services.IAddressService;


public class AddressDeleteHandler extends AbstractHandler {

	private IAddressService addressService;

	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			Iterator<?> it = ((IStructuredSelection) selection).iterator();
			while (it.hasNext()) {
				Object obj = it.next();
				if (obj instanceof Address) {
					addressService.deleteAddress(((Address) obj).getId());
				}
			}
		}
		return null;
	}

	public void setAddressService(IAddressService addressService) {
		this.addressService = addressService;
	}

}