package com.example.addressbook;

import de.ralfebert.singlesource.NLS;

public class AddressBookMessages {

	private static final String BUNDLE_NAME = "com.example.addressbook.messages"; //$NON-NLS-1$

	public String Address_Book;
	public String Address;
	public String Name;
	public String Street;
	public String Zip;
	public String City;
	public String Country;
	public String Email;

	// General purpose: String displayed between form labels for combined fields
	public String Field_Separator;
	// General purpose: String displayed after all form labels
	public String Field_Mark;
	// General purpose: Title for validation error dialogs
	public String ValidationError;
	// General purpose: Validation error message
	public String SaveNotAllowedBecauseOfValidationError;

	public String LoadAddresses;
	public String RefreshAddressList;

	public static AddressBookMessages get() {
		return NLS.get(BUNDLE_NAME, AddressBookMessages.class);
	}

	private AddressBookMessages() {
	}
}
