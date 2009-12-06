package com.example.addressbook.internal.address.list;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.progress.UIJob;

import com.example.addressbook.AddressBookMessages;
import com.example.addressbook.AddressBookResources;
import com.example.addressbook.entities.Address;
import com.example.addressbook.services.IAddressChangeListener;
import com.example.addressbook.services.IAddressService;

public class AddressListViewPart extends ViewPart {

	/**
	 * Job which loads the list of addresses and schedules an UIJob to refresh
	 * the UI afterwards.
	 */
	public class LoadAddressesJob extends Job {

		public LoadAddressesJob() {
			super(AddressBookMessages.LoadAddresses);
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			final List<Address> addresses = addressService.getAllAddresses();
			UIJob updateAddresesUiJob = new UIJob(AddressBookMessages.RefreshAddressList) {

				@Override
				public IStatus runInUIThread(IProgressMonitor monitor) {
					tableViewer.setInput(addresses);
					return Status.OK_STATUS;
				}
			};
			updateAddresesUiJob.schedule();
			return Status.OK_STATUS;
		}

	}

	private TableViewer tableViewer;

	private IAddressService addressService;

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
		searchLabel.setImage(resources.createImage(AddressBookResources.ICON_MAGNIFIER));
		final Text searchText = new Text(searchComposite, SWT.BORDER | SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);

		// Separate composite to embed the table
		Composite tableComposite = new Composite(parent, SWT.NONE);

		// Setup layout for parent
		GridLayoutFactory.fillDefaults().numColumns(1).applyTo(parent);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).applyTo(searchComposite);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(tableComposite);

		// Setup layout for searchComposite
		GridLayoutFactory.fillDefaults().numColumns(2).margins(5, 5).applyTo(searchComposite);
		GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(searchLabel);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(searchText);

		// Create JFace viewer
		tableViewer = new TableViewer(tableComposite, SWT.FULL_SELECTION | SWT.MULTI | SWT.V_SCROLL);
		tableViewer.setContentProvider(new ArrayContentProvider());

		// Configure underlying SWT table
		final Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		// Create column "Name"
		TableViewerColumn colName = new TableViewerColumn(tableViewer, SWT.NONE);
		colName.getColumn().setText(AddressBookMessages.Name);
		colName.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				cell.setText(((Address) cell.getElement()).getName());
			}

		});

		// Setup layout to use percental column widths
		// Composite with TableColumnLayout may only include the Table widget!
		TableColumnLayout tableLayout = new TableColumnLayout();
		tableComposite.setLayout(tableLayout);
		tableLayout.setColumnData(colName.getColumn(), new ColumnWeightData(100));

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

		// Create a context menu using the JFace MenuManager and add a separator
		// as placeholder for contributions
		final MenuManager menuManager = new MenuManager();
		menuManager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		// Create a SWT menu and set it to the table widget
		table.setMenu(menuManager.createContextMenu(table));
		// Register the context menu with the workbench so contributions can be
		// made declaratively using org.eclipse.ui.menus
		getSite().registerContextMenu(menuManager, tableViewer);
		getSite().setSelectionProvider(tableViewer);

		// Register for update events to refresh the view contents automatically
		addressService.addAddressChangeListener(addressChangeListener);

		refresh();
	}

	@Override
	public void dispose() {
		super.dispose();
		// Remove the change listener because otherwise the address service
		// would call the view object even when it is already gone
		addressService.removeAddressChangeListener(addressChangeListener);
	}

	public void setAddressService(IAddressService addressService) {
		this.addressService = addressService;
	}

	@Override
	public void setFocus() {
		tableViewer.getTable().setFocus();
	}

	public void refresh() {
		new LoadAddressesJob().schedule();
	}
}