/**
 * 
 */
package sk.seges.acris.widget.client.table;

import com.google.gwt.gen2.table.client.FixedWidthFlexTable;
import com.google.gwt.gen2.table.client.FixedWidthGrid;
import com.google.gwt.gen2.table.client.TableDefinition;
import com.google.gwt.gen2.table.client.TableModel;

/**
 * Extended class for overriding default behaviour, e.g. header sorting.
 * 
 * @author ladislav.gazo
 */
public class PagingScrollTable<RowType> extends com.google.gwt.gen2.table.client.PagingScrollTable<RowType> {
	public PagingScrollTable(TableModel<RowType> tableModel,
			FixedWidthGrid dataTable, FixedWidthFlexTable headerTable,
			TableDefinition<RowType> tableDefinition,
			ScrollTableResources resources) {
		super(tableModel, dataTable, headerTable, tableDefinition, resources);
	}

	public PagingScrollTable(TableModel<RowType> tableModel,
			FixedWidthGrid dataTable, FixedWidthFlexTable headerTable,
			TableDefinition<RowType> tableDefinition) {
		super(tableModel, dataTable, headerTable, tableDefinition);
	}

	public PagingScrollTable(TableModel<RowType> tableModel,
			TableDefinition<RowType> tableDefinition,
			ScrollTableResources resources) {
		super(tableModel, tableDefinition, resources);
	}

	public PagingScrollTable(TableModel<RowType> tableModel,
			TableDefinition<RowType> tableDefinition) {
		super(tableModel, tableDefinition);
	}

	@Override
	protected boolean onHeaderSort(int row, int column) {
		return (row > 0 ? false: super.onHeaderSort(row, column));
	}
}
