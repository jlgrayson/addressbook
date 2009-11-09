package com.example.addressbook.internal.address.create;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class FreeTextWizardPage extends WizardPage {

	private Text text;

	protected FreeTextWizardPage() {
		super("New address");
		setTitle("New address");
		setMessage("Please enter a new address!");
	}

	public void createControl(Composite parent) {
		text = new Text(parent, SWT.MULTI);
		setControl(text);
	}

	public String getText() {
		return text.getText();
	}

}
