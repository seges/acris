package sk.seges.acris.widget.client.optionpane;

import sk.seges.acris.widget.client.Cleaner;
import sk.seges.acris.widget.client.factory.WidgetFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class OptionsPanel extends FlowPanel {

	private static final String STYLE_SEPARATOR = "acris-cmp-option-separator";
	private static final String STYLE_OPTIONS = "acris-cmp-options";
	private static final String STYLE_OPTION = "acris-cmp-option";

	private Cleaner cleaner;
	
	public OptionsPanel() {
		this.setStyleName(STYLE_OPTIONS);
	}
	
	public void clearOptions() {
		clear();
	}

	/**
	 * Add an option to the list of options with hiding click listener. Usually it is a button at least
	 * closing the dialog.
	 * 
	 * @param option
	 * @param addHideAction If true dialog hiding click listener will be bundled with option.
	 */
	public HandlerRegistration addOption(Widget optionWidget, ClickHandler hidingClickHanler) {
		// FIXME: tmp hack...
		Widget origWidget = optionWidget;
		Widget option = optionWidget;
		if(option instanceof Button) {
			option = WidgetFactory.hackWidget(option);
		} else if(option.getStyleName().contains(WidgetFactory.HACK_WIDGET)) {
			origWidget = ((SimplePanel) option).getWidget();
		}
		
		removeCleaner();
		
		option.addStyleName(STYLE_OPTION);
		add(option);

		HandlerRegistration registration = null;
		
		if (hidingClickHanler != null) {
			if((origWidget instanceof HasClickHandlers)) {
				registration = ((HasClickHandlers)origWidget).addClickHandler(hidingClickHanler);
			}else {
				GWT.log("Widget does not implement " + HasClickHandlers.class.getName(), null);
			}
		}
		addCleaner();

		return registration;
	}
	
	public HandlerRegistration addOption(Widget option) {
		return addOption(option, null);
	}

	/**
	 * Add a set of options to the existing list of options.
	 * 
	 * @param options
	 */
	public void addOptions(Widget options[], ClickHandler hidingClickHanler) {
		for (Widget option : options) {
			addOption(option, hidingClickHanler);
		}
	}

	public void addOptions(Widget options[]) {
		addOptions(options, null);
	}

	/**
	 * Add a separator in the options pane of the dialog.
	 */
	public void addOptionSeparator() {
		SimplePanel separator = new SimplePanel();
		separator.setStyleName(STYLE_SEPARATOR);
		addOption(separator);
	}
	
	private void removeCleaner() {
		if (cleaner != null) {
			cleaner.removeFromParent();
		}		
	}
	
	private void addCleaner() {
		if (cleaner == null) {
			cleaner = new Cleaner();
		}
		add(cleaner);
	}
}