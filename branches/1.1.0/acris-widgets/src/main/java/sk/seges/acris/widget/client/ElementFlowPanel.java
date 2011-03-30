package sk.seges.acris.widget.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHTML;

public class ElementFlowPanel extends FlowPanel implements HasClickHandlers, HasHTML {

	protected Element element;

	public ElementFlowPanel() {
		this(DOM.createDiv());
	}

	public ElementFlowPanel(Element element) {
		this.element = element;
	}

	@Override
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

	@Override
	public String getText() {
		if (isAttached()) {
			return getElement().getParentElement().getInnerText();
		} else {
			Element ele = DOM.createDiv();
			Node clone = element.cloneNode(true);
			ele.appendChild(clone);
			return ele.getInnerText();
		}
	}

	@Override
	public void setText(String text) {
		if (isAttached()) {
			getElement().getParentElement().setInnerText(text);
		} else {
			throw new RuntimeException("ElementFlowPanel is not attached");
		}
	}

	@Override
	public String getHTML() {
		if (isAttached()) {
			return getElement().getParentElement().getInnerHTML();
		} else {
			Element ele = DOM.createDiv();
			Node clone = element.cloneNode(true);
			ele.appendChild(clone);
			return ele.getInnerHTML();
		}
	}

	@Override
	public void setHTML(String html) {
		if (isAttached()) {
			getElement().getParentElement().setInnerHTML(html);
		} else {
			throw new RuntimeException("ElementFlowPanel is not attached");
		}
	}
}
