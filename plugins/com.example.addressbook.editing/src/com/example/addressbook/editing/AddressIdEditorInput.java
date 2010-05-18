package com.example.addressbook.editing;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.example.addressbook.AddressBookMessages;

public class AddressIdEditorInput implements IEditorInput {

	private final int id;

	public AddressIdEditorInput(int addressId) {
		this.id = addressId;
	}

	public boolean exists() {
		return true;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return AddressBookMessages.Address + " " + id; //$NON-NLS-1$
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public String getToolTipText() {
		return getName();
	}

	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class adapter) {
		return null;
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
		AddressIdEditorInput other = (AddressIdEditorInput) obj;
		if (id != other.id)
			return false;
		return true;
	}

}