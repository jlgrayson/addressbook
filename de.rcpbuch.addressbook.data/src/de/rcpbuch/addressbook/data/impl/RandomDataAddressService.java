package de.rcpbuch.addressbook.data.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.rcpbuch.addressbook.data.Address;
import de.rcpbuch.addressbook.data.IAddressService;

public class RandomDataAddressService implements IAddressService {

	@Override
	public List<Address> getAllAddresses() {
		ArrayList<Address> addresses = new ArrayList<Address>();
		RandomData rd = new RandomData(1);
		for (int i = 1; i <= 50; i++) {
			addresses.add(new Address(rd.somePersonName(), rd.someStreet(), rd.someZipCode(), rd.someCity()));
			rd.newData();
		}
		return addresses;
	}

	@Override
	public String[] getAllCities() {
		return Arrays.copyOf(RandomData.CITIES, RandomData.CITIES.length);
	}

}
