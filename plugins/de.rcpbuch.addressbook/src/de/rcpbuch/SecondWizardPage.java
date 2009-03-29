package de.rcpbuch;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;

public class SecondWizardPage extends WizardPage {

	protected SecondWizardPage() {
		super("Zweiter Schritt");
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setBackground(new Color(parent.getDisplay(), 173, 168, 211));
		setControl(composite);
	}

}
