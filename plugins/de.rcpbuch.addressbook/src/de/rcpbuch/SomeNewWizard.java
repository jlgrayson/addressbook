package de.rcpbuch;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class SomeNewWizard extends Wizard implements INewWizard {

	public SomeNewWizard() {
		addPage(new FirstWizardPage());
		addPage(new SecondWizardPage());
	}

	@Override
	public boolean performFinish() {
		// true zurückgeben wenn Wizard erfolgreich abgeschlossen wurde
		return true;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

}
