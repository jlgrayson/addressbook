package de.rcpbuch.addressbook.entities;

public class Country {

	private final String name;
	private final int addressCount;

	public int getAddressCount() {
		return addressCount;
	}

	public Country(String name, int addressCount) {
		super();
		this.name = name;
		this.addressCount = addressCount;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Country[" + name + "]";
	}
}
