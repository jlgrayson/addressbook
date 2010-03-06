package com.example.addressbook;

import org.eclipse.osgi.util.NLS;

public class AddressBookMessages extends NLS {

	private static final String BUNDLE_NAME = "OSGI-INF.l10n.bundle"; //$NON-NLS-1$

	public static String Address_Book;
	public static String Address;
	public static String Addresses;
	public static String Name;
	public static String Street;
	public static String Zip;
	public static String City;
	public static String Country;
	public static String Email;

	// General purpose: String displayed between form labels for combined fields
	public static String Field_Separator;
	// General purpose: String displayed after all form labels
	public static String Field_Mark;
	// General purpose: Title for validation error dialogs
	public static String ValidationError;
	// General purpose: Validation error message
	public static String SaveNotAllowedBecauseOfValidationError;

	public static String LoadAddresses;
	public static String RefreshAddressList;

	public static String File;
	public static String Edit;
	public static String Window;
	public static String Help;
	public static String Progress;
	public static String Open;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, AddressBookMessages.class);
	}

	private AddressBookMessages() {
	}
}
