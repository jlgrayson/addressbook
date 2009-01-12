package de.rcpbuch.addressbook;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
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

		parent.setLayout(new RowLayout(SWT.VERTICAL));

		final Label labelName = new Label(parent, SWT.NONE);
		labelName.setText("Name:");
		labelName.setLayoutData(new RowData(80, SWT.DEFAULT));

		new Text(parent, SWT.BORDER);

		final Label labelStreet = new Label(parent, SWT.NONE);
		labelStreet.setText("Straße:");
		labelStreet.setLayoutData(new RowData(80, SWT.DEFAULT));

		new Text(parent, SWT.BORDER);

		final Label labelZipCity = new Label(parent, SWT.NONE);
		labelZipCity.setText("PLZ/Ort");
		labelZipCity.setLayoutData(new RowData(80, SWT.DEFAULT));

		Composite composite = new Composite(parent, SWT.NONE);
		RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
		rowLayout.marginLeft = 0;
		rowLayout.marginTop = 0;
		composite.setLayout(rowLayout);

		new Text(composite, SWT.BORDER);
		new Text(composite, SWT.BORDER);

	}

	@Override
	public void setFocus() {

	}

}
