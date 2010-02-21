package com.example.addressbook.entities;

/**
 * Entity object to represent an address.
 */
public class Address {

	private Integer id;
	private String name, street, zip, city;
	private Country country;
	private String email;

	public Address(Address adr) {
		this.id = adr.id;
		this.name = adr.name;
		this.street = adr.street;
		this.zip = adr.zip;
		this.city = adr.city;
		this.country = adr.country;
		this.email = adr.email;
	}

	public Address() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format(
				"Address [id=%s, name=%s, street=%s, zip=%s, country=%s, city=%s, email=%s]", id, name, street, zip, //$NON-NLS-1$
				country, city, email);
	}

}