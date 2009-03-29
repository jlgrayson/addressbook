package de.rcpbuch;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class FirstWizardPage extends WizardPage {

	protected FirstWizardPage() {
		super("Some wizard Page");
		setTitle("WizardPage Title");
		setMessage("WizardPage Message");
	}

	public void createControl(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setBackground(new Color(parent.getDisplay(), 173, 168, 211));
		label.setText("Control");
		setControl(label);
	}

}
