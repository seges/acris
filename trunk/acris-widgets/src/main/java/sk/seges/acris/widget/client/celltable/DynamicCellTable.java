package sk.seges.acris.widget.client.celltable;

import java.util.List;
import java.util.Map;

import sk.seges.acris.widget.client.celltable.column.ColumnValuesRemoteLoaderAsync;
import sk.seges.acris.widget.client.celltable.column.DynamicColumDefinition;
import sk.seges.acris.widget.client.celltable.resource.TableResources;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.cellview.client.Header;

public class DynamicCellTable extends AbstractFilterableTable<Map<String, Object>> {

	private final ColumnValuesRemoteLoaderAsync valuesLoader;
	
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
	
	protected DynamicCellTable(ColumnValuesRemoteLoaderAsync valuesLoader, boolean multiselect, boolean sortable, TableResources resources){
		super(new DynamicCellTableKeyProvider(), Map.class, multiselect, resources, sortable);
		this.valuesLoader = valuesLoader;
	}

	@Override
	protected void doSetHeaderVisible(boolean isFooter, boolean isVisible) {
		super.doSetHeaderVisible(isFooter, isVisible);
		
		int count = getColumnCount();
		for (int i = 0; i < count; i++) {
			Header<?> header = getHeader(i);
			if (header instanceof AttachableHeader) {
				((AttachableHeader)header).onAttachHeader(Element.as(getTableHeadElement().getChild(0).getChild(i)));
			}
		}
	}
	
	public void setColumns(List<DynamicColumDefinition> columns) {
		int colCount = getColumnCount();
		for (int i = 0; i < colCount; i++) {
			removeColumn(0);
		}
		addColumns(columns);
		addCheckboxColumn(50);

	}

	protected void addColumns(List<DynamicColumDefinition> columns) {
		for (final DynamicColumDefinition column : columns) {
			if (column.getField() == null) {
				continue;
			}
			ColumnType.fromString(column.getType()).addColumn(this, column, columns.size(), valuesLoader);
		}
	}
}