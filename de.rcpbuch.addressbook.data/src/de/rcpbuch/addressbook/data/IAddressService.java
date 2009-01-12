package de.rcpbuch.addressbook.data;

import java.util.List;

public interface IAddressService {

	public List<Address> getAllAddresses();

	public String[] getAllCities();

}