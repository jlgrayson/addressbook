package com.example.addressbook.internal.address.list;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.dialogs.ListDialog;
import org.eclipse.ui.part.ViewPart;

import com.example.addressbook.entities.Address;
import com.example.addressbook.services.AddressbookServices;
import com.example.addressbook.services.IAddressChangeListener;

public class AddressListViewPart extends ViewPart {

	private final IAddressChangeListener ADDRESS_CHANGE_LISTENER = new IAddressChangeListener() {

		public void addressesChanged() {
			updateUi();
		}

	};

	private TableViewer tableViewer;

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
		colName.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				Address address = (Address) cell.getElement();
				cell.setText(address.getName());
			}

		});
		tableLayout.setColumnData(colName.getColumn(), new ColumnWeightData(50));

		TableViewerColumn colStreet = new TableViewerColumn(tableViewer, SWT.NONE);
		colStreet.getColumn().setText("Straße");
		colStreet.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				Address address = (Address) cell.getElement();
				cell.setText(address.getStreet());
			}

		});
		tableLayout.setColumnData(colStreet.getColumn(), new ColumnWeightData(25));

		TableViewerColumn colZip = new TableViewerColumn(tableViewer, SWT.NONE);
		colZip.getColumn().setText("PLZ");
		colZip.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				Address address = (Address) cell.getElement();
				cell.setText(address.getZip());
			}

		});
		tableLayout.setColumnData(colZip.getColumn(), new ColumnPixelData(80));

		TableViewerColumn colCity = new TableViewerColumn(tableViewer, SWT.NONE);
		colCity.getColumn().setText("Ort");
		colCity.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				Address address = (Address) cell.getElement();
				cell.setText(address.getCity());
			}

		});
		tableLayout.setColumnData(colCity.getColumn(), new ColumnWeightData(25));

		colCity.setEditingSupport(new EditingSupport(tableViewer) {

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return new DialogCellEditor(table) {

					@Override
					protected Object openDialogBox(Control cellEditorWindow) {
						ListDialog listDialog = new ListDialog(cellEditorWindow.getShell());
						listDialog.setTitle("Stadt auswählen");
						listDialog.setMessage("Bitte wählen Sie eine Stadt aus:");
						listDialog.setContentProvider(new ArrayContentProvider());
						listDialog.setLabelProvider(new LabelProvider());
						listDialog.setInput(AddressbookServices.getAddressService().getAllCities());
						if (listDialog.open() == Dialog.OK && listDialog.getResult().length > 0) {
							return listDialog.getResult()[0];
						} else {
							MessageDialog.openError(cellEditorWindow.getShell(), "Fehler",
									"Bitte wählen Sie eine Stadt aus!");
							return null;
						}
					}

				};
			}

			@Override
			protected Object getValue(Object element) {
				Address address = ((Address) element);
				return address.getCity();
			}

			@Override
			protected void setValue(Object element, Object value) {
				Address address = (Address) element;
				address.setCity(String.valueOf(value));
				tableViewer.refresh(element);
			}

		});

		tableViewer.setContentProvider(new ArrayContentProvider());

		// Kontextmenü für Contributions vorbereiten
		MenuManager menuManager = new MenuManager();
		menuManager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		table.setMenu(menuManager.createContextMenu(table));
		getSite().registerContextMenu(menuManager, tableViewer);
		getSite().setSelectionProvider(tableViewer);

		AddressbookServices.getAddressService().addAddressChangeListener(ADDRESS_CHANGE_LISTENER);

		updateUi();
	}

	@Override
	public void dispose() {
		super.dispose();
		AddressbookServices.getAddressService().removeAddressChangeListener(ADDRESS_CHANGE_LISTENER);
	}

	@Override
	public void setFocus() {
		tableViewer.getTable().setFocus();
	}

	public void updateUi() {
		tableViewer.setInput(AddressbookServices.getAddressService().getAllAddresses());
	}

}