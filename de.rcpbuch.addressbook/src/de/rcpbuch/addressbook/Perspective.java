package de.rcpbuch.addressbook;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		layout.addView(AdressListViewPart.VIEW_ID, IPageLayout.TOP, 0.4f, layout.getEditorArea());
		layout.addView(SwtTestsViewPart.VIEW_ID, IPageLayout.LEFT, 0.5f, layout.getEditorArea());
	}
}
