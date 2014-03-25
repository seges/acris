package sk.seges.acris.recorder.client.event;

import sk.seges.acris.core.client.annotation.BeanWrapper;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericTargetableEventWithFlags;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

@BeanWrapper
public class MouseEvent extends AbstractGenericTargetableEventWithFlags {

	protected int detail = 0;
	protected int screenX;
	protected int screenY;
	protected int clientX;
	protected int clientY;
	protected int button;
	
	protected boolean relative = false;;
	
	public static final String RELATIVE_INT_ATTRIBUTE = "relativeInt";
	public static final String CLIENT_X_ATTRIBUTE = "clientX";
	public static final String CLIENT_Y_ATTRIBUTE = "clientY";
	public static final String SCREEN_X_ATTRIBUTE = "screenX";
	public static final String SCREEN_Y_ATTRIBUTE = "screenY";
	
	public MouseEvent() {
	}

	public static boolean isCorrectEvent(Event event) {
		int type = DOM.eventGetType(event);
		return isCorrectEvent(type);
	}
	
	public MouseEvent(Event event) {
		super(event);

		button = DOM.eventGetButton(event);

		this.clientX = DOM.eventGetClientX(event);
		this.clientY = DOM.eventGetClientY(event);
		this.screenX = DOM.eventGetScreenX(event);
		this.screenY = DOM.eventGetScreenY(event);

		// TODO
		// If mouse whell event is occured is also scroll event occured ?
		// DOM.eventGetMouseWheelVelocityY(event);

	}

	public int getClientX() {
		return clientX;
	}

	public void setClientX(int clientX) {
		this.clientX = clientX;
	}

	public int getClientY() {
		return clientY;
	}

	public void setClientY(int clientY) {
		this.clientY = clientY;
	}

	public void setScreenX(int screenX) {
		this.screenX = screenX;
	}

	public void setScreenY(int screenY) {
		this.screenY = screenY;
	}

	protected void initTarget(Element target, Event event) {
		super.initTarget(target, event);

		if (this.relatedTargetXpath != null && this.relatedTargetXpath.length() > 0) {
			int left = DOM.getAbsoluteLeft(target);
			int top = DOM.getAbsoluteTop(target);
	
			relative = true;
			this.clientX = this.clientX - left;
			this.clientY = this.clientY - top;
			this.screenX = this.screenX - left;
			this.screenY = this.screenY - top;
		}
	}

	public int getRelativeInt() {
		return relative ? 1 : 0;
	}

	public void setRelativeInt(int relative) {
		this.relative = relative == 1;
	}

