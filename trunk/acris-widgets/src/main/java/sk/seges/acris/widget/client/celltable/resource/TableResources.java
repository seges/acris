package sk.seges.acris.widget.client.celltable.resource;

import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.cellview.client.CellTable.Style;

public interface TableResources extends Resources {

	public interface CellTableStyle extends Style {
	};

	@Source({ "celltable.css" })
	CellTableStyle cellTableStyle();
}