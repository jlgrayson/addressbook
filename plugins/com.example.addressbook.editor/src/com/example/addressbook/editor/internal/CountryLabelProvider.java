/**
 * 
 */
package com.example.addressbook.editor.internal;

import org.eclipse.jface.viewers.LabelProvider;

import com.example.addressbook.entities.Country;


final class CountryLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {
		Country countryElement = (Country) element;
		return countryElement.getName();
	}

}