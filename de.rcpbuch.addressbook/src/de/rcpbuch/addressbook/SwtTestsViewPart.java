package de.rcpbuch.addressbook;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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

		final Label label = new Label(parent, SWT.NONE);
		label.setText("Label");

		final Text text = new Text(parent, SWT.BORDER);
		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				label.setText(text.getText());
			}

		});

		Button checkbox = new Button(parent, SWT.CHECK);
		checkbox.setText("Checkbox");
	}

	@Override
	public void setFocus() {

	}

}
