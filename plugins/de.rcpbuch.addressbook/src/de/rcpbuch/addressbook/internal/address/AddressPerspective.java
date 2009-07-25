package de.rcpbuch.addressbook.internal.address;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import de.rcpbuch.addressbook.AddressbookConstants;

public class AddressPerspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		layout.addView(AddressbookConstants.ADDRESS_LIST_VIEW_ID, IPageLayout.TOP, 0.4f, layout.getEditorArea());
	}
}
