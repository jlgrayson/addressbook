package com.example.addressbook.services;

import com.example.addressbook.services.internal.RandomDataAddressService;

/**
 * Singleton-Klasse zur Bereitstellung von IAddressService. Ein Singleton wird
 * hier nur der Einfachheit wegen zu Uebungszwecken angewendet, in realen
 * Anwendungen sollte Dependency Injection verwendet werden.
 * 
 * @author Ralf Ebert
 */
public class AddressbookServices {

	private static IAddressService addressService = new RandomDataAddressService();

	public static IAddressService getAddressService() {
		return addressService;
	}

}