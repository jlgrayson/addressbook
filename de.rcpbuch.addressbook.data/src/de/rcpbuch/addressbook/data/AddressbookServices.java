package de.rcpbuch.addressbook.data;

import de.rcpbuch.addressbook.data.impl.RandomDataAddressService;

public class AddressbookServices {

	private static IAddressService addressService = new RandomDataAddressService();

	public static IAddressService getAddressService() {
		return addressService;
	}

}