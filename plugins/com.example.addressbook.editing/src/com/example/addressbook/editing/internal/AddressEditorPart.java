package com.example.addressbook.editing.internal;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

import com.example.addressbook.AddressBookMessages;
import com.example.addressbook.editing.AddressBookEditing;
import com.example.addressbook.editing.AddressIdEditorInput;
import com.example.addressbook.entities.Address;
import com.example.addressbook.entities.Country;
import com.example.addressbook.services.AddressbookServices;

/**
 * Editor part implementation for editing Address objects using the
 * AddressBookService.
 */
public class AddressEditorPart extends EditorPart {

	private Text txtName;
	private Text txtStreet;
	private Text txtZip;
	private Text txtCity;
	private ComboViewer cvCountry;

	private boolean dirty;

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
	}

	@Override
	public void createPartControl(Composite parent) {
		createUi(parent);
		loadModel();
		addDirtyOnChangeListeners();
	}

	private void createUi(Composite parent) {

		// Set help context for Dynamic Help
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, AddressBookEditing.HELP_EDIT);

		// Name
		Label lblName = new Label(parent, SWT.NONE);
		lblName.setText(AddressBookMessages.Name + AddressBookMessages.Field_Mark);

		txtName = new Text(parent, SWT.BORDER);

		// Street
		Label lblStreet = new Label(parent, SWT.NONE);
		lblStreet.setText(AddressBookMessages.Street + AddressBookMessages.Field_Mark);

		txtStreet = new Text(parent, SWT.BORDER);
		txtStreet.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		// Zip / City
		Label lblZipCity = new Label(parent, SWT.NONE);
		lblZipCity.setText(AddressBookMessages.Zip + AddressBookMessages.Field_Separator + AddressBookMessages.City
				+ AddressBookMessages.Field_Mark);

		txtZip = new Text(parent, SWT.BORDER);
		txtCity = new Text(parent, SWT.BORDER);

		ControlDecoration decoration = new ControlDecoration(txtCity, SWT.RIGHT | SWT.TOP);
		Image errorImage = FieldDecorationRegistry.getDefault().getFieldDecoration(
				FieldDecorationRegistry.DEC_CONTENT_PROPOSAL).getImage();
		decoration.setImage(errorImage);

		new AutoCompleteField(txtCity, new TextContentAdapter(), AddressbookServices.getAddressService().getAllCities());

		// Country
		Label lblCountry = new Label(parent, SWT.NONE);
		lblCountry.setText(AddressBookMessages.Country + AddressBookMessages.Field_Mark);

		cvCountry = new ComboViewer(parent, SWT.READ_ONLY);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).span(2, 1).applyTo(
				cvCountry.getCombo());
		cvCountry.setContentProvider(ArrayContentProvider.getInstance());
		cvCountry.setLabelProvider(new CountryLabelProvider());
		cvCountry.setInput(AddressbookServices.getAddressService().getAllCountries());

		// Layout
		GridLayoutFactory.fillDefaults().margins(5, 5).spacing(5, 3).numColumns(3).applyTo(parent);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).span(2, 1).applyTo(txtName);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).span(2, 1).applyTo(txtStreet);
		GridDataFactory.fillDefaults().hint(50, SWT.DEFAULT).applyTo(txtZip);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).applyTo(txtCity);

	}

	private void loadModel() {
		Address address = AddressbookServices.getAddressService().getAddress(getEditorInput().getId());
		txtName.setText(address.getName());
		txtStreet.setText(address.getStreet());
		txtZip.setText(address.getZip());
		txtCity.setText(address.getCity());
		cvCountry.setSelection(new StructuredSelection(address.getCountry()));
		setPartName(address.getName());
	}

	private void addDirtyOnChangeListeners() {
		ModifyListener modifyListener = new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}

		};

		txtName.addModifyListener(modifyListener);
		txtStreet.addModifyListener(modifyListener);
		txtZip.addModifyListener(modifyListener);
		txtCity.addModifyListener(modifyListener);

		ISelectionChangedListener changedListener = new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				setDirty(true);
			}
		};

		cvCountry.addSelectionChangedListener(changedListener);
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	protected void setDirty(boolean dirty) {
		this.dirty = dirty;
		firePropertyChange(PROP_DIRTY);
	}

	@Override
	public void doSave(IProgressMonitor monitor) {

		Address address = AddressbookServices.getAddressService().getAddress(getEditorInput().getId());
		address.setName(txtName.getText());
		address.setStreet(txtStreet.getText());
		address.setZip(txtZip.getText());
		address.setCity(txtCity.getText());
		IStructuredSelection selection = (IStructuredSelection) cvCountry.getSelection();
		address.setCountry((Country) selection.getFirstElement());

		AddressbookServices.getAddressService().saveAddress(address);

		loadModel();
		setDirty(false);
	}

	@Override
	public AddressIdEditorInput getEditorInput() {
		return (AddressIdEditorInput) super.getEditorInput();
	}

	@Override
	public void setFocus() {
		txtName.setFocus();
	}

	@Override
	public void doSaveAs() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

}