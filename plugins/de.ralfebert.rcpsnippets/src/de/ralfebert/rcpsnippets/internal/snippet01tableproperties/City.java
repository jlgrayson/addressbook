package de.ralfebert.rcpsnippets.internal.snippet01tableproperties;

import java.util.Date;

public class City {

	private String name;
	private Date foundingYear;
	private final CityStats stats;

	public City(String name, Date foundingYear, CityStats stats) {
		super();
		this.name = name;
		this.foundingYear = foundingYear;
		this.stats = stats;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getFoundingYear() {
		return foundingYear;
	}

	public void setFoundingYear(Date foundingYear) {
		this.foundingYear = foundingYear;
	}

	public CityStats getStats() {
		return stats;
	}

}