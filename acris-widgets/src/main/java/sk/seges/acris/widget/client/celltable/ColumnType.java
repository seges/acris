package sk.seges.acris.widget.client.celltable;

import java.util.Date;
import java.util.List;
import java.util.Map;

import sk.seges.acris.common.util.Pair;
import sk.seges.acris.common.util.Triple;
import sk.seges.acris.widget.client.celltable.column.ColumnValuesRemoteLoaderAsync;
import sk.seges.acris.widget.client.celltable.column.DynamicColumDefinition;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DateTimeFormat.PredefinedFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SimpleSafeHtmlRenderer;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

public enum ColumnType {
	BOOLEAN {
		public Column<Map<String, Object>, ? extends Object> createColumn(final DynamicColumDefinition column) {
			return new Column<Map<String, Object>, Boolean>(new BooleanCell()) {
				@Override
				public Boolean getValue(Map<String, Object> arg0) {
					return (Boolean) arg0.get(column.getField());
				}
			};
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T extends Map<String, Object>> void addColumn(AbstractFilterableTable<T> table, DynamicColumDefinition column, int columnCount, int columnIndex, 
				ColumnValuesRemoteLoaderAsync valuesLoader, Triple<Button, Integer, ClickHandler> footerButton, String locale, String webId) {
			table.addTextColumn((Column<T, ?>) createColumn(column), column.getWidth() != null ? column.getWidth() : 100 / (columnCount), column.getLabel(), column.getField(), footerButton);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public <T extends Map<String, Object>> void addFooterWidgetColumn(AbstractFilterableTable<T> table, DynamicColumDefinition column, int columnCount, int columnIndex,
				ColumnValuesRemoteLoaderAsync valuesLoader, List<Pair<Widget, ClickHandler>> footerWidget, Integer colSpan, String locale, String webId) {
			table.addFooterWidgetTextColumn((Column<T, ?>) createColumn(column), column.getWidth() != null ? column.getWidth() : 100 / (columnCount), column.getLabel(), column.getField(), footerWidget, colSpan);
		}

	},
	STRING {
		public Column<Map<String, Object>, ? extends Object> createColumn(final DynamicColumDefinition column) {
			return new Column<Map<String, Object>, String>(new TextCell()) {
				@Override
				public String getValue(Map<String, Object> arg0) {
					if(arg0 == null){
						return null;
					}
					return (String) arg0.get(column.getField());
				}
			};
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T extends Map<String, Object>> void addColumn(AbstractFilterableTable<T> table, DynamicColumDefinition column, int columnCount, int columnIndex, 
				ColumnValuesRemoteLoaderAsync valuesLoader, Triple<Button, Integer, ClickHandler> footerButton, String locale, String webId) {
			table.addTextColumn((Column<T, ?>) createColumn(column), column.getWidth() != null ? column.getWidth() : 100 / (columnCount), column.getLabel(), column.getField(), footerButton);
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T extends Map<String, Object>> void addFooterWidgetColumn(AbstractFilterableTable<T> table, DynamicColumDefinition column, int columnCount, int columnIndex,
				ColumnValuesRemoteLoaderAsync valuesLoader, List<Pair<Widget, ClickHandler>> footerWidget, Integer colSpan, String locale, String webId) {
			table.addFooterWidgetTextColumn((Column<T, ?>) createColumn(column), column.getWidth() != null ? column.getWidth() : 100 / (columnCount), column.getLabel(), column.getField(), footerWidget, colSpan);
		}
		
	}, DATE {
		public Column<Map<String, Object>, ? extends Object> createColumn(final DynamicColumDefinition column) {
			return new Column<Map<String, Object>, Date>(new DateCell(DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM))) {
				@Override
				public Date getValue(Map<String, Object> arg0) {
					return (Date) arg0.get(column.getField());
				}
			};
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T extends Map<String, Object>> void addColumn(AbstractFilterableTable<T> table, DynamicColumDefinition column, int columnCount, int columnIndex
				, ColumnValuesRemoteLoaderAsync valuesLoader, 
				Triple<Button, Integer, ClickHandler> footerButton, String locale, String webId) {
			table.addDateColumn((Column<T, ?>) createColumn(column), column.getWidth() != null ? column.getWidth() : 100 / (columnCount), column.getLabel(), column.getField(), footerButton);	
		}
		
		
		@SuppressWarnings("unchecked")
		@Override
		public <T extends Map<String, Object>> void addFooterWidgetColumn(AbstractFilterableTable<T> table, DynamicColumDefinition column, int columnCount, int columnIndex,
				ColumnValuesRemoteLoaderAsync valuesLoader, List<Pair<Widget, ClickHandler>> footerWidget, Integer colSpan, String locale, String webId) {
			table.addFooterWidgetDateColumn((Column<T, ?>) createColumn(column), column.getWidth() != null ? column.getWidth() : 100 / (columnCount), column.getLabel(), column.getField(), footerWidget, colSpan);
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
		public <T extends Map<String, Object>> void addColumn(AbstractFilterableTable<T> table, DynamicColumDefinition column, int columnCount, int columnIndex, 
				ColumnValuesRemoteLoaderAsync valuesLoader, Triple<Button, Integer, ClickHandler> footerButton, String locale, String webId) {
			table.addTextColumn((Column<T, ?>) createColumn(column), column.getWidth() != null ? column.getWidth() : 100 / (columnCount), column.getLabel(), column.getField(), footerButton);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public <T extends Map<String, Object>> void addFooterWidgetColumn(AbstractFilterableTable<T> table, DynamicColumDefinition column, int columnCount, int columnIndex,
				ColumnValuesRemoteLoaderAsync valuesLoader, List<Pair<Widget, ClickHandler>> footerWidget, Integer colSpan, String locale, String webId) {
			table.addFooterWidgetTextColumn((Column<T, ?>) createColumn(column), column.getWidth() != null ? column.getWidth() : 100 / (columnCount), column.getLabel(), column.getField(), footerWidget, colSpan);
		}
		
	}, ENUM {
		public Column<Map<String, Object>, ? extends Object> createColumn(final DynamicColumDefinition column) {
			return new Column<Map<String, Object>, String>(new TextCell()) {
				@Override
				public String getValue(Map<String, Object> arg0) {
					Object value = arg0.get(column.getField());
					if (value == null) {
						return "-";
					}
					return value.toString();
				}
			};
		}

		@Override
		public <T extends Map<String, Object>> void addColumn(final AbstractFilterableTable<T> table, final DynamicColumDefinition column, 
				final int columnCount, final int columnIndex, ColumnValuesRemoteLoaderAsync valuesLoader, final Triple<Button, Integer, ClickHandler> footerButton, String locale, String webId) {
	
			valuesLoader.loadColumnValues(table.getDataClass().getName(), column.getField(), locale, webId, new AsyncCallback<Map<String, String>>() {
				
				@SuppressWarnings("unchecked")
				@Override
				public void onSuccess(Map<String, String> result) {
					table.addSelectionColumn((Column<T, ?>) createColumn(column), column.getWidth() != null ? column.getWidth() : 100 / (columnCount), column.getLabel(), column.getField(), result, footerButton, columnIndex);
				}
				
				@Override
				public void onFailure(Throwable caught) {}
			});
		}
		
		@Override
		public <T extends Map<String, Object>> void addFooterWidgetColumn(final AbstractFilterableTable<T> table, final DynamicColumDefinition column, 
				final int columnCount, final int columnIndex,
				ColumnValuesRemoteLoaderAsync valuesLoader, final List<Pair<Widget, ClickHandler>> footerWidget, final Integer colSpan, String locale, String webId) {
	
			valuesLoader.loadColumnValues(table.getDataClass().getName(), column.getField(), locale, webId, new AsyncCallback<Map<String, String>>() {
				
				@SuppressWarnings("unchecked")
				@Override
				public void onSuccess(Map<String, String> result) {
					table.addFooterWidgetSelectionColumn((Column<T, ?>) createColumn(column), column.getWidth() != null ? column.getWidth() : 100 / (columnCount), column.getLabel(), column.getField(), result, footerWidget, columnIndex, colSpan);
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
	
	public abstract <T extends Map<String, Object>> void addColumn(AbstractFilterableTable<T> table, final DynamicColumDefinition column, int columnCount, int columnIndex, ColumnValuesRemoteLoaderAsync valuesLoader,
			Triple<Button, Integer, ClickHandler> footerButton, String locale, String webId);
	
	public abstract <T extends Map<String, Object>> void  addFooterWidgetColumn(AbstractFilterableTable<T> table, final DynamicColumDefinition column, int columnCount, int columnIndex, ColumnValuesRemoteLoaderAsync valuesLoader,
			List<Pair<Widget, ClickHandler>> footerWidget, Integer colSpan, String locale, String webId);
	
	public class BooleanCell extends AbstractCell<Boolean> {

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				Boolean value, SafeHtmlBuilder sb) {
			if (value != null) {
				sb.append(SimpleSafeHtmlRenderer.getInstance().render(
						value.toString()));
			}
		}
	}
}