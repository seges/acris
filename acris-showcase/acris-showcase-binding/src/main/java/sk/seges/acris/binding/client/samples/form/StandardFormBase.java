package sk.seges.acris.binding.client.samples.form;

import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;

class StandardFormBase extends AbstractFormBase {

	private int rowCounter = 0;

	private static final int COLUMN_COUNT = 2;
	
	public StandardFormBase() {
		super();
	}
	
	public void setWidget(int row, String text, Widget widget) {
		if (row > rowCounter) {
			rowCounter = row;
			grid.resize(rowCounter+1, COLUMN_COUNT);
		}
		grid.setWidget(row, 0, new Label(text));
		grid.setWidget(row, 1, widget);
		rowCounter++;
	}

	public void addWidget(String text, Widget widget) {
		addWidget(new Label(text), widget);
	}

	public void addWidget(Label label, Widget widget) {
		grid.resize(rowCounter+1, COLUMN_COUNT);
		grid.setWidget(rowCounter, 0, label);
		grid.setWidget(rowCounter, 1, widget);
		rowCounter++;
	}

	public void addWidget(Widget widget) {
		addWidget(widget, null);
	}

	public void addWidget(Widget widget, HorizontalAlignmentConstant alignment) {
		grid.resize(rowCounter+1, COLUMN_COUNT);
		grid.setWidget(rowCounter, 1, widget);
		if (alignment != null) {
	        final HTMLTable.CellFormatter cellFormatter = grid.getCellFormatter();
	        cellFormatter.setHorizontalAlignment(rowCounter, 1, alignment);
		}
		rowCounter++;
	}
}