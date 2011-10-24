package sk.seges.acris.widget.showcase.dynamicui;

import sk.seges.acris.widget.client.ElementFlowPanel;
import sk.seges.acris.widget.client.uibinder.DynamicUiBinder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author ladislav.gazo
 */
public class EventsInDynamicallyBoundPanel extends Composite {
	interface ComplexBoundPanelUiBinder extends DynamicUiBinder<Widget, EventsInDynamicallyBoundPanel> {}
	private static final ComplexBoundPanelUiBinder binder = GWT.create(ComplexBoundPanelUiBinder.class);
	
	@UiField
	ElementFlowPanel panel;
	
	@UiField
	Image image;
	
	public EventsInDynamicallyBoundPanel() {
		binder.setViewTemplate("<div ui:field='panel'>image panel</div><img ui:field='image' src='http://code.google.com/p/acris/logo?cct=1312404180'></img>");
		initWidget(binder.createAndBindUi(this));
		
		final Image image2 = new Image("http://acris.googlecode.com/svn/wiki/icons_grey/learnMore.png");
		image2.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				image2.setUrl("http://acris.googlecode.com/svn/wiki/icons_grey/gwt-logo.png");
			}
		});
		panel.add(image2);
		panel.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				image.setUrl("http://acris.googlecode.com/svn/wiki/icons_grey/roadmap.png");
			}
		});
		
		
		image.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				image.setUrl("http://acris.googlecode.com/svn/wiki/icons_grey/contribute.png");
			}
		});
	}
}
