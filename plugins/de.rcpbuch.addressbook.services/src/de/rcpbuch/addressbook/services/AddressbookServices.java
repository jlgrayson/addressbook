package de.rcpbuch.addressbook.services;

import de.rcpbuch.addressbook.services.internal.RandomDataAddressService;

public class AddressbookServices {

	private static IAddressService addressService = new RandomDataAddressService();

	public static IAddressService getAddressService() {
		return addressService;
	}
}