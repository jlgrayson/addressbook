package com.example.addressbook.editing;

import de.ralfebert.singlesource.NLS;

public class AddressEditorMessages {

	private static final String BUNDLE_NAME = AddressBookEditing.PLUGIN_ID + ".messages"; //$NON-NLS-1$

	public String ZipValidator_Message;

	public static AddressEditorMessages get() {
		return NLS.get(BUNDLE_NAME, AddressEditorMessages.class);
	}

	private AddressEditorMessages() {
	}

}