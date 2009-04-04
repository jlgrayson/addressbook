package de.rcpbuch.addressbook.vcard;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.progress.UIJob;

import de.rcpbuch.addressbook.entities.Address;

public class VCardComposite extends Composite {

	private boolean selected;
	private Label lblName;
	private Label lblStreet;
	private Label lblZipCity;

	public VCardComposite(Composite parent, Address address) {
		super(parent, SWT.BORDER);

		LocalResourceManager resources = new LocalResourceManager(JFaceResources.getResources(), this);

		FontDescriptor boldFontDescriptor = FontDescriptor.createFrom(getDisplay().getSystemFont());
		boldFontDescriptor = boldFontDescriptor.setStyle(SWT.BOLD);
		Font boldFont = resources.createFont(boldFontDescriptor);

		RowLayout layout = new RowLayout(SWT.VERTICAL);
		layout.marginLeft = 5;
		layout.marginRight = 5;
		layout.marginTop = 5;
		layout.marginBottom = 5;
		this.setLayout(layout);

		lblName = new Label(this, SWT.NONE);
		lblName.setFont(boldFont);
		lblName.setText(address.getName());

		lblStreet = new Label(this, SWT.NONE);
		lblStreet.setText(address.getStreet());

		lblZipCity = new Label(this, SWT.NONE);
		lblZipCity.setText(address.getZip() + " " + address.getCity());

		setSelected(false);

		MouseAdapter clickListener = new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				setSelected(!isSelected());
			}
		};
		addMouseListener(clickListener);
		for (Control c : getChildren()) {
			c.addMouseListener(clickListener);
		}
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		return new Point(200, 100);
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;

		new UIJob("Update selection") {

			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				Color color = getDisplay().getSystemColor(isSelected() ? SWT.COLOR_LIST_SELECTION : SWT.COLOR_WHITE);
				setBackground(color);
				lblName.setBackground(color);
				lblStreet.setBackground(color);
				lblZipCity.setBackground(color);
				return Status.OK_STATUS;
			}

		}.schedule();
	}

}