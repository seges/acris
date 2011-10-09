package sk.seges.acris.theme.client.metal;

import sk.seges.acris.theme.client.annotation.ThemeSupport;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

//@ThemeSupport(widgetClass = SimplePanel.class, elementName = "panel", themeName = MetalTheme.NAME)
public class MetalSimplePanel {

	private FlowPanel titlePanel;
	
	public SimplePanel createWidgetWrapper() {
		SimplePanel panelW = new SimplePanel();
		panelW.addStyleName("acris-metal-theme-simple-panel");
		FlowPanel panelWrapper = new FlowPanel();
		titlePanel = new FlowPanel();
		panelWrapper.add(titlePanel);
		
		if (titleWidget != null) {
			setTitleWidget(titleWidget);
		}
		
		SimplePanel simplePanel = new SimplePanel();
		
		panelWrapper.add(simplePanel);
		panelW.add(panelWrapper);
		return simplePanel;
	}

	private Widget titleWidget;
	
	public void setTitleWidget(Widget widget) {
		titleWidget = widget;
		if (titlePanel != null) {
			titlePanel.clear();
			titlePanel.add(widget);
		}
	}
	
	public void setTitle(String text) {
		HTML titleText = new HTML(text);
		setTitleWidget(titleText);
	}
}
