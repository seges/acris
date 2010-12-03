package sk.seges.acris.widget.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;

public class ElementFlowPanel extends FlowPanel implements HasClickHandlers {

	protected Element element;

	public ElementFlowPanel() {
		this(DOM.createDiv());
	}

	public ElementFlowPanel(Element element) {
		this.element = element;
	}

	public com.google.gwt.user.client.Element getElement() {
		return (com.google.gwt.user.client.Element) this.element;
	}
	
	public void setThisElement(Element element) {
		this.element = element;
	}

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }
}
