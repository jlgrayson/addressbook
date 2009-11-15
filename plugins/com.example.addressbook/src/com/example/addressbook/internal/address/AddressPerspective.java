package com.example.addressbook.internal.address;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IViewLayout;

import com.example.addressbook.AddressBookConstants;

/*
 * Initial page layout for "Addresses" perspective.
 */
public class AddressPerspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		// Add the address list view to the perspective (left from the editor
		// area with 30% of the remaining width)
		layout.addView(AddressBookConstants.ADDRESS_LIST_VIEW_ID, IPageLayout.LEFT, 0.3f, layout.getEditorArea());
		// Do not allow the user to close the view
		IViewLayout listViewLayout = layout.getViewLayout(AddressBookConstants.ADDRESS_LIST_VIEW_ID);
		listViewLayout.setCloseable(false);
	}
}
