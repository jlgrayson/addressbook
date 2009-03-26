package de.rcpbuch.addressbook.editor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

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
		return "Adresse " + id;
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public String getToolTipText() {
		return getName();
	}

	@SuppressWarnings("unchecked")
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