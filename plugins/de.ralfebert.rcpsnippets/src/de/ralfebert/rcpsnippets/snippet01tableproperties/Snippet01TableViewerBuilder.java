package de.ralfebert.rcpsnippets.snippet01tableproperties;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import de.ralfebert.rcputils.builder.table.ColumnBuilder;
import de.ralfebert.rcputils.builder.table.ICellFormatter;
import de.ralfebert.rcputils.builder.table.TableViewerBuilder;
import de.ralfebert.rcputils.builder.table.format.Formatter;
import de.ralfebert.rcputils.builder.table.format.StringValueFormatter;
import de.ralfebert.rcputils.properties.BaseValue;
import de.ralfebert.rcputils.random.RandomData;

public class Snippet01TableViewerBuilder extends ViewPart {

	private TableViewer tableViewer;

	@Override
	public void createPartControl(Composite parent) {

		TableViewerBuilder t = new TableViewerBuilder(parent);

		ColumnBuilder city = t.createColumn("Stadt");
		city.bindToProperty("name");
		city.setPercentWidth(60);
		city.makeEditable();
		city.useAsDefaultSortColumn();
		city.build();

		ColumnBuilder population = t.createColumn("Einwohner");
		population.bindToProperty("stats.population");
		population.alignRight();
		population.format(new ICellFormatter() {

			public void formatCell(ViewerCell cell, Object value) {
				int population = (Integer) value;
				int color = (population > 5000000) ? SWT.COLOR_RED : SWT.COLOR_BLACK;
				cell.setForeground(cell.getControl().getDisplay().getSystemColor(color));
			}

		});
		population.format(Formatter.forInt(new DecimalFormat("#,##0")));
		population.makeEditable(Formatter.forInt());
		population.build();

		ColumnBuilder area = t.createColumn("Fläche");
		area.bindToProperty("stats.areaKm2");
		area.alignRight();
		area.format(Formatter.forDouble(new DecimalFormat("0.00 km²")));
		area.makeEditable(Formatter.forDouble(new DecimalFormat("0.00")));
		area.build();

		ColumnBuilder density = t.createColumn("Ew./km²");
		density.bindToValue(new BaseValue<City>() {

			@Override
			public Object get(City element) {
				return element.getStats().getPopulation() / element.getStats().getAreaKm2();
			}

		});
		density.format(Formatter.forDouble(new DecimalFormat("0.0")));
		density.alignRight();
		density.build();

		ColumnBuilder foundingDate = t.createColumn("Gründung am");
		foundingDate.bindToProperty("foundingDate");
		foundingDate.setPixelWidth(100);
		StringValueFormatter dateFormat = Formatter.forDate(SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM));
		foundingDate.format(dateFormat);
		foundingDate.makeEditable(dateFormat);
		foundingDate.build();

		ColumnBuilder neighborCity = t.createColumn("Nachbarstadt");
		neighborCity.bindToProperty("neighborCity");
		neighborCity.setPercentWidth(40);
		ComboBoxViewerCellEditor cityComboEditor = new ComboBoxViewerCellEditor(t.getTable(), SWT.READ_ONLY);
		cityComboEditor.setContenProvider(new ArrayContentProvider());
		cityComboEditor.setLabelProvider(new LabelProvider());
		cityComboEditor.setInput(RandomData.CITIES);
		neighborCity.makeEditable(cityComboEditor);
		neighborCity.build();

		tableViewer = t.build(createSomeData());

	}

	private List<City> createSomeData() {
		List<City> data = new ArrayList<City>();
		RandomData randomData = new RandomData();
		for (int i = 0; i < 50; i++) {
			CityStats stats = new CityStats(randomData.someNumber(10000, 10000000), randomData.someNumber(100d, 800d));
			data.add(new City(randomData.someCity(), randomData.someDate(1200, 1600), stats, randomData.someCity()));
			randomData.newData();
		}
		return data;
	}

	@Override
	public void setFocus() {
		tableViewer.getTable().setFocus();
	}

}
