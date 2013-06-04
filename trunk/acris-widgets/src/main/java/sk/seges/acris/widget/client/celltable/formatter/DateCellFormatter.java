package sk.seges.acris.widget.client.celltable.formatter;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

public class DateCellFormatter<T> implements CellFormatter<Date, T> {

	private DateTimeFormat format;
	
	private DateTimeFormat ensureFormat() {
		if (format == null) {
			format = getFormat();
		}
		
		return format;
	}
	
	protected DateTimeFormat getFormat() {
		return DateTimeFormat.getFormat("dd.MM.yyyy");
	}
	
	@Override
	public String getCellValue(Date cellValue, T model) {
		if (cellValue == null) {
			return "Nedefinovaný";
		}
		return ensureFormat().format(cellValue);
	}
}