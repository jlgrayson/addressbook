package com.example.addressbook.internal.address;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IViewLayout;

import com.example.addressbook.AddressBookConstants;

public class AddressPerspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		layout.addView(AddressBookConstants.ADDRESS_LIST_VIEW_ID, IPageLayout.LEFT, 0.3f, layout.getEditorArea());
		IViewLayout listViewLayout = layout.getViewLayout(AddressBookConstants.ADDRESS_LIST_VIEW_ID);
		listViewLayout.setCloseable(false);
	}
}
