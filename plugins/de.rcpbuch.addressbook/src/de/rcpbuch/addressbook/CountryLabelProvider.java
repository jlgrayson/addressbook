/**
 * 
 */
package de.rcpbuch.addressbook;

import org.eclipse.jface.viewers.LabelProvider;

import de.rcpbuch.addressbook.entities.Country;

final class CountryLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {
		Country countryElement = (Country) element;
		return countryElement.getName();
	}

}