package de.ralfebert.rcpsnippets.internal.snippet01tableproperties;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.part.ViewPart;

import de.ralfebert.rcputils.properties.PropertyCellLabelProvider;
import de.ralfebert.rcputils.properties.PropertyEditingSupport;
import de.ralfebert.rcputils.random.RandomData;

public class Snippet01TablePropertiesViewPart extends ViewPart {

	private TableViewer tableViewer;
	private Table table;

	@Override
	public void createPartControl(Composite parent) {

		TableColumnLayout tableLayout = new TableColumnLayout();
		parent.setLayout(tableLayout);

		tableViewer = new TableViewer(parent);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableViewerColumn colName = new TableViewerColumn(tableViewer, SWT.NONE);
		colName.getColumn().setText("Stadt");
		colName.setLabelProvider(new PropertyCellLabelProvider("name"));
		colName.setEditingSupport(new PropertyEditingSupport(tableViewer, "name", new TextCellEditor(table)));
		tableLayout.setColumnData(colName.getColumn(), new ColumnWeightData(100));

		TableViewerColumn colPopulation = new TableViewerColumn(tableViewer, SWT.RIGHT);
		colPopulation.getColumn().setText("Einwohner");
		colPopulation.setLabelProvider(new PropertyCellLabelProvider("stats.population"));
		tableLayout.setColumnData(colPopulation.getColumn(), new ColumnPixelData(100));

		TableViewerColumn colArea = new TableViewerColumn(tableViewer, SWT.RIGHT);
		colArea.getColumn().setText("Fläche (km2)");
		colArea.setLabelProvider(new PropertyCellLabelProvider("stats.areaKm2"));
		tableLayout.setColumnData(colArea.getColumn(), new ColumnPixelData(100));

		TableViewerColumn colFoundingYear = new TableViewerColumn(tableViewer, SWT.RIGHT);
		colFoundingYear.getColumn().setText("Gründung am");
		colFoundingYear.setLabelProvider(new PropertyCellLabelProvider("foundingYear"));
		tableLayout.setColumnData(colFoundingYear.getColumn(), new ColumnPixelData(100));

		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setInput(createSomeData());

	}

	private List<City> createSomeData() {
		List<City> data = new ArrayList<City>();
		RandomData randomData = new RandomData();
		for (int i = 0; i < 5000; i++) {
			CityStats stats = new CityStats(randomData.someNumber(10000, 1000000), randomData.someNumber(100d, 800d));
			data.add(new City(randomData.someCity(), randomData.someDate(1200, 1600), stats));
			randomData.newData();
		}
		return data;
	}

	@Override
	public void setFocus() {
		table.setFocus();
	}

}
