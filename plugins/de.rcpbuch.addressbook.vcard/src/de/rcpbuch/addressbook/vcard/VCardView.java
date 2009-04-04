package de.rcpbuch.addressbook.vcard;

import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import de.rcpbuch.addressbook.entities.Address;
import de.rcpbuch.addressbook.services.AddressbookServices;
import de.rcpbuch.addressbook.services.IAddressChangeListener;

public class VCardView extends ViewPart {

	public static final String VIEW_ID = VCardView.class.getName();

	private Composite vcardContainer;

	private final IAddressChangeListener ADDRESS_CHANGE_LISTENER = new IAddressChangeListener() {

		public void addressesChanged() {
			updateUi();
		}

	};

	@Override
	public void createPartControl(Composite parent) {

		this.vcardContainer = parent;
		AddressbookServices.getAddressService().addAddressChangeListener(ADDRESS_CHANGE_LISTENER);
		updateUi();

	}

	@Override
	public void dispose() {
		super.dispose();
		AddressbookServices.getAddressService().removeAddressChangeListener(ADDRESS_CHANGE_LISTENER);
	}

	private void updateUi() {

		RowLayout layout = new RowLayout();
		layout.spacing = 8;
		layout.marginLeft = 8;
		layout.marginRight = 8;
		layout.marginTop = 8;
		layout.marginBottom = 8;
		vcardContainer.setLayout(layout);

		while (vcardContainer.getChildren().length > 0) {
			vcardContainer.getChildren()[0].dispose();
		}

		for (Address address : AddressbookServices.getAddressService().getAllAddresses()) {
			new VCardComposite(vcardContainer, address);
		}

	}

	@Override
	public void setFocus() {

	}

}
