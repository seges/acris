package sk.seges.acris.widget.client.celltable.column;

import java.util.List;

import sk.seges.acris.common.util.Pair;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;

public interface DynamicColumnDefinitionWithFooterWidget extends DynamicColumDefinition {

	public List<Pair<Widget, ClickHandler>> getFooterWidget();

	public void setFooterWidget(List<Pair<Widget, ClickHandler>> footerWidget);

	public Integer getColSpan();

	public void setColSpan(Integer colSpan);
}
