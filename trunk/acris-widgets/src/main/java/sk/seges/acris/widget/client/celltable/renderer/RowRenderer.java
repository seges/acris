package sk.seges.acris.widget.client.celltable.renderer;

import com.google.gwt.dom.client.Style;

public interface RowRenderer<T> {

	void renderRow(Style style, T model);

}