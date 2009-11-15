package com.example.addressbook.editing;

import org.eclipse.osgi.util.NLS;

public class AddressEditorMessages extends NLS {

	private static final String BUNDLE_NAME = "com.example.addressbook.editing.messages"; //$NON-NLS-1$

	public static String ZipValidator_Message;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, AddressEditorMessages.class);
	}

}