package sk.seges.acris.widget.client.celltable;

import java.util.Map;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.Column;

public class DynamicCellTable extends AbstractFilterableTable<Map<String, Object>> {

	public static final String STRING = "STRING";
	public static final String DATE = "DATE";
	public static final String NUMBER = "NUMBER";
	
	static class DynamicCellTableKeyProvider implements ProvidesIdentifier<Map<String, Object>> {
		@Override
		public Object getKey(Map<String, Object> item) {
			return item.get(ID);
		}

		@Override
		public String getKeyColumnName() {
			return ID;
		}
	}
	
	public void init() {
		initialize();
	}

	public DynamicCellTable() {
		super(new DynamicCellTableKeyProvider(), Map.class);
	}
	
	public DynamicCellTable(boolean sortable) {
		super(new DynamicCellTableKeyProvider(), Map.class, sortable);
	}

	public void setColumns(Map<String, String[]> columns) {
		int colCount = getColumnCount();
		for (int i = 0; i < colCount; i++) {
			removeColumn(0);
		}
		for (final String column : columns.keySet()) {
			if (columns.get(column) == null) {
				continue;
			}
			if (columns.get(column)[0].toUpperCase().equals(STRING)) {
				Column<Map<String, Object>, String> col = new Column<Map<String, Object>, String>(new TextCell()) {
					@Override
					public String getValue(Map<String, Object> arg0) {
						return (String) arg0.get(column);
					}
				};
				addTextColumn(col, 100 / (columns.size()-1), columns.get(column)[1], column);
			} else if (columns.get(column)[0].toUpperCase().equals(DATE)) {

			} else if (columns.get(column)[0].toUpperCase().equals(NUMBER)) {

			}
		}
		addCheckboxColumn(50);

	}

}
