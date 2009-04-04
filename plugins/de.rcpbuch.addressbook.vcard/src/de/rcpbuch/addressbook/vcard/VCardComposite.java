package de.rcpbuch.addressbook.vcard;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import de.rcpbuch.addressbook.entities.Address;

public class VCardComposite extends Composite {

	public VCardComposite(Composite parent, Address address) {
		super(parent, SWT.NONE);

		Color white = getDisplay().getSystemColor(SWT.COLOR_WHITE);

		LocalResourceManager resources = new LocalResourceManager(JFaceResources.getResources(), this);

		FontDescriptor boldFontDescriptor = FontDescriptor.createFrom(getDisplay().getSystemFont());
		boldFontDescriptor = boldFontDescriptor.setStyle(SWT.BOLD);
		Font boldFont = resources.createFont(boldFontDescriptor);

		this.setBackground(white);
		RowLayout layout = new RowLayout(SWT.VERTICAL);
		layout.marginLeft = 5;
		layout.marginRight = 5;
		layout.marginTop = 5;
		layout.marginBottom = 5;
		this.setLayout(layout);

		Label lblName = new Label(this, SWT.NONE);
		lblName.setBackground(white);
		lblName.setFont(boldFont);
		lblName.setText(address.getName());

		Label lblStreet = new Label(this, SWT.NONE);
		lblStreet.setBackground(white);
		lblStreet.setText(address.getStreet());

		Label lblZipCity = new Label(this, SWT.NONE);
		lblZipCity.setBackground(white);
		lblZipCity.setText(address.getZip() + " " + address.getCity());

	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		return new Point(200, 100);
	}

}
