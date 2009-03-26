package de.rcpbuch.addressbook.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.progress.WorkbenchJob;

import de.rcpbuch.addressbook.entities.Address;
import de.rcpbuch.addressbook.entities.Country;
import de.rcpbuch.addressbook.services.AddressbookServices;

public class AddressEditorPart extends EditorPart {

	public static final String EDITOR_ID = AddressEditorPart.class.getName();

	private Address address;

	private Text txtName;
	private Text txtStreet;
	private Text txtZip;
	private Text txtCity;
	private ComboViewer cvCountry;

	private boolean dirty;

	private Composite parent;

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

		this.parent = parent;

		GridLayout layout = new GridLayout(2, false);
		layout.marginRight = 10;
		parent.setLayout(layout);

		// NAME
		final Label labelName = new Label(parent, SWT.NONE);
		labelName.setText("Name:");

		txtName = new Text(parent, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		ModifyListener dirtyModifyListener = new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}

		};

		txtName.addModifyListener(dirtyModifyListener);

		// STRASSE
		final Label labelStreet = new Label(parent, SWT.NONE);
		labelStreet.setText("Straße:");

		txtStreet = new Text(parent, SWT.BORDER);
		txtStreet.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		txtStreet.addModifyListener(dirtyModifyListener);

		// PLZ / ORT
		final Label labelZipCity = new Label(parent, SWT.NONE);
		labelZipCity.setText("PLZ/Ort:");

		Composite zipCityComposite = new Composite(parent, SWT.NONE);
		zipCityComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		zipCityComposite.setLayout(gridLayout);

		txtZip = new Text(zipCityComposite, SWT.BORDER);
		txtZip.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		txtZip.addModifyListener(dirtyModifyListener);

		txtCity = new Text(zipCityComposite, SWT.BORDER);
		txtCity.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		txtCity.addModifyListener(dirtyModifyListener);

		ControlDecoration decoration = new ControlDecoration(txtCity, SWT.RIGHT | SWT.TOP);
		Image errorImage = FieldDecorationRegistry.getDefault().getFieldDecoration(
				FieldDecorationRegistry.DEC_CONTENT_PROPOSAL).getImage();
		decoration.setImage(errorImage);

		new AutoCompleteField(txtCity, new TextContentAdapter(), AddressbookServices.getAddressService().getAllCities());

		// LAND
		final Label labelCountry = new Label(parent, SWT.NONE);
		labelCountry.setText("Land:");

		cvCountry = new ComboViewer(parent, SWT.READ_ONLY);
		cvCountry.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		cvCountry.setContentProvider(new ArrayContentProvider());
		cvCountry.setLabelProvider(new CountryLabelProvider());
		cvCountry.setInput(AddressbookServices.getAddressService().getAllCountries());
		cvCountry.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				setDirty(true);
			}

		});

		Button btn = new Button(parent, SWT.NONE);
		btn.setText("Calculate");
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		btn.setLayoutData(gd);
		btn.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent event) {
				Job job = new Job("Some calculation") {

					@Override
					protected IStatus run(IProgressMonitor monitor) {
						monitor.beginTask("Calculating", 50);
						try {
							for (int i = 0; i < 50; i++) {
								Thread.sleep(100);
								monitor.worked(1);
							}
						} catch (InterruptedException e) {
							throw new RuntimeException(e);
						}
						System.out.println("finished");
						return Status.OK_STATUS;
					}

				};

				job.schedule();
			}

		});

		reload();

	}

	private void reload() {
		// TODO: nice progress, clean up code
		parent.setVisible(false);

		new Job("Load address") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				address = AddressbookServices.getAddressService().getAddress(getEditorInput().getId());

				new WorkbenchJob("Update UI") {

					@Override
					public IStatus runInUIThread(IProgressMonitor monitor) {
						updateUi();
						parent.setVisible(true);
						return Status.OK_STATUS;
					}

				}.schedule();

				return Status.OK_STATUS;
			}

		}.schedule();

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