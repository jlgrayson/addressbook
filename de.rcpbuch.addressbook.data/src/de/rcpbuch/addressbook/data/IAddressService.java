package de.rcpbuch.addressbook.data;

import java.util.List;


public interface IAddressService {

	public List<Address> getAllAddresses();

	public String[] getAllCities();

	public List<Country> getAllCountries();

	public void deleteAddress(int id);

	public void addAddressChangeListener(IAddressChangeListener listener);

	public void removeAddressChangeListener(IAddressChangeListener listener);

}