package com.example.addressbook.internal.ui;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.riena.core.wire.InjectService;
import org.eclipse.ui.handlers.HandlerUtil;
import org.osgi.service.log.LogService;

import com.example.addressbook.entities.Address;
import com.example.addressbook.services.IAddressService;

import de.ralfebert.rcputils.selection.SelectionUtils;
import de.ralfebert.rcputils.wired.WiredHandler;

/**
 * Handler to delete selected addresses.
 */
public class AddressDeleteHandler extends WiredHandler {

	private IAddressService addressService;
	private LogService log;

	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		for (Address address : SelectionUtils.getIterable(selection, Address.class)) {
			log.log(LogService.LOG_INFO, "Deleting address: " + address); //$NON-NLS-1$
			addressService.deleteAddress(address.getId());
		}
		return null;
	}

	@Override
	public boolean isEnabled() {
		return super.isEnabled() && addressService != null;
	}

	@InjectService
	public void bindAddressService(IAddressService addressService) {
		this.addressService = addressService;
	}

	public void unbindAddressService(IAddressService addressService) {
		this.addressService = null;
	}

	@InjectService
	public void bindLog(LogService log) {
		this.log = log;
	}

	public void unbindLog(LogService log) {
		this.log = null;
	}

}