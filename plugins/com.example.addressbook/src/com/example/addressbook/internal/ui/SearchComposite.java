package com.example.addressbook.internal.ui;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.example.addressbook.AddressBook;

import de.ralfebert.rcputils.porting.CrossPlatformSupport;

public class SearchComposite extends Composite {

	private final IObservableValue textObservable;

	public SearchComposite(Composite parent) {
		super(parent, SWT.NONE);
		LocalResourceManager resources = new LocalResourceManager(JFaceResources.getResources(), this);

		int columns = 1;
		if (!CrossPlatformSupport.isStyleSupported(SWT.ICON_SEARCH)) {
			Label searchLabel = new Label(this, SWT.NONE);
			searchLabel.setImage(resources.createImage(AddressBook.ICON_MAGNIFIER));
			GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(searchLabel);
			columns++;
		}
		Text searchText = new Text(this, SWT.BORDER | SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
		textObservable = SWTObservables.observeText(searchText, SWT.Modify);

		// Layout
		GridLayoutFactory.fillDefaults().numColumns(columns).margins(5, 5).applyTo(this);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(searchText);
	}

	public IObservableValue observeText() {
		return textObservable;
	}
}
