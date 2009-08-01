package de.ralfebert.rcpsnippets.internal;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import de.ralfebert.rcpsnippets.RcpSnippetsConstants;

public class SnippetsPerspectiveFactory implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		IFolderLayout snippetsFolder = layout.createFolder("snippets", IPageLayout.TOP, 0.35f, layout.getEditorArea());
		snippetsFolder.addView(RcpSnippetsConstants.SNIPPET_01_TABLE_PROPERTIES_VIEW_ID);
	}

}
