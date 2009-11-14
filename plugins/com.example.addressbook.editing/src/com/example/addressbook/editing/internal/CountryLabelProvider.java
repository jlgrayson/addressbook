package com.example.addressbook.editing.internal;

import org.eclipse.jface.viewers.LabelProvider;

import com.example.addressbook.entities.Country;

final class CountryLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {
		return ((Country) element).getName();
	}

}