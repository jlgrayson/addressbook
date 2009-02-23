package de.rcpbuch.addressbook;

import java.util.List;

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
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import de.rcpbuch.addressbook.data.AddressbookServices;
import de.rcpbuch.addressbook.data.Country;

public class SwtTestsViewPart extends ViewPart {

	public static final String VIEW_ID = SwtTestsViewPart.class.getName();

	public SwtTestsViewPart() {

	}

	@Override
	public void createPartControl(Composite parent) {

		GridLayout layout = new GridLayout(2, false);
		layout.marginRight = 10;
		parent.setLayout(layout);

		final Label labelName = new Label(parent, SWT.NONE);
		labelName.setText("Name:");

		Text name = new Text(parent, SWT.BORDER);
		name.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		final Label labelStreet = new Label(parent, SWT.NONE);
		labelStreet.setText("Straße:");

		Text street = new Text(parent, SWT.BORDER);
		street.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		final Label labelZipCity = new Label(parent, SWT.NONE);
		labelZipCity.setText("PLZ/Ort:");

		Composite zipCityComposite = new Composite(parent, SWT.NONE);
		zipCityComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		zipCityComposite.setLayout(gridLayout);

		Text zip = new Text(zipCityComposite, SWT.BORDER);
		zip.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		Text city = new Text(zipCityComposite, SWT.BORDER);
		city.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		ControlDecoration decoration = new ControlDecoration(city, SWT.RIGHT | SWT.TOP);
		Image errorImage = FieldDecorationRegistry.getDefault().getFieldDecoration(
				FieldDecorationRegistry.DEC_CONTENT_PROPOSAL).getImage();

		decoration.setImage(errorImage);
		decoration.show();

		new AutoCompleteField(city, new TextContentAdapter(), AddressbookServices.getAddressService().getAllCities());

		final Label labelCountry = new Label(parent, SWT.NONE);
		labelCountry.setText("Land:");

		final Combo country = new Combo(parent, SWT.READ_ONLY);
		country.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		List<Country> countries = AddressbookServices.getAddressService().getAllCountries();

		ComboViewer comboViewer = new ComboViewer(country);
		comboViewer.setContentProvider(new ArrayContentProvider());
		comboViewer.setLabelProvider(new CountryLabelProvider());
		comboViewer.setInput(countries);

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

	}

	@Override
	public void setFocus() {

	}

}
