package de.ralfebert.rcpsnippets.internal.snippet01tableproperties;

import java.util.Date;

public class City {

	private String name;
	private Date foundingDate;
	private final CityStats stats;

	public City(String name, Date foundingYear, CityStats stats) {
		super();
		this.name = name;
		this.foundingDate = foundingYear;
		this.stats = stats;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getFoundingDate() {
		return foundingDate;
	}

	public void setFoundingDate(Date foundingYear) {
		this.foundingDate = foundingYear;
	}

	public CityStats getStats() {
		return stats;
	}

}