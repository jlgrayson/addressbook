package de.rcpbuch.addressbook.data.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import de.rcpbuch.addressbook.data.Address;
import de.rcpbuch.addressbook.data.IAddressChangeListener;
import de.rcpbuch.addressbook.data.IAddressService;

public class RandomDataAddressService implements IAddressService {

	private final List<Address> addresses;
	private final LinkedHashSet<IAddressChangeListener> addressChangeListeners = new LinkedHashSet<IAddressChangeListener>();

	public RandomDataAddressService() {
		addresses = new ArrayList<Address>();
		RandomData rd = new RandomData(1);
		for (int i = 1; i <= 50; i++) {
			addresses.add(new Address(i, rd.somePersonName(), rd.someStreet(), rd.someZipCode(), rd.someCity()));
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
