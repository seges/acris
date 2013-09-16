package sk.seges.acris.widget.client.celltable.column;

import sk.seges.acris.common.util.Triple;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;

public interface DynamicColumnDefinitionWithFooterButton extends DynamicColumDefinition{
	
	public Triple<Button, Integer, ClickHandler> getFooterButton();

	public void setFooterButton(Triple<Button, Integer, ClickHandler> footerButton);
	
}
