package sk.seges.acris.theme.client.metal;

import sk.seges.acris.widget.client.Cleaner;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.SimplePanel;

//@ThemeSupport(widgetClass = FocusPanel.class, elementName = "focusPanel", themeName = MetalTheme.NAME)
public class MetalFocusPanel extends FocusPanel {

	private SimplePanel toolbarPanel;
	private FlowPanel metalFocusPanelContent;
	
	public FlowPanel createWidgetWrapper() {
		toolbarPanel = new SimplePanel();
		toolbarPanel.setStyleName("acris-metal-focus-panel");
		
		FlowPanel t = new FlowPanel();

		FlowPanel center = new FlowPanel();
		center.addStyleName("acris-metal-focus-panel-center");
		
		SimplePanel leftBorder = new SimplePanel();
		leftBorder.addStyleName("acris-metal-focus-panel-left-border");

		SimplePanel rightBorder = new SimplePanel();
		rightBorder.addStyleName("acris-metal-focus-panel-right-border");
		

		
		metalFocusPanelContent = new FlowPanel();
		metalFocusPanelContent.addStyleName("acris-metal-focus-panel-content");
		rightBorder.add(metalFocusPanelContent);
		leftBorder.add(rightBorder);
		center.add(leftBorder);
		
		
		FlowPanel bottom = new FlowPanel();
		bottom.addStyleName("acris-metal-focus-panel-bottom");

		SimplePanel leftCorner = new SimplePanel();
		leftCorner.addStyleName("acris-metal-focus-panel-left-corner");
		bottom.add(leftCorner);

		SimplePanel rightCorner = new SimplePanel();
		rightCorner.addStyleName("acris-metal-focus-panel-right-corner");
		bottom.add(rightCorner);
		
		Cleaner cl = new Cleaner();
		bottom.add(cl);

		
		t.add(center);
		t.add(bottom);
		

		toolbarPanel.add(t);
		return metalFocusPanelContent;
	}
}
