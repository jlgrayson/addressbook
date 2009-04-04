package de.rcpbuch.addressbook;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import de.rcpbuch.addressbook.list.AddressListViewPart;

public class AddressPerspective implements IPerspectiveFactory {

	public static final String PERSPECTIVE_ID = AddressPerspective.class.getName();

	public void createInitialLayout(IPageLayout layout) {
		layout.addView(AddressListViewPart.VIEW_ID, IPageLayout.TOP, 0.4f, layout.getEditorArea());
	}
}
