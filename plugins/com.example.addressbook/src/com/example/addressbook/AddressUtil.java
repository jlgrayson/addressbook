package com.example.addressbook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.example.addressbook.entities.Address;

/**
 * Helper methods for handling addresses.
 */
public class AddressUtil {

	/**
	 * Returns a list of all addresses contained in the given selection.
	 */
	public static final List<Address> getAddresses(ISelection selection) {
		if (selection.isEmpty())
			return Collections.emptyList();

		List<Address> addresses = new ArrayList<Address>();
		if (selection instanceof IStructuredSelection) {
			Iterator<?> iterator = ((IStructuredSelection) selection).iterator();
			while (iterator.hasNext()) {
				Object selectedObject = iterator.next();
				if (selectedObject instanceof Address) {
					addresses.add((Address) selectedObject);
				}

			}
		}

		return addresses;
	}

}
