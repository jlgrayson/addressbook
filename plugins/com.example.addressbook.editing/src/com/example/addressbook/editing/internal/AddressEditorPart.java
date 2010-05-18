package com.example.addressbook.editing.internal;

import java.io.ByteArrayInputStream;

import jgravatar.Gravatar;
import jgravatar.GravatarDefaultImage;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.riena.core.wire.InjectService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.example.addressbook.AddressBookMessages;
import com.example.addressbook.editing.AddressBookEditing;
import com.example.addressbook.editing.AddressIdEditorInput;
import com.example.addressbook.entities.Address;
import com.example.addressbook.entities.Country;
import com.example.addressbook.services.IAddressService;

import de.ralfebert.rcputils.concurrent.UIProcess;
import de.ralfebert.rcputils.wired.WiredModelDataBindingEditorPart;

/**
 * Editor part implementation for editing Address objects using the
 * AddressBookService.
 */
public class AddressEditorPart extends WiredModelDataBindingEditorPart<AddressIdEditorInput, Address> {

	private IObservableValue uiName, uiCountryChoices, uiCountry, uiStreet, uiZip, uiCity, uiEmail, uiGravatarImage;

	private IAddressService addressService;
	private Text txtName;

	@Override
	protected void onCreateUi(Composite parent) {

		// Help Context
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, AddressBookEditing.HELP_EDIT);

		// Name
		createLabel(parent, AddressBookMessages.Name);
		txtName = new Text(parent, SWT.BORDER);

		// Gravatar
		Label lblGravatar = new Label(parent, SWT.NONE);

		// Steet
		createLabel(parent, AddressBookMessages.Street);
		Text txtStreet = new Text(parent, SWT.BORDER);

		// Zip, City
		createLabel(parent, AddressBookMessages.Zip + AddressBookMessages.Field_Separator + AddressBookMessages.City);
		Text txtZip = new Text(parent, SWT.BORDER);
		Text txtCity = new Text(parent, SWT.BORDER);

		// Country
		createLabel(parent, AddressBookMessages.Country);
		ComboViewer cvCountry = new ComboViewer(parent, SWT.READ_ONLY);
		Combo cbCountry = cvCountry.getCombo();
		cvCountry.setContentProvider(ArrayContentProvider.getInstance());
		cvCountry.setLabelProvider(new CountryLabelProvider());

		// E-Mail
		createLabel(parent, AddressBookMessages.Email);
		Text txtEmail = new Text(parent, SWT.BORDER);

		// UI Observables
		uiName = SWTObservables.observeText(txtName, SWT.Modify);
		uiStreet = SWTObservables.observeText(txtStreet, SWT.Modify);
		uiZip = SWTObservables.observeText(txtZip, SWT.Modify);
		uiCountryChoices = ViewersObservables.observeInput(cvCountry);
		uiCountry = ViewersObservables.observeSingleSelection(cvCountry);
		uiCity = SWTObservables.observeText(txtCity, SWT.Modify);
		uiEmail = SWTObservables.observeText(txtEmail, SWT.Modify);
		uiGravatarImage = SWTObservables.observeImage(lblGravatar);

