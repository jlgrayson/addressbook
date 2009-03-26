package de.rcpbuch.addressbook.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import de.rcpbuch.addressbook.list.AddressListViewPart;

public class AddressListRefreshHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPart part = HandlerUtil.getActivePart(event);
		if (part instanceof AddressListViewPart) {
			((AddressListViewPart) part).updateUi();
		}
		return null;
	}

}
