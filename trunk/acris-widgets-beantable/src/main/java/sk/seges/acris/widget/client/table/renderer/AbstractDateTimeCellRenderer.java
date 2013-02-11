/**
 * 
 */
package sk.seges.acris.widget.client.table.renderer;

import java.util.Date;

import com.google.gwt.gen2.table.client.CellRenderer;
import com.google.gwt.gen2.table.client.ColumnDefinition;
import com.google.gwt.gen2.table.client.TableDefinition.AbstractCellView;
import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * @author stefan.sivak
 *
 */
public abstract class AbstractDateTimeCellRenderer<RowType> implements CellRenderer<RowType, Date> {
	private final DateTimeFormat dateTimeFormat;

	public AbstractDateTimeCellRenderer(String format) {
		dateTimeFormat = DateTimeFormat.getFormat(format);
	}

	public AbstractDateTimeCellRenderer(DateTimeFormat dateTimeFormat) {
		this.dateTimeFormat = dateTimeFormat;
	}

	@Override
	public void renderRowValue(RowType rowValue, ColumnDefinition<RowType, Date> columnDef,
			AbstractCellView<RowType> view) {
		Date cellValue = columnDef.getCellValue(rowValue);
		if (cellValue == null) {
			view.setHTML("&nbsp;");
		} else {
			if (dateTimeFormat != null) {
				view.setHTML(dateTimeFormat.format(cellValue));
			} else {
				view.setHTML(cellValue.toString());
			}
		}
	} 

}
