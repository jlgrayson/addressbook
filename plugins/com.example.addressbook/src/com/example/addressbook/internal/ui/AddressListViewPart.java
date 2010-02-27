package com.example.addressbook.internal.ui;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.riena.core.wire.InjectService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.example.addressbook.AddressBookMessages;
import com.example.addressbook.entities.Address;
import com.example.addressbook.services.IAddressChangeListener;
import com.example.addressbook.services.IAddressService;

import de.ralfebert.rcputils.concurrent.UIProcess;
import de.ralfebert.rcputils.menus.ContextMenu;
import de.ralfebert.rcputils.tables.TableViewerBuilder;
import de.ralfebert.rcputils.wired.WiredViewPart;

public class AddressListViewPart extends WiredViewPart {

	private IAddressService addressService;

	/**
	 * Job which loads the list of addresses and schedules an UIJob to refresh
	 * the UI afterwards.
	 */
	public class LoadAddressesJob extends UIProcess {

		private List<Address> addresses;

		public LoadAddressesJob(Display display) {
			super(display, AddressBookMessages.LoadAddresses);
		}

		@Override
		protected void runInBackground(IProgressMonitor monitor) {
			addresses = (addressService != null) ? addressService.getAllAddresses() : Collections.<Address> emptyList();
		}

		@Override
		protected void runInUIThread() {
			if (addressList != null && !addressList.getTable().isDisposed()) {
				addressList.setInput(addresses);
				// WORKAROUND: Unnecessary horizontal scrollbar
				// See https://bugs.eclipse.org/bugs/show_bug.cgi?id=304128
				addressList.getTable().getParent().layout();
			}
		}
	}

	private TableViewerBuilder addressList;

	private final IAddressChangeListener addressChangeListener = new IAddressChangeListener() {

		public void addressesChanged() {
			refresh();
		}

	};

	@Override
	public void createPartControl(Composite parent) {

		// Search control
		final SearchComposite search = new SearchComposite(parent);

		// Separate composite to embed the table
		Composite tableComposite = new Composite(parent, SWT.NONE);

		addressList = new TableViewerBuilder(tableComposite);
		addressList.createColumn(AddressBookMessages.Name).bindToProperty("name").setPercentWidth(100).build(); //$NON-NLS-1$

		// Filter
		addressList.getTableViewer().addFilter(new ViewerFilter() {

			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				Address address = (Address) element;
				String text = String.valueOf(search.observeText().getValue());
				return address.getName().toLowerCase().contains(text.toLowerCase());
			}

		});
		search.observeText().addChangeListener(new IChangeListener() {

			@Override
			public void handleChange(ChangeEvent event) {
				addressList.getTableViewer().refresh();
			}
		});

		ContextMenu contextMenu = new ContextMenu(addressList.getTableViewer(), getSite());
		contextMenu.setDefaultItemHandling(true);

		// Layout
		GridLayoutFactory.fillDefaults().numColumns(1).applyTo(parent);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).applyTo(search);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(tableComposite);

		refresh();
	}

	@InjectService
	public void bindAddressService(IAddressService addressService) {
		this.addressService = addressService;
		// Register for update events to refresh the view contents automatically
		addressService.addAddressChangeListener(addressChangeListener);
		refresh();
	}

	public void unbindAddressService(IAddressService addressService) {
		this.addressService = null;
		// Remove the change listener because otherwise the address service
		// would call the view object even when this view is already gone
		addressService.removeAddressChangeListener(addressChangeListener);
		refresh();
	}

	@Override
	public void setFocus() {
		addressList.getTable().setFocus();
	}

	public void refresh() {
		if (addressList != null && !addressList.getTable().isDisposed()) {
			new LoadAddressesJob(addressList.getTable().getDisplay()).schedule();
		}
	}
}