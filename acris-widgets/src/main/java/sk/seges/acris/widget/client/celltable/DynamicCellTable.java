package sk.seges.acris.widget.client.celltable;

import java.util.Map;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.view.client.ProvidesKey;

public class DynamicCellTable extends AbstractFilterableTable<Map<String, Object>> {
	
	public static String STRING = "STRING";
	public static String DATE = "DATE";
	public static String NUMBER = "NUMBER";

	public DynamicCellTable(Map<String, String> columns) {
		super(new ProvidesKey<Map<String, Object>>() {
			@Override
			public Object getKey(Map<String, Object> arg0) {
				return arg0.get("id");
			}}, Object.class);
		
		setColumns(columns);

	}
	
	public void setColumns(Map<String, String> columns) {
		for (int i = 0; i < getColumnCount(); i++) {
			removeColumn(i);
		}
		for (final String column : columns.keySet()) {
			if (columns.get(column) == null) {
				continue;
			}
			if (columns.get(column).toUpperCase().equals(STRING)) {
				Cell<String> c = new TextCell(); 
				addTextColumn(new Column<Map<String, Object>, String>(c) {
					@Override
					public String getValue(Map<String, Object> arg0) {
						return (String) arg0.get(column);
					}}, 0, column, column);
			} else if (columns.get(column).toUpperCase().equals(DATE)) {
				
			} else if (columns.get(column).toUpperCase().equals(NUMBER)) {
				
			}
		}		
	}

}
