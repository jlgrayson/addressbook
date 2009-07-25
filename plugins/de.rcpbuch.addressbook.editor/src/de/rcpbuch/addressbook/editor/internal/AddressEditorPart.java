package de.rcpbuch.addressbook.editor.internal;

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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.EditorPart;

import de.rcpbuch.addressbook.editor.AddressEditorConstants;
import de.rcpbuch.addressbook.editor.AddressIdEditorInput;
import de.rcpbuch.addressbook.entities.Address;
import de.rcpbuch.addressbook.entities.Country;
import de.rcpbuch.addressbook.services.AddressbookServices;

public class AddressEditorPart extends EditorPart {

	private Address address;

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
	public AddressIdEditorInput getEditorInput() {
		return (AddressIdEditorInput) super.getEditorInput();
	}

	@Override
	public void createPartControl(Composite parent) {

		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, AddressEditorConstants.HELP_CONTEXT_EDIT);

		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		final ScrolledForm form = toolkit.createScrolledForm(parent);
		FillLayout layout = new FillLayout();
		form.getBody().setLayout(layout);

		Section section = toolkit.createSection(form.getBody(), Section.TWISTIE | Section.TITLE_BAR);
		section.setText("Anschrift");

		Composite client = toolkit.createComposite(section, SWT.WRAP);
		GridLayoutFactory.fillDefaults().margins(10, 3).numColumns(3).applyTo(client);
		section.setClient(client);

		// NAME
		toolkit.createLabel(client, "Name:");

		txtName = toolkit.createText(client, "");
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).span(2, 1).applyTo(txtName);

		ModifyListener dirtyModifyListener = new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}

		};

		txtName.addModifyListener(dirtyModifyListener);

		// STRASSE
		toolkit.createLabel(client, "Straße:");

		txtStreet = toolkit.createText(client, "");
		txtStreet.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		txtStreet.addModifyListener(dirtyModifyListener);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).span(2, 1).applyTo(txtStreet);

		// PLZ / ORT
		toolkit.createLabel(client, "PLZ/Ort:");

		txtZip = toolkit.createText(client, "");
		txtZip.addModifyListener(dirtyModifyListener);

		txtCity = toolkit.createText(client, "");
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).applyTo(txtCity);
		txtCity.addModifyListener(dirtyModifyListener);

		ControlDecoration decoration = new ControlDecoration(txtCity, SWT.RIGHT | SWT.TOP);
		Image errorImage = FieldDecorationRegistry.getDefault().getFieldDecoration(
				FieldDecorationRegistry.DEC_CONTENT_PROPOSAL).getImage();
		decoration.setImage(errorImage);

		new AutoCompleteField(txtCity, new TextContentAdapter(), AddressbookServices.getAddressService().getAllCities());

		// LAND
		toolkit.createLabel(client, "Land:");

		cvCountry = new ComboViewer(client, SWT.READ_ONLY);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).span(2, 1).applyTo(
				cvCountry.getCombo());
		cvCountry.setContentProvider(new ArrayContentProvider());
		cvCountry.setLabelProvider(new CountryLabelProvider());
		cvCountry.setInput(AddressbookServices.getAddressService().getAllCountries());
		cvCountry.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				setDirty(true);
			}

		});

		reload();
		section.setExpanded(true);

	}

	private void reload() {
		address = AddressbookServices.getAddressService().getAddress(getEditorInput().getId());
		updateUi();
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	protected void setDirty(boolean b) {
		this.dirty = b;
		firePropertyChange(IEditorPart.PROP_DIRTY);
		setPartName(address.getName());
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		updateModel();
		address = AddressbookServices.getAddressService().saveAddress(address);
		updateUi();
	}

	private void updateModel() {
		address.setName(txtName.getText());
		address.setStreet(txtStreet.getText());
		address.setZip(txtZip.getText());
		address.setCity(txtCity.getText());
		IStructuredSelection selection = (IStructuredSelection) cvCountry.getSelection();
		address.setCountry((Country) selection.getFirstElement());
	}

	private void updateUi() {
		txtName.setText(address.getName());
		txtStreet.setText(address.getStreet());
		txtZip.setText(address.getZip());
		txtCity.setText(address.getCity());
		cvCountry.setSelection(new StructuredSelection(address.getCountry()));
		setPartName(address.getName());
		setDirty(false);
	}

	@Override
	public void doSaveAs() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void setFocus() {
		txtName.setFocus();
	}

}