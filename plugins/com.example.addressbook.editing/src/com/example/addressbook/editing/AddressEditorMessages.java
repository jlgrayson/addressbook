package com.example.addressbook.editing;

import org.eclipse.osgi.util.NLS;

public class AddressEditorMessages extends NLS {

	private static final String BUNDLE_NAME = "OSGI-INF.l10n.bundle"; //$NON-NLS-1$

	public static String Address;
	public static String ZipValidator_Message;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, AddressEditorMessages.class);
	}

}