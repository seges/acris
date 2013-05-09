package sk.seges.acris.widget.client.celltable;

import java.util.Date;
import java.util.List;
import java.util.Map;

import sk.seges.acris.widget.client.celltable.column.ColumnValuesRemoteLoaderAsync;
import sk.seges.acris.widget.client.celltable.column.DynamicColumDefinition;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;

public enum ColumnType {
	STRING {
		public Column<Map<String, Object>, ? extends Object> createColumn(final DynamicColumDefinition column) {
			return new Column<Map<String, Object>, String>(new TextCell()) {
				@Override
				public String getValue(Map<String, Object> arg0) {
					return (String) arg0.get(column.getField());
				}
			};
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T extends Map<String, Object>> void addColumn(AbstractFilterableTable<T> table, DynamicColumDefinition column, int columnCount, ColumnValuesRemoteLoaderAsync valuesLoader) {
			table.addTextColumn((Column<T, ?>) createColumn(column), 100 / (columnCount - 1), column.getLabel(), column.getField());
		}

	}, DATE {
		public Column<Map<String, Object>, ? extends Object> createColumn(final DynamicColumDefinition column) {
			return new Column<Map<String, Object>, Date>(new DateCell()) {
				@Override
				public Date getValue(Map<String, Object> arg0) {
					return (Date) arg0.get(column.getField());
				}
			};
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T extends Map<String, Object>> void addColumn(AbstractFilterableTable<T> table, DynamicColumDefinition column, int columnCount, ColumnValuesRemoteLoaderAsync valuesLoader) {
			table.addDateColumn((Column<T, ?>) createColumn(column), 100 / (columnCount - 1), column.getLabel(), column.getField());	
		}
	}, NUMBER {
		public Column<Map<String, Object>, ? extends Object> createColumn(final DynamicColumDefinition column) {
			return new Column<Map<String, Object>, Number>(new NumberCell()) {
				@Override
				public Number getValue(Map<String, Object> arg0) {
					return (Number) arg0.get(column.getField());
				}
			};
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T extends Map<String, Object>> void addColumn(AbstractFilterableTable<T> table, DynamicColumDefinition column, int columnCount, ColumnValuesRemoteLoaderAsync valuesLoader) {
			table.addTextColumn((Column<T, ?>) createColumn(column), 100 / (columnCount - 1), column.getLabel(), column.getField());
		}
		
	}, ENUM {
		public Column<Map<String, Object>, ? extends Object> createColumn(final DynamicColumDefinition column) {
			return new Column<Map<String, Object>, String>(new TextCell()) {
				@Override
				public String getValue(Map<String, Object> arg0) {
					return arg0.get(column.getField()).toString();
				}
			};
		}

		@Override
		public <T extends Map<String, Object>> void addColumn(final AbstractFilterableTable<T> table, final DynamicColumDefinition column, final int columnCount, ColumnValuesRemoteLoaderAsync valuesLoader) {
	
			valuesLoader.loadColumnValues(table.getDataClass(), column.getField(), new AsyncCallback<List<String>>() {
				
				@SuppressWarnings("unchecked")
				@Override
				public void onSuccess(List<String> result) {
					table.addSelectionColumn((Column<T, ?>) createColumn(column), 100 / (columnCount - 1), column.getLabel(), column.getField(), result);
				}
				
				@Override
				public void onFailure(Throwable caught) {}
			});
		}
	};
	
	public static ColumnType fromString(String name) {
		for (ColumnType columnType: ColumnType.values()) {
			if (columnType.name().toLowerCase().equals(name.toLowerCase())) {
				return columnType;
			}
		}
		
		throw new RuntimeException("Unsupported column type: " + name);
	}
	
	public abstract <T extends Map<String, Object>> void addColumn(AbstractFilterableTable<T> table, final DynamicColumDefinition column, int columnCount, ColumnValuesRemoteLoaderAsync valuesLoader);
}