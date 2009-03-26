package de.rcpbuch.addressbook.services.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import de.rcpbuch.addressbook.entities.Address;
import de.rcpbuch.addressbook.entities.Country;
import de.rcpbuch.addressbook.services.IAddressChangeListener;
import de.rcpbuch.addressbook.services.IAddressService;

public class RandomDataAddressService implements IAddressService {

	private final AtomicInteger idSequence = new AtomicInteger(0);
	private final List<Address> addresses;
	private final LinkedHashSet<IAddressChangeListener> addressChangeListeners = new LinkedHashSet<IAddressChangeListener>();

	public RandomDataAddressService() {
		addresses = new ArrayList<Address>();
		RandomData rd = new RandomData(1);
		for (int i = 1; i <= 50; i++) {
			addresses.add(new Address(idSequence.incrementAndGet(), rd.somePersonName(), rd.someStreet(), rd
					.someZipCode(), rd.someCity()));
			rd.newData();
		}
	}

	public List<Address> getAllAddresses() {
		ArrayList<Address> list = new ArrayList<Address>();
		for (Address address : addresses) {
			list.add(new Address(address));
		}
		return list;
	}

	public String[] getAllCities() {
		return RandomData.CITIES;
	}

	public List<Country> getAllCountries() {
		RandomData rd = new RandomData(1);
		List<Country> countries = new ArrayList<Country>();
		for (String countryName : RandomData.COUNTRIES) {
			countries.add(new Country(countryName, rd.someNumber(1, 40)));
			rd.newData();
		}

		return countries;
	}

	public void deleteAddress(int id) {
		for (Iterator<Address> i = addresses.iterator(); i.hasNext();) {
			Address address = i.next();
			if (address.getId() == id) {
				i.remove();
				fireAddressChange();
				return;
			}
		}
	}

	public Address getAddress(int id) {
		for (Iterator<Address> i = addresses.iterator(); i.hasNext();) {
			Address address = i.next();
			if (address.getId() == id) {
				return new Address(address);
			}
		}
		return null;
	}

	public Address saveAddress(Address changedOrNewAddress) {
		if (changedOrNewAddress.getId() == null) {
			// create new address
			Address createdAdr = new Address(idSequence.incrementAndGet(), changedOrNewAddress.getName(),
					changedOrNewAddress.getStreet(), changedOrNewAddress.getZip(), changedOrNewAddress.getCity());
			addresses.add(createdAdr);
			fireAddressChange();
			return new Address(createdAdr);
		} else {
			// change existing address
			for (Iterator<Address> i = addresses.iterator(); i.hasNext();) {
				Address address = i.next();
				if (address.getId() == changedOrNewAddress.getId()) {
					address.setName(changedOrNewAddress.getName());
					address.setStreet(changedOrNewAddress.getStreet());
					address.setZip(changedOrNewAddress.getCity());
					address.setCity(changedOrNewAddress.getCity());
				}
				fireAddressChange();
				return getAddress(address.getId());
			}
			throw new IllegalArgumentException("Address " + changedOrNewAddress.getId() + " not found!");
		}
	}

	private void fireAddressChange() {
		for (IAddressChangeListener changeListener : addressChangeListeners) {
			changeListener.addressesChanged();
		}
	}

	public void addAddressChangeListener(IAddressChangeListener listener) {
		this.addressChangeListeners.add(listener);
	}

	public void removeAddressChangeListener(IAddressChangeListener listener) {
		this.addressChangeListeners.remove(listener);
	}

}
