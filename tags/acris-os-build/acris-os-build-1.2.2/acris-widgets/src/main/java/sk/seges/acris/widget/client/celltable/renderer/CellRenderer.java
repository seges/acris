package sk.seges.acris.widget.client.celltable.renderer;

public interface CellRenderer<CellValue, ModelData> {

	void renderCell(com.google.gwt.dom.client.Style style, CellValue cellValue, ModelData model);

}