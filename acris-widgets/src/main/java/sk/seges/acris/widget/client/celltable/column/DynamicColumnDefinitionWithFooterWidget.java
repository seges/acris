package sk.seges.acris.widget.client.celltable.column;

import com.google.gwt.user.client.ui.Widget;

public interface DynamicColumnDefinitionWithFooterWidget extends DynamicColumDefinition {

	public Widget getFooterWidget();

	public void setFooterWidget(Widget footerWidget);
}
