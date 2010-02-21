package com.example.addressbook.editing.internal;

import java.io.ByteArrayInputStream;

import jgravatar.Gravatar;

import org.apache.commons.lang.StringUtils;
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

	private static final int GRAVATAR_SIZE = 50;

	private Text txtName;
	private Text txtStreet;
	private Text txtZip;
	private Text txtCity;
	private ComboViewer cvCountry;
	private Text txtEmail;

	private boolean dirty;
	private Label lblGravatar;

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

		// Gravatar
		lblGravatar = new Label(parent, SWT.NONE);
		lblGravatar.setData("org.eclipse.swtbot.widget.key", "gravatar"); //$NON-NLS-1$ //$NON-NLS-2$

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
		cvCountry.setContentProvider(ArrayContentProvider.getInstance());
		cvCountry.setLabelProvider(new CountryLabelProvider());
		cvCountry.setInput(AddressbookServices.getAddressService().getAllCountries());

		// E-Mail
		Label lblEmail = new Label(parent, SWT.NONE);
		lblEmail.setText(AddressBookMessages.Email + AddressBookMessages.Field_Mark);

		txtEmail = new Text(parent, SWT.BORDER);
		txtEmail.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		// Layout
		GridLayoutFactory.fillDefaults().margins(10, 10).spacing(6, 3).numColumns(4).applyTo(parent);
		GridDataFactory simpleField = GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).span(2,
				1);
		simpleField.applyTo(txtName);
		GridDataFactory.fillDefaults().span(1, 5).hint(GRAVATAR_SIZE, GRAVATAR_SIZE).align(SWT.LEFT, SWT.TOP).indent(
				20, 0).applyTo(lblGravatar);
		simpleField.applyTo(txtStreet);
		GridDataFactory.fillDefaults().hint(50, SWT.DEFAULT).applyTo(txtZip);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).applyTo(txtCity);
		simpleField.applyTo(cvCountry.getCombo());
		simpleField.applyTo(txtEmail);
	}

	private void loadModel() {
		Address address = AddressbookServices.getAddressService().getAddress(getEditorInput().getId());
		txtName.setText(address.getName());
		txtStreet.setText(address.getStreet());
		txtZip.setText(address.getZip());
		txtCity.setText(address.getCity());
		cvCountry.setSelection(new StructuredSelection(address.getCountry()));
		txtEmail.setText(address.getEmail());
		setPartName(address.getName());
		updateGravatar();
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
		txtEmail.addModifyListener(modifyListener);

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
		address.setEmail(txtEmail.getText());

		AddressbookServices.getAddressService().saveAddress(address);

		loadModel();
		setDirty(false);
	}

	private void updateGravatar() {
		String email = txtEmail.getText();
		Image newImage = null;
		if (StringUtils.isNotBlank(email)) {
			Gravatar gravatar = new Gravatar();
			gravatar.setSize(GRAVATAR_SIZE);
			byte[] imageBytes = gravatar.download(email);
			if (imageBytes != null) {
				newImage = new Image(lblGravatar.getDisplay(), new ByteArrayInputStream(imageBytes));
			}
		}
		if (lblGravatar.getImage() != null) {
			lblGravatar.getImage().dispose();
		}
		lblGravatar.setImage(newImage);
		lblGravatar.getParent().layout();
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