	private static boolean isCorrectEvent(int type) {
		switch (type) {
		case Event.ONCLICK:
		case Event.ONDBLCLICK:
		case Event.ONMOUSEDOWN:
		case Event.ONMOUSEMOVE:
		case Event.ONMOUSEOUT:
		case Event.ONMOUSEOVER:
		case Event.ONMOUSEUP:
		case Event.ONMOUSEWHEEL:
			return true;
		}

		// Where handle this? It is Microsoft specific event when the
		// releaseCapture method is invoked.
		// Hopefully we don't have to handle it
		// case Event.ONLOSECAPTURE: return "losecapture";

		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + button;
		result = prime * result + detail;
		result = prime * result + clientX;
		result = prime * result + clientY;
		result = prime * result + screenX;
		result = prime * result + screenY;
		result = prime * result + (relative ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof MouseEvent))
			return false;
		MouseEvent other = (MouseEvent) obj;
		if (button != other.button)
			return false;
		if (detail != other.detail)
			return false;
		if (clientX != other.clientX)
			return false;
		if (clientY != other.clientY)
			return false;
		if (screenX != other.screenX)
			return false;
		if (screenY != other.screenY)
			return false;
		if (relative != other.relative)
			return false;
		return true;
	}
	
	public int getScreenX() {
		return screenX;
	}

	public int getScreenY() {
		return screenY;
	}

	private void recalculatePosition() {
		if (!relative) {
			return;
		}
		
		if (this.relatedTargetXpath != null && this.relatedTargetXpath.length() > 0) {
			relative = false;

			prepareEvent();
			
			int left = DOM.getAbsoluteLeft(el);
			int top = DOM.getAbsoluteTop(el);
			
			this.clientX = this.clientX + left;
			this.clientY = this.clientY + top;
			this.screenX = this.screenX + left;
			this.screenY = this.screenY + top;
		}
	}
	
	public int getAbsoluteClientX() {
		recalculatePosition();
		return clientX;
	}

	public int getAbsoluteClientY() {
		recalculatePosition();
		return clientY;
	}

	protected NativeEvent createEvent(Element el) {
		return Document.get().createMouseEvent(type, canBubble, cancelable, detail, 
				screenX, screenY, clientX, clientY, ctrlKey, altKey, 
				shiftKey, metaKey, button, el);
	}
	
	public String toString(boolean pretty, boolean detailed) {
		
		if (pretty) {
			if (!detailed) {
				if (relatedTargetXpath != null && relatedTargetXpath.length() > 0) {
					return type + " on " + relatedTargetXpath + " element";
				} else {
					return type + " to " + getAbsoluteClientX() + ", " + getAbsoluteClientY();
				}
			} else {
				String flags = "[CASM]" + (ctrlKey ? "true" : "false") + (altKey ? "true" : "false") +
						(shiftKey ? "true" : "false") + (metaKey ? "true" : "false");
				flags += ", button " + button + " pressed";
				
				if (relatedTargetXpath != null && relatedTargetXpath.length() > 0) {
					return type + " on " + relatedTargetXpath + " element" + ", " + flags;
				} else {
					return type + " to " + getAbsoluteClientX() + ", " + getAbsoluteClientY() + ", " + flags;
				}
			}
		} else {
			if (!detailed) {
				return "MouseEvent [elementRelativeClientX=" + clientX
				+ ", elementRelativeClientY=" + clientY
				+ ", elementRelativeScreenX=" + screenX
				+ ", elementRelativeScreenY=" + screenY
				+ ", relatedTargetXpath="
				+ relatedTargetXpath + ", type=" + type + "]";
			} else {
				return "MouseEvent [altKey=" + altKey + ", button=" + button
					+ ", canBubble=" + canBubble + ", cancelable=" + cancelable
					+ ", ctrlKey=" + ctrlKey + ", detail=" + detail
					+ ", elementRelativeClientX=" + clientX
					+ ", elementRelativeClientY=" + clientY
					+ ", elementRelativeScreenX=" + screenX
					+ ", elementRelativeScreenY=" + screenY
					+ ", metaKey=" + metaKey + ", relatedTargetXpath="
					+ relatedTargetXpath + ", shiftKey=" + shiftKey + ", type=" + type + "]";
			}
		}
	}

	public int getTypeInt() {
		if (ClickEvent.getType().getName().equals(type)) {
			return 0;
		} else if (DoubleClickEvent.getType().getName().equals(type)) {
			return 1;
		} else if (MouseDownEvent.getType().getName().equals(type)) {
			return 2;
		} else if (MouseMoveEvent.getType().getName().equals(type)) {
			return 3;
		} else if (MouseOutEvent.getType().getName().equals(type)) {
			return 4;
		} else if (MouseOverEvent.getType().getName().equals(type)) {
			return 5;
		} else if (MouseUpEvent.getType().getName().equals(type)) {
			return 6;
		}
		
		return 7;
	}

	@Override
	public void setTypeInt(int type) {
		switch (type) {
		case 0:
			this.type = ClickEvent.getType().getName();
			return;
		case 1:
			this.type = DoubleClickEvent.getType().getName();
			return;
		case 2:
			this.type = MouseDownEvent.getType().getName();
			return;
		case 3:
			this.type = MouseMoveEvent.getType().getName();
			return;
		case 4:
			this.type = MouseOutEvent.getType().getName();
			return;
		case 5:
			this.type = MouseOverEvent.getType().getName();
			return;
		case 6:
			this.type = MouseUpEvent.getType().getName();
			return;
		case 7:
			this.type = MouseWheelEvent.getType().getName();
			return;
		}
		
		throw new IllegalArgumentException("Unknown event type: " + type);
	}
}