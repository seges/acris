package sk.seges.acris.widget.client.celltable.formatter;

public interface CellFormatter<CellValue, ModelData> {

	String getCellValue(CellValue cellValue, ModelData model);

}