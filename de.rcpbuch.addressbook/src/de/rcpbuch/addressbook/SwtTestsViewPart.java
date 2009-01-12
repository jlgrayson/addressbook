package de.rcpbuch.addressbook;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

public class SwtTestsViewPart extends ViewPart {

	public static final String VIEW_ID = SwtTestsViewPart.class.getName();

	public SwtTestsViewPart() {

	}

	@Override
	public void createPartControl(Composite parent) {

		parent.setLayout(new GridLayout(2, false));

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
		Text city = new Text(zipCityComposite, SWT.BORDER);
		city.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

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
