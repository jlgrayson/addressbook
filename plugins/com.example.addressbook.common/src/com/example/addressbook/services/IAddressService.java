package com.example.addressbook.services;

import java.util.Collection;
import java.util.List;

import com.example.addressbook.entities.Address;
import com.example.addressbook.entities.Country;

public interface IAddressService {

	public List<Address> getAllAddresses();

	public Address getAddress(int id);

	public Address saveAddress(Address changedOrNewAddress);

	public void deleteAddress(int id);

	public Collection<Country> getAllCountries();

	public String[] getAllCities();

	public void addAddressChangeListener(IAddressChangeListener listener);

	public void removeAddressChangeListener(IAddressChangeListener listener);

}