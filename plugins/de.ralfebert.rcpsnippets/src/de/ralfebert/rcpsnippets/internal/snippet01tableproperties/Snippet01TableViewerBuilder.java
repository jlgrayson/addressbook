package de.ralfebert.rcpsnippets.internal.snippet01tableproperties;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import de.ralfebert.rcputils.properties.BaseValue;
import de.ralfebert.rcputils.random.RandomData;
import de.ralfebert.rcputils.tablebuilder.ColumnBuilder;
import de.ralfebert.rcputils.tablebuilder.ICellFormatter;
import de.ralfebert.rcputils.tablebuilder.TableViewerBuilder;

public class Snippet01TableViewerBuilder extends ViewPart {

	private TableViewer tableViewer;

	@Override
	public void createPartControl(Composite parent) {

		TableViewerBuilder t = new TableViewerBuilder(parent);

		ColumnBuilder city = t.createColumn("Stadt");
		city.bindToProperty("name");
		city.setPercentWidth(100);
		city.makeEditable();
		city.useAsDefaultSortColumn();
		city.build();

		ColumnBuilder population = t.createColumn("Einwohner");
		population.bindToProperty("stats.population");
		population.alignRight();
		population.format(new ICellFormatter() {

			public void formatCell(ViewerCell cell, Object value) {
				int population = (Integer) value;
				if (population > 5000000) {
					cell.setForeground(cell.getControl().getDisplay().getSystemColor(SWT.COLOR_RED));
				}
			}

		});
		population.format(new DecimalFormat("#,##0"));
		population.build();

		ColumnBuilder area = t.createColumn("Fläche");
		area.bindToProperty("stats.areaKm2");
		area.alignRight();
		area.format(new DecimalFormat("0.00 km²"));
		area.build();

		ColumnBuilder density = t.createColumn("Ew./km²");
		density.bindToValue(new BaseValue<City>() {

			@Override
			public Object get(City element) {
				return element.getStats().getPopulation() / element.getStats().getAreaKm2();
			}

		});
		density.format(new DecimalFormat("0.000"));
		density.alignRight();
		density.build();

		ColumnBuilder foundingDate = t.createColumn("Gründung am");
		foundingDate.bindToProperty("foundingDate");
		foundingDate.setPixelWidth(100);
		foundingDate.format(SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM));
		foundingDate.build();

		tableViewer = t.build(createSomeData());

	}

	private List<City> createSomeData() {
		List<City> data = new ArrayList<City>();
		RandomData randomData = new RandomData();
		for (int i = 0; i < 5000; i++) {
			CityStats stats = new CityStats(randomData.someNumber(10000, 10000000), randomData.someNumber(100d, 800d));
			data.add(new City(randomData.someCity(), randomData.someDate(1200, 1600), stats));
			randomData.newData();
		}
		return data;
	}

	@Override
	public void setFocus() {
		tableViewer.getTable().setFocus();
	}

}
