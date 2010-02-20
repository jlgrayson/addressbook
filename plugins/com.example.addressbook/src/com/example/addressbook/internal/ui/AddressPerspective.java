package com.example.addressbook.internal.ui;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IViewLayout;

import com.example.addressbook.AddressBook;

/*
 * Initial page layout for "Addresses" perspective.
 */
public class AddressPerspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		// Add the address list view to the perspective (left from the editor
		// area with 30% of the remaining width)
		layout.addView(AddressBook.VIEW_ADDRESS_LIST, IPageLayout.LEFT, 0.3f, layout.getEditorArea());
		// Do not allow the user to close the view
		IViewLayout listViewLayout = layout.getViewLayout(AddressBook.VIEW_ADDRESS_LIST);
		listViewLayout.setCloseable(false);
	}
}
