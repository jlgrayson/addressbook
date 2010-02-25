package com.example.addressbook.internal.ui;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.riena.core.wire.InjectService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.handlers.IHandlerService;

import com.example.addressbook.AddressBook;
import com.example.addressbook.AddressBookMessages;
import com.example.addressbook.entities.Address;
import com.example.addressbook.services.IAddressChangeListener;
import com.example.addressbook.services.IAddressService;

import de.ralfebert.rcputils.concurrent.UIProcess;
import de.ralfebert.rcputils.menus.ContextMenu;
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
			super(display, AddressBookMessages.get().LoadAddresses);
		}

		@Override
		protected void runInBackground(IProgressMonitor monitor) {
			addresses = (addressService != null) ? addressService.getAllAddresses() : Collections.<Address> emptyList();
		}

		@Override
		protected void runInUIThread() {
			if (tableViewer != null && !tableViewer.getTable().isDisposed()) {
				tableViewer.setInput(addresses);
			}
		}

	}

	private TableViewer tableViewer;

	private final IAddressChangeListener addressChangeListener = new IAddressChangeListener() {

		public void addressesChanged() {
			refresh();
		}

	};

	@Override
	public void createPartControl(Composite parent) {

		// Resources are managed with a ResourceManager and disposed when parent
		// is disposed
		LocalResourceManager resources = new LocalResourceManager(JFaceResources.getResources(), parent);

		// Search control
		Composite searchComposite = new Composite(parent, SWT.NONE);
		Label searchLabel = new Label(searchComposite, SWT.NONE);
		searchLabel.setImage(resources.createImage(AddressBook.ICON_MAGNIFIER));
		// TODO: RAP: SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL
		final Text searchText = new Text(searchComposite, SWT.BORDER);

		// Separate composite to embed the table
		Composite tableComposite = new Composite(parent, SWT.NONE);

		// Create JFace viewer
		tableViewer = new TableViewer(tableComposite, SWT.FULL_SELECTION | SWT.MULTI | SWT.V_SCROLL);
		tableViewer.setContentProvider(new ArrayContentProvider());

		// Configure underlying SWT table
		final Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		// Create column "Name"
		TableViewerColumn colName = new TableViewerColumn(tableViewer, SWT.NONE);
		colName.getColumn().setText(AddressBookMessages.get().Name);
		colName.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				cell.setText(((Address) cell.getElement()).getName());
			}

		});

		// Sort
		tableViewer.setComparator(new ViewerComparator() {
			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				Address addr1 = (Address) e1;
				Address addr2 = (Address) e2;
				return addr1.getName().compareToIgnoreCase(addr2.getName());
			}
		});

		// Filter
		tableViewer.addFilter(new ViewerFilter() {

			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				Address address = (Address) element;
				return address.getName().toLowerCase().contains(searchText.getText().toLowerCase());
			}

		});
		searchText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				tableViewer.refresh();
			}

		});

		// TODO: RAP: Menu.setDefaultItem
		new ContextMenu(tableViewer, getSite(), false);
		tableViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				IHandlerService handlerService = (IHandlerService) getSite().getWorkbenchWindow().getService(
						IHandlerService.class);
				try {
					handlerService.executeCommand(AddressBook.COMMAND_OPEN, null);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		// Layout for parent
		GridLayoutFactory.fillDefaults().numColumns(1).applyTo(parent);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).applyTo(searchComposite);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(tableComposite);

		// Layout for searchComposite
		GridLayoutFactory.fillDefaults().numColumns(2).margins(5, 5).applyTo(searchComposite);
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(searchLabel);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(searchText);

		// Layout for Table
		TableColumnLayout tableLayout = new TableColumnLayout();
		tableComposite.setLayout(tableLayout);
		tableLayout.setColumnData(colName.getColumn(), new ColumnWeightData(100));

		refresh();
		parent.layout(true);
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
		tableViewer.getTable().setFocus();
	}

	public void refresh() {
		if (tableViewer != null && !tableViewer.getTable().isDisposed()) {
			new LoadAddressesJob(tableViewer.getTable().getDisplay()).schedule();
		}
	}
}