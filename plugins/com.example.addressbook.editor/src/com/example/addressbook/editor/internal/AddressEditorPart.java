package com.example.addressbook.editor.internal;

import java.util.Collection;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.EditorPart;

import com.example.addressbook.editor.AddressEditorConstants;
import com.example.addressbook.editor.AddressIdEditorInput;
import com.example.addressbook.entities.Address;
import com.example.addressbook.services.IAddressService;


public class AddressEditorPart extends EditorPart {

	private IAddressService addressService;

	private Text txtName;
	private Text txtStreet;
	private Text txtZip;
	private Text txtCity;
	private ComboViewer cvCountry;

	private boolean dirty;
	private IObservableValue partNameObservable;
	private IObservableValue model = new WritableValue();

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		partNameObservable = new PartNameObservableValue();
	}

	@Override
	public void createPartControl(Composite parent) {

		createUi(parent);
		loadModel();
		bindUIToModel();

	}

	private void createUi(Composite parent) {
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

		// STRASSE
		toolkit.createLabel(client, "Straße:");

		txtStreet = toolkit.createText(client, "");
		txtStreet.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).span(2, 1).applyTo(txtStreet);

		// PLZ / ORT
		toolkit.createLabel(client, "PLZ/Ort:");

		txtZip = toolkit.createText(client, "");
		GridDataFactory.fillDefaults().hint(50, SWT.DEFAULT).applyTo(txtZip);

		txtCity = toolkit.createText(client, "");
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).applyTo(txtCity);

		ControlDecoration decoration = new ControlDecoration(txtCity, SWT.RIGHT | SWT.TOP);
		Image errorImage = FieldDecorationRegistry.getDefault().getFieldDecoration(
				FieldDecorationRegistry.DEC_CONTENT_PROPOSAL).getImage();
		decoration.setImage(errorImage);

		new AutoCompleteField(txtCity, new TextContentAdapter(), addressService.getAllCities());

		// LAND
		toolkit.createLabel(client, "Land:");

		cvCountry = new ComboViewer(client, SWT.READ_ONLY);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).span(2, 1).applyTo(
				cvCountry.getCombo());
		cvCountry.setContentProvider(new ArrayContentProvider());
		cvCountry.setLabelProvider(new CountryLabelProvider());
		cvCountry.setInput(addressService.getAllCountries());

		section.setExpanded(true);
	}

	private void loadModel() {
		this.model.setValue(addressService.getAddress(getEditorInput().getId()));
	}

	private void bindUIToModel() {
		DataBindingContext ctx = new DataBindingContext();

		IObservableValue name = PojoObservables.observeDetailValue(model, "name", String.class);
		ctx.bindValue(SWTObservables.observeText(txtName, SWT.Modify), name);
		ctx.bindValue(partNameObservable, name);
		ctx.bindValue(SWTObservables.observeText(txtStreet, SWT.Modify), PojoObservables.observeDetailValue(model,
				"street", String.class));
		ctx.bindValue(SWTObservables.observeText(txtZip, SWT.Modify), PojoObservables.observeDetailValue(model, "zip",
				String.class));
		ctx.bindValue(SWTObservables.observeText(txtCity, SWT.Modify), PojoObservables.observeDetailValue(model,
				"city", String.class));
		ctx.bindValue(ViewersObservables.observeSingleSelection(cvCountry), PojoObservables.observeDetailValue(model,
				"country", String.class));

		addDirtyOnModelChangeListeners(ctx);
	}

	/**
	 * Listen on all model values of the given data binding context and set the
	 * editor dirty flag if a model value changes.
	 */
	@SuppressWarnings("unchecked")
	private void addDirtyOnModelChangeListeners(DataBindingContext ctx) {
		for (Binding binding : (Collection<Binding>) ctx.getBindings()) {
			binding.getModel().addChangeListener(new IChangeListener() {

				public void handleChange(ChangeEvent event) {
					setDirty(true);
				}
			});
		}
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
		model.setValue(addressService.saveAddress(getModelObject()));
		setDirty(false);
	}

	private Address getModelObject() {
		return (Address) model.getValue();
	}

	@Override
	public AddressIdEditorInput getEditorInput() {
		return (AddressIdEditorInput) super.getEditorInput();
	}

	@Override
	public void setFocus() {
		txtName.setFocus();
	}

	public void setAddressService(IAddressService addressService) {
		this.addressService = addressService;
	}

	private final class PartNameObservableValue extends AbstractObservableValue {

		public PartNameObservableValue() {
			super(SWTObservables.getRealm(getSite().getShell().getDisplay()));
		}

		@Override
		protected Object doGetValue() {
			return getPartName();
		}

		@Override
		protected void doSetValue(Object value) {
			setPartName(String.valueOf(value));
		}

		public Object getValueType() {
			return String.class;
		}
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