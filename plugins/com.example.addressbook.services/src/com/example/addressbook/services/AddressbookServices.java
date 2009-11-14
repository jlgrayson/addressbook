package com.example.addressbook.services;

import com.example.addressbook.services.internal.RandomDataAddressService;

/**
 * Singleton to obtain an IAddressService instance. A singleton is used here
 * only for the sake of simplicity, using OSGi services or Dependency injection
 * would be more suitable in real applications.
 */
public class AddressbookServices {

	private static IAddressService addressService = new RandomDataAddressService();

	public static IAddressService getAddressService() {
		return addressService;
	}

}