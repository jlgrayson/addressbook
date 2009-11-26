package com.example.addressbook.services.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.example.addressbook.entities.Address;
import com.example.addressbook.entities.Country;
import com.example.addressbook.services.IAddressChangeListener;
import com.example.addressbook.services.IAddressService;

public class RandomDataAddressService implements IAddressService {

	private final AtomicInteger idSequence = new AtomicInteger(0);
	private final List<Address> addresses;
	private final LinkedHashSet<IAddressChangeListener> addressChangeListeners = new LinkedHashSet<IAddressChangeListener>();
	private final Set<Country> countries;

	public RandomDataAddressService() {

		countries = new HashSet<Country>();
		for (String countryName : RandomData.COUNTRIES) {
			countries.add(new Country(countryName));
		}

		addresses = new ArrayList<Address>();
		RandomData rd = new RandomData(1);
		for (int i = 1; i <= 50; i++) {
			addresses.add(new Address(idSequence.incrementAndGet(), rd.somePersonName(), rd.someStreet(), rd
					.someZipCode(), rd.someCity(), rd.someElement(countries)));
			rd.newData();
		}

	}

	public List<Address> getAllAddresses() {
		simulateSlowNetworkConnection();
		List<Address> list = new ArrayList<Address>();
		for (Address address : addresses) {
			list.add(new Address(address));
		}
		return list;
	}

	private void simulateSlowNetworkConnection() {
		try {
			Thread.sleep(new RandomData().someNumber(1000, 2000));
		} catch (InterruptedException e) {
			// ignore
		}
	}

	public String[] getAllCities() {
		return RandomData.CITIES;
	}

	public Collection<Country> getAllCountries() {
		return Collections.unmodifiableSet(countries);
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
					changedOrNewAddress.getStreet(), changedOrNewAddress.getZip(), changedOrNewAddress.getCity(),
					changedOrNewAddress.getCountry());
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
					address.setZip(changedOrNewAddress.getZip());
					address.setCity(changedOrNewAddress.getCity());
					address.setCountry(changedOrNewAddress.getCountry());
					fireAddressChange();
					return getAddress(address.getId());
				}
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
