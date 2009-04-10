package de.rcpbuch.addressbook.europe;

import de.rcpbuch.addressbook.localemanager.ILocaleHandler;

public class GermanLocaleHandler implements ILocaleHandler {

	public boolean validateZipCode(String zipCode) {
		return zipCode.matches("\\d{5}");
	}

}
