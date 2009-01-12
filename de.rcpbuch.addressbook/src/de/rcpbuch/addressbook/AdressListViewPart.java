package de.rcpbuch.addressbook;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import de.rcpbuch.addressbook.data.Address;
import de.rcpbuch.addressbook.data.AddressbookServices;

public class AdressListViewPart extends ViewPart {

	public static final String VIEW_ID = AdressListViewPart.class.getName();

	public AdressListViewPart() {

	}

	@Override
	public void createPartControl(Composite parent) {

		TableViewer tableViewer = new TableViewer(parent);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);

		TableViewerColumn colName = new TableViewerColumn(tableViewer, SWT.NONE);
		colName.getColumn().setText("Name");
		colName.getColumn().setWidth(200);
		colName.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				Address address = (Address) cell.getElement();
				cell.setText(address.getName());
			}

		});

		TableViewerColumn colStreet = new TableViewerColumn(tableViewer, SWT.NONE);
		colStreet.getColumn().setText("Straße");
		colStreet.getColumn().setWidth(200);
		colStreet.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				Address address = (Address) cell.getElement();
				cell.setText(address.getStreet());
			}

		});

		TableViewerColumn colZipCity = new TableViewerColumn(tableViewer, SWT.NONE);
		colZipCity.getColumn().setText("PLZ/Ort");
		colZipCity.getColumn().setWidth(200);
		colZipCity.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				Address address = (Address) cell.getElement();
				cell.setText(address.getZip() + " " + address.getCity());
			}

		});

		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setInput(AddressbookServices.getAddressService().getAllAddresses());
	}

	@Override
	public void setFocus() {

	}

}
