package com.example.addressbook.email.internal;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.program.Program;
import org.eclipse.ui.handlers.HandlerUtil;

import com.example.addressbook.entities.Address;

import de.ralfebert.rcputils.selection.SelectionUtils;

public class EMailHandler extends AbstractHandler {

	private static final String SCHEME_MAILTO = "mailto:"; //$NON-NLS-1$

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		for (Address address : SelectionUtils.getIterable(HandlerUtil.getCurrentSelection(event), Address.class)) {
			Program.launch(SCHEME_MAILTO + address.getEmail());
		}

		return null;
	}

}
