package de.rcpbuch.addressbook.internal.address.list;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.dialogs.ListDialog;
import org.eclipse.ui.part.ViewPart;

import de.ralfebert.rcputils.properties.PropertyCellLabelProvider;
import de.ralfebert.rcputils.properties.PropertyEditingSupport;
import de.rcpbuch.addressbook.services.IAddressChangeListener;
import de.rcpbuch.addressbook.services.IAddressService;

public class AddressListViewPart extends ViewPart {

	private IAddressService addressService;

	private final IAddressChangeListener ADDRESS_CHANGE_LISTENER = new IAddressChangeListener() {

		public void addressesChanged() {
			updateUi();
		}

	};

	private TableViewer tableViewer;

	public AddressListViewPart() {

	}

	@Override
	public void createPartControl(Composite parent) {

		TableColumnLayout tableLayout = new TableColumnLayout();
		parent.setLayout(tableLayout);

		tableViewer = new TableViewer(parent, SWT.FULL_SELECTION | SWT.MULTI | SWT.V_SCROLL);
		tableViewer.getTable().setData("org.eclipse.swtbot.widget.key", "adressen");
		final Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableViewerColumn colName = new TableViewerColumn(tableViewer, SWT.NONE);
		colName.getColumn().setText("Name");

		colName.setLabelProvider(new PropertyCellLabelProvider("name"));
		tableLayout.setColumnData(colName.getColumn(), new ColumnWeightData(40));

		TableViewerColumn colStreet = new TableViewerColumn(tableViewer, SWT.NONE);
		colStreet.getColumn().setText("Straße");
		colStreet.setLabelProvider(new PropertyCellLabelProvider("street"));
		colStreet.setEditingSupport(new PropertyEditingSupport(tableViewer, "street", new TextCellEditor(table)));

		tableLayout.setColumnData(colStreet.getColumn(), new ColumnWeightData(20));

		TableViewerColumn colZip = new TableViewerColumn(tableViewer, SWT.NONE);
		colZip.getColumn().setText("PLZ");
		colZip.setLabelProvider(new PropertyCellLabelProvider("zip"));
		colZip.setEditingSupport(new PropertyEditingSupport(tableViewer, "zip", new TextCellEditor(table)));

		tableLayout.setColumnData(colZip.getColumn(), new ColumnPixelData(80));

		TableViewerColumn colCity = new TableViewerColumn(tableViewer, SWT.NONE);
		colCity.getColumn().setText("Ort");
		colCity.setLabelProvider(new PropertyCellLabelProvider("city"));
		tableLayout.setColumnData(colCity.getColumn(), new ColumnWeightData(20));

		TableViewerColumn colCountry = new TableViewerColumn(tableViewer, SWT.NONE);
		colCountry.getColumn().setText("Land");
		colCountry.setLabelProvider(new PropertyCellLabelProvider("country.name"));
		tableLayout.setColumnData(colCountry.getColumn(), new ColumnWeightData(20));

		final DialogCellEditor cityCellEditor = new DialogCellEditor(table) {

			@Override
			protected Object openDialogBox(Control cellEditorWindow) {
				ListDialog listDialog = new ListDialog(cellEditorWindow.getShell());
				listDialog.setTitle("Stadt auswählen");
				listDialog.setMessage("Bitte wählen Sie eine Stadt aus:");
				listDialog.setContentProvider(new ArrayContentProvider());
				listDialog.setLabelProvider(new LabelProvider());
				listDialog.setInput(addressService.getAllCities());
				if (listDialog.open() == Dialog.OK && listDialog.getResult().length > 0) {
					return listDialog.getResult()[0];
				} else {
					MessageDialog.openError(cellEditorWindow.getShell(), "Fehler", "Bitte wählen Sie eine Stadt aus!");
					return null;
				}
			}

		};

		colCity.setEditingSupport(new PropertyEditingSupport(tableViewer, "city", cityCellEditor));

		tableViewer.setContentProvider(new ArrayContentProvider());

		// Kontextmenü für Contributions vorbereiten
		MenuManager menuManager = new MenuManager();
		menuManager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		table.setMenu(menuManager.createContextMenu(table));
		getSite().registerContextMenu(menuManager, tableViewer);
		getSite().setSelectionProvider(tableViewer);

		addressService.addAddressChangeListener(ADDRESS_CHANGE_LISTENER);

		updateUi();
	}

	@Override
	public void dispose() {
		super.dispose();
		addressService.removeAddressChangeListener(ADDRESS_CHANGE_LISTENER);
	}

	@Override
	public void setFocus() {
		tableViewer.getTable().setFocus();
	}

	public void updateUi() {
		tableViewer.setInput(addressService.getAllAddresses());
	}

	public void setAddressService(IAddressService addressService) {
		this.addressService = addressService;
	}

}