package sk.seges.acris.widget.client.celltable;

import java.util.List;
import java.util.Map;

import sk.seges.acris.common.util.Pair;
import sk.seges.acris.common.util.Triple;
import sk.seges.acris.widget.client.celltable.column.ColumnValuesRemoteLoaderAsync;
import sk.seges.acris.widget.client.celltable.column.DynamicColumDefinition;
import sk.seges.acris.widget.client.celltable.column.DynamicColumnDefinitionWithFooterButton;
import sk.seges.acris.widget.client.celltable.column.DynamicColumnDefinitionWithFooterWidget;
import sk.seges.acris.widget.client.celltable.resource.TableResources;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

public class DynamicCellTable extends AbstractFilterableTable<Map<String, Object>> {

	private final ColumnValuesRemoteLoaderAsync valuesLoader;
	protected boolean checkboxColumnAdded = false;
	
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

	public DynamicCellTable(ColumnValuesRemoteLoaderAsync valuesLoader) {
		super(new DynamicCellTableKeyProvider(), Map.class, false);
		this.valuesLoader = valuesLoader;
	}
	
	public DynamicCellTable(ColumnValuesRemoteLoaderAsync valuesLoader, boolean sortable) {
		super(new DynamicCellTableKeyProvider(), Map.class, false, sortable);
		this.valuesLoader = valuesLoader;
	}
	
	public DynamicCellTable(ColumnValuesRemoteLoaderAsync valuesLoader, boolean multiselect, boolean sortable) {
		super(new DynamicCellTableKeyProvider(), Map.class, multiselect, sortable);
		this.valuesLoader = valuesLoader;
	}
	
	protected DynamicCellTable(ColumnValuesRemoteLoaderAsync valuesLoader, boolean multiselect, boolean sortable, TableResources resources, boolean filterable){
		super(new DynamicCellTableKeyProvider(), Map.class, multiselect, resources, sortable, filterable);
		this.valuesLoader = valuesLoader;		
	}

	@Override
	protected void doSetHeaderVisible(boolean isFooter, boolean isVisible) {
		super.doSetHeaderVisible(isFooter, isVisible);
		
		int count = getColumnCount();
		for (int i = 0; i < count; i++) {
			if(isFooter){
				Header<?> footer = getFooter(i);
				if (footer instanceof AttachableHeader) {
					((AttachableHeader)footer).onAttachHeader(Element.as(getTableFootElement().getChild(0).getChild(i)));
				}
			}else{
				Header<?> header = getHeader(i);
				if (header instanceof AttachableHeader) {
					((AttachableHeader)header).onAttachHeader(Element.as(getTableHeadElement().getChild(0).getChild(i)));
				}
			}			
					
		}
	}
	
	public void setColumns(List<DynamicColumDefinition> columns) {
		int colCount = getColumnCount();
		for (int i = 0; i < colCount; i++) {
			removeColumn(0);
		}
		addColumns(columns);
		addCheckboxColumn(50, null);

	}

	protected void addColumns(List<DynamicColumDefinition> columns) {		
		for (int i = 0; i < columns.size(); i++) {
			final DynamicColumDefinition column = columns.get(i);
			if (column.getField() == null) {
				continue;
			}
			int columnIndex = i;
			if(checkboxColumnAdded){
				columnIndex = i + 1;
			}
			if (column instanceof DynamicColumnDefinitionWithFooterButton) {
				Triple<Button, Integer, ClickHandler> footerButton = ((DynamicColumnDefinitionWithFooterButton)column).getFooterButton();
				ColumnType.fromString(column.getType()).addColumn(this, column, columns.size(), columnIndex, valuesLoader, footerButton);
			} else if (column instanceof DynamicColumnDefinitionWithFooterWidget) {
				DynamicColumnDefinitionWithFooterWidget footerWidgetColumn = (DynamicColumnDefinitionWithFooterWidget) column;
				List<Pair<Widget, ClickHandler>> footerWidget = footerWidgetColumn.getFooterWidget();
				Integer colSpan = 1;
				if (footerWidgetColumn.getColSpan() != null) {
					colSpan = footerWidgetColumn.getColSpan();
				}
				ColumnType.fromString(column.getType()).addFooterWidgetColumn(this, column, columns.size(), columnIndex, valuesLoader, footerWidget, colSpan);
			} else {
				ColumnType.fromString(column.getType()).addColumn(this, column, columns.size(), columnIndex, valuesLoader, null);
			}
		}
	}
	
	@Override
	public void addCheckboxColumn(int width, Triple<Button, Integer, ClickHandler> footerButton) {
		super.addCheckboxColumn(width, footerButton);
		checkboxColumnAdded = true;
	}	
	
}