package sk.seges.acris.widget.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHTML;

public class ElementFlowPanel extends FlowPanel implements HasClickHandlers, HasDoubleClickHandlers, HasAllMouseHandlers, HasHTML {

	protected Element element;

	public ElementFlowPanel() {
		this(DOM.createDiv());
	}

	public ElementFlowPanel(Element element) {
		this.element = element;
	}

	public static ElementFlowPanel wrap(Element element) {
		ElementFlowPanel panel = new ElementFlowPanel(element);
		panel.onAttach();
		return panel;
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
	  public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
		    return addDomHandler(handler, DoubleClickEvent.getType());
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

	@Override
	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
		return addDomHandler(handler, MouseDownEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
		return addDomHandler(handler, MouseUpEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return addDomHandler(handler, MouseOutEvent.getType());	}

	@Override
	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return addDomHandler(handler, MouseOverEvent.getType());	}

	@Override
	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
		return addDomHandler(handler, MouseMoveEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
		return addDomHandler(handler, MouseWheelEvent.getType());
	}
}
