package com.example.addressbook.editing.internal;

import java.util.Collection;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.ValidationStatusProvider;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.internal.databinding.provisional.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.example.addressbook.AddressBookMessages;
import com.example.addressbook.editing.AddressIdEditorInput;
import com.example.addressbook.entities.Address;
import com.example.addressbook.services.IAddressService;

/**
 * Editor part implementation for editing Address objects using the
 * AddressBookService.
 */
@SuppressWarnings("restriction")
public class AddressEditorPart extends EditorPart {

	private Text txtName;
	private Text txtStreet;
	private Text txtZip;
	private Text txtCity;
	private ComboViewer cvCountry;

	private final DataBindingContext bindingContext = new DataBindingContext();
	private boolean dirty;
	private IObservableValue partNameObservable;
	private final IObservableValue model = new WritableValue();
	private IAddressService addressService;

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
		createBindings();
	}

	private void createUi(Composite parent) {

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

		new AutoCompleteField(txtCity, new TextContentAdapter(), addressService.getAllCities());

		// Country
		Label lblCountry = new Label(parent, SWT.NONE);
		lblCountry.setText(AddressBookMessages.Country + AddressBookMessages.Field_Mark);

		cvCountry = new ComboViewer(parent, SWT.READ_ONLY);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).span(2, 1).applyTo(
				cvCountry.getCombo());
		cvCountry.setContentProvider(new ArrayContentProvider());
		cvCountry.setLabelProvider(new CountryLabelProvider());
		cvCountry.setInput(addressService.getAllCountries());

		// Layout
		GridLayoutFactory.fillDefaults().margins(10, 10).spacing(10, 6).numColumns(3).applyTo(parent);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).span(2, 1).applyTo(txtName);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).span(2, 1).applyTo(txtStreet);
		GridDataFactory.fillDefaults().hint(50, SWT.DEFAULT).applyTo(txtZip);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).applyTo(txtCity);

	}

	public void setAddressService(IAddressService addressService) {
		this.addressService = addressService;
	}

	private void loadModel() {
		model.setValue(addressService.getAddress(getEditorInput().getId()));
	}

	/**
	 * Bind the SWT UI to the model object using JFace Data Binding.
	 */
	private void createBindings() {
		IObservableValue name = PojoObservables.observeDetailValue(model, "name", String.class); //$NON-NLS-1$

		bindingContext.bindValue(SWTObservables.observeText(txtName, SWT.Modify), name);
		bindingContext.bindValue(partNameObservable, name);

		bindingContext.bindValue(SWTObservables.observeText(txtStreet, SWT.Modify), PojoObservables.observeDetailValue(
				model, "street", String.class)); //$NON-NLS-1$

		UpdateValueStrategy zipUiToModel = new UpdateValueStrategy();
		zipUiToModel.setAfterConvertValidator(new ZipValidator());
		Binding zipBinding = bindingContext.bindValue(SWTObservables.observeText(txtZip, SWT.Modify), PojoObservables
				.observeDetailValue(model, "zip", //$NON-NLS-1$
						String.class), zipUiToModel, null);

		bindingContext.bindValue(SWTObservables.observeText(txtCity, SWT.Modify), PojoObservables.observeDetailValue(
				model, "city", String.class)); //$NON-NLS-1$

		bindingContext.bindValue(ViewersObservables.observeSingleSelection(cvCountry), PojoObservables
				.observeDetailValue(model, "country", String.class)); //$NON-NLS-1$

		// Add control decorations
		ControlDecorationSupport.create(zipBinding, SWT.TOP | SWT.RIGHT);

		addDirtyOnModelChangeListeners(bindingContext);
	}

	/**
	 * Listen on all model values of the given data binding context and set the
	 * editor dirty flag if a model value changes.
	 */
	@SuppressWarnings("unchecked")
	private void addDirtyOnModelChangeListeners(DataBindingContext ctx) {
		for (Binding binding : (Collection<Binding>) ctx.getBindings()) {
			binding.getTarget().addChangeListener(new IChangeListener() {

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
		@SuppressWarnings("unchecked")
		Collection<ValidationStatusProvider> validationStatusProviders = bindingContext.getValidationStatusProviders();
		for (ValidationStatusProvider statusProvider : validationStatusProviders) {
			IStatus status = (IStatus) (statusProvider.getValidationStatus().getValue());
			if (!status.isOK()) {
				ErrorDialog.openError(getSite().getShell(), AddressBookMessages.ValidationError,
						AddressBookMessages.SaveNotAllowedBecauseOfValidationError, status);
				monitor.setCanceled(true);
				return;
			}
		}

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

	@Override
	public void doSaveAs() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	/**
	 * Observable value for JFace data binding to bind to the part name of this
	 * editor.
	 */
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

}