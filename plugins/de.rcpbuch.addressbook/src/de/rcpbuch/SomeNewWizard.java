package de.rcpbuch;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import de.rcpbuch.addressbook.entities.Address;
import de.rcpbuch.addressbook.services.AddressbookServices;

public class SomeNewWizard extends Wizard implements INewWizard {

	private FreeTextWizardPage textWizardPage;

	public SomeNewWizard() {
		textWizardPage = new FreeTextWizardPage();
		addPage(textWizardPage);
	}

	@Override
	public boolean performFinish() {
		String address = textWizardPage.getText();

		// This is way too simple!
		String[] parts = StringUtils.split(address, "\r\n");
		String name = parts.length >= 1 ? parts[0] : "";
		String street = parts.length >= 2 ? parts[1] : "";
		String zipCity = parts.length >= 3 ? parts[2] : "";
		String[] zipCityParts = StringUtils.split(zipCity, ' ');
		String zip = zipCityParts.length >= 1 ? zipCityParts[0] : "";
		String city = zipCityParts.length >= 2 ? zipCityParts[1] : "";

		AddressbookServices.getAddressService().saveAddress(new Address(null, name, street, zip, city, null));

		return true;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

}
