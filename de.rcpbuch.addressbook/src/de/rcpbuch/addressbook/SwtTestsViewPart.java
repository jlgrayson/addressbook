package de.rcpbuch.addressbook;

import org.eclipse.swt.SWT;
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

		Label label = new Label(parent, SWT.NONE);
		label.setText("Label");

		Text text = new Text(parent, SWT.BORDER);

		Button checkbox = new Button(parent, SWT.CHECK);
		checkbox.setText("Checkbox");
	}

	@Override
	public void setFocus() {

	}

}