		// Layout
		GridLayoutFactory.fillDefaults().margins(10, 10).spacing(6, 3).numColumns(4).applyTo(parent);
		GridDataFactory field = GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).span(2, 1);
		field.applyTo(txtName);
		GridDataFactory.fillDefaults().span(1, 5).hint(AddressBookEditing.GRAVATAR_SIZE,
				AddressBookEditing.GRAVATAR_SIZE).align(SWT.LEFT, SWT.TOP).indent(20, 0).applyTo(lblGravatar);
		field.applyTo(txtStreet);
		GridDataFactory.fillDefaults().hint(50, SWT.DEFAULT).applyTo(txtZip);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).indent(5, 0).grab(true, false).applyTo(txtCity);
		field.applyTo(cbCountry);
		field.applyTo(txtEmail);

		// Theme
		Color white = parent.getDisplay().getSystemColor(SWT.COLOR_WHITE);
		parent.setBackground(white);
		lblGravatar.setBackground(white);

		// Testing Support
		lblGravatar.setData("org.eclipse.swtbot.widget.key", "gravatar"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	protected void onBind(DataBindingContext bindingContext, IObservableValue model) {
		IObservableValue modelName = PojoObservables.observeDetailValue(model, "name", String.class); //$NON-NLS-1$
		IObservableValue modelStreet = PojoObservables.observeDetailValue(model, "street", String.class); //$NON-NLS-1$
		IObservableValue modelZip = PojoObservables.observeDetailValue(model, "zip", String.class); //$NON-NLS-1$
		IObservableValue modelCity = PojoObservables.observeDetailValue(model, "city", String.class); //$NON-NLS-1$
		IObservableValue modelCountry = PojoObservables.observeDetailValue(model, "country", Country.class); //$NON-NLS-1$
		IObservableValue modelEmail = PojoObservables.observeDetailValue(model, "email", String.class); //$NON-NLS-1$

		bindingContext.bindValue(uiName, modelName);
		bindingContext.bindValue(getPartNameObservable(), modelName);
		bindingContext.bindValue(uiStreet, modelStreet);

		UpdateValueStrategy zipUiToModel = new UpdateValueStrategy();
		zipUiToModel.setAfterConvertValidator(new ZipValidator());
		Binding zipBinding = bindingContext.bindValue(uiZip, modelZip, zipUiToModel, null);
		ControlDecorationSupport.create(zipBinding, SWT.TOP | SWT.RIGHT);

		bindingContext.bindValue(uiCity, modelCity);
		bindingContext.bindValue(uiCountry, modelCountry);
		bindingContext.bindValue(uiEmail, modelEmail);
	}

	@Override
	protected Address onLoad(AddressIdEditorInput input) {
		return addressService.getAddress(input.getId());
	}

	@Override
	protected Address onSave(Address modelObject, IProgressMonitor monitor) {
		return addressService.saveAddress(modelObject);
	}

	@Override
	protected void onReload(Address modelObject) {
		this.uiCountryChoices.setValue(addressService.getAllCountries());
		new UpdateGravatarJob(getDisplay(), getModelObject().getEmail()).schedule();
	}

	@Override
	public void setFocus() {
		txtName.setFocus();
	}

	private class UpdateGravatarJob extends UIProcess {

		private final String email;
		private byte[] imageBytes;

		public UpdateGravatarJob(Display display, String email) {
			super(display, "Update gravatar"); //$NON-NLS-1$
			this.email = email;
		}

		@Override
		protected void runInBackground(IProgressMonitor monitor) {
			if (StringUtils.isNotBlank(email)) {
				Gravatar gravatar = new Gravatar();
				gravatar.setSize(AddressBookEditing.GRAVATAR_SIZE);
				gravatar.setDefaultImage(GravatarDefaultImage.IDENTICON);
				imageBytes = gravatar.download(email);
			}
		}

		@Override
		protected void runInUIThread() {
			Image newImage = null;
			Image newSmallImage = null;
			if (imageBytes != null) {
				newImage = new Image(getDisplay(), new ByteArrayInputStream(imageBytes));
				newSmallImage = new Image(getDisplay(), newImage.getImageData().scaledTo(
						AddressBookEditing.GRAVATAR_SIZE_SMALL, AddressBookEditing.GRAVATAR_SIZE_SMALL));
			}

			// set large gravatar image
			Image oldImage = (Image) uiGravatarImage.getValue();
			uiGravatarImage.setValue(newImage);
			if (oldImage != null) {
				oldImage.dispose();
			}

			// set small title image
			setTitleImage(newSmallImage);
		}
	}

	private Label createLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(text + AddressBookMessages.Field_Mark);
		label.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		return label;
	}

	@Override
	protected boolean isReady() {
		return txtName != null && !txtName.isDisposed() && addressService != null;
	}

	@InjectService
	public void bindAddressService(final IAddressService addressService) {
		this.addressService = addressService;
		reload();
	}

	public void unbindAddressService(IAddressService addressService) {
		this.addressService = null;
		reload();
	}

}