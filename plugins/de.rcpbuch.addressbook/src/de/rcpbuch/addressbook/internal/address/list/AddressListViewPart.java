package de.rcpbuch.addressbook.internal.address.list;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.dialogs.ListDialog;
import org.eclipse.ui.part.ViewPart;

import de.ralfebert.rcputils.builder.table.TableViewerBuilder;
import de.ralfebert.rcputils.properties.PropertyLabelProvider;
import de.ralfebert.rcputils.properties.PropertyValueFormatter;
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

		TableViewerBuilder t = new TableViewerBuilder(parent);
		t.getTable().setData("org.eclipse.swtbot.widget.key", "adressen");

		t.createColumn("Name").bindToProperty("name").setPercentWidth(30).build();
		t.createColumn("Straße").bindToProperty("street").setPercentWidth(20).makeEditable().build();
		t.createColumn("PLZ").bindToProperty("zip").setPercentWidth(10).makeEditable().build();

		final DialogCellEditor cityCellEditor = new DialogCellEditor(t.getTable()) {

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

		t.createColumn("Stadt").bindToProperty("city").makeEditable(cityCellEditor).setPercentWidth(20).build();

		ComboBoxViewerCellEditor countryEditor = new ComboBoxViewerCellEditor(t.getTable(), SWT.READ_ONLY);
		countryEditor.setContenProvider(new ArrayContentProvider());
		countryEditor.setLabelProvider(new PropertyLabelProvider("name"));
		countryEditor.setInput(addressService.getAllCountries());

		t.createColumn("Land").bindToProperty("country").format(new PropertyValueFormatter("name")).makeEditable(
				countryEditor).setPercentWidth(20).build();

		tableViewer = t.getTableViewer();
		tableViewer.setContentProvider(new ArrayContentProvider());

		// Kontextmenü für Contributions vorbereiten
		MenuManager menuManager = new MenuManager();
		menuManager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		t.getTable().setMenu(menuManager.createContextMenu(t.getTable()));
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