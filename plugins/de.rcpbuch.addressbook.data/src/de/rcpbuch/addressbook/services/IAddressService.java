package de.rcpbuch.addressbook.services;

import java.util.List;

import de.rcpbuch.addressbook.entities.Address;
import de.rcpbuch.addressbook.entities.Country;


public interface IAddressService {

	public List<Address> getAllAddresses();

	public String[] getAllCities();

	public List<Country> getAllCountries();

	public void deleteAddress(int id);

	public void addAddressChangeListener(IAddressChangeListener listener);

	public void removeAddressChangeListener(IAddressChangeListener listener);

}