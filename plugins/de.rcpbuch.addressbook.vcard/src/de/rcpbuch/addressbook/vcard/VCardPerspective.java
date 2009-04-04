package de.rcpbuch.addressbook.vcard;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class VCardPerspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.addStandaloneView(VCardView.VIEW_ID, false, IPageLayout.LEFT, 1.0f, layout.getEditorArea());
	}

}
