package com.example.addressbook;

import org.eclipse.osgi.util.NLS;

public class AddressBookMessages extends NLS {

	private static final String BUNDLE_NAME = "com.example.addressbook.messages"; //$NON-NLS-1$

	public static String Address_Book;
	public static String Address;
	public static String Name;
	public static String Street;
	public static String Zip;
	public static String City;
	public static String Country;
	// String displayed between form labels for combined fields
	public static String Field_Separator;
	// String displayed after all form labels
	public static String Field_Mark;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, AddressBookMessages.class);
	}

	private AddressBookMessages() {
	}
}
