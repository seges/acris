package sk.seges.acris.recorder.client.event;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import sk.seges.acris.core.client.annotation.BeanWrapper;
import sk.seges.acris.recorder.client.common.Position;
import sk.seges.acris.recorder.client.event.fields.EMouseEventFields;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericTargetableEventWithFlags;
import sk.seges.acris.recorder.client.tools.ElementXpathCache;

@BeanWrapper
public class MouseEvent extends AbstractGenericTargetableEventWithFlags implements HasAbsolutePosition {

	protected int clientX;
	protected int clientY;
	protected int button;
	
	protected boolean relative = false;
	
	public static final String RELATIVE_INT_ATTRIBUTE = "relativeInt";

	public static final String CLIENT_X_HI_ATTRIBUTE = "clientX_Hi";
	public static final String CLIENT_X_LO_ATTRIBUTE = "clientX_Lo";

	public static final String CLIENT_Y_HI_ATTRIBUTE = "clientY_Hi";
	public static final String CLIENT_Y_LO_ATTRIBUTE = "clientY_Lo";

	public static final String BUTTON_ATTRIBUTE = "button";

	public MouseEvent(ElementXpathCache elementXpathCache) {
        super(elementXpathCache);
	}

	public static boolean isCorrectEvent(Event event) {
		int type = DOM.eventGetType(event);
		return isCorrectEvent(type);
	}
	
	public MouseEvent(ElementXpathCache elementXpathCache, Event event) {
		super(elementXpathCache, event);

		button = DOM.eventGetButton(event);

		if (getTypeInt() == MOUSE_WHEEL_TYPE) {
			this.clientX = Document.get().getScrollLeft();
			this.clientY = Document.get().getScrollTop();
		} else {
			this.clientX = DOM.eventGetClientX(event);
			this.clientY = DOM.eventGetClientY(event);
		}

        initPositions();
	}

	public int getClientX() {
		return clientX;
	}

	public void setClientX(int clientX) {
		this.clientX = clientX;
	}

    private int getNumber(int loValue, int hiValue, int length) {
        int multiplier = 1;
        if ((hiValue | (1 << (length - 1))) == hiValue) {
            hiValue &= ~(1 << (length - 1));
            multiplier = -1;
        }

        return (loValue | (hiValue << length)) * multiplier;
    }

    private int getNumber(int loValue, int hiValue) {
        return loValue | hiValue;
    }

    private int getUnsignedNumberPart(int value, long andVal) {
        return (int) (Math.abs(value) & andVal);
    }

    private int getSignedNumberPart(int value, int length) {
        int result = Math.abs(value) >> length;

        if (value < 0) {
            result |= 1 << (length - 1);
        }

        return result;
    }

    public int getClientX_Lo() {
        return getUnsignedNumberPart(getClientX(), X_LO_VAL);
	}

	public int getClientX_Hi() {
        return getSignedNumberPart(getClientX(), EMouseEventFields.EVENT_CLIENT_X_LO.getFieldDefinition().getLength());
    }

	public void setClientX_Hi(int clientX_Hi) {
        this.clientX = getNumber(getClientX_Lo(), clientX_Hi, EMouseEventFields.EVENT_CLIENT_X_LO.getFieldDefinition().getLength());
	}

	public void setClientX_Lo(int clientX_Lo) {
		this.clientX = getNumber(clientX_Lo, getClientX_Hi());
	}

	public int getClientY() {
		return clientY;
	}

	public void setClientY(int clientY) {
		this.clientY = clientY;
	}

    private static final Long Y_LO_VAL = Long.valueOf(new String(new char[EMouseEventFields.EVENT_CLIENT_Y_LO.getFieldDefinition().getLength()]).replace("\0", "1"), 2);
    private static final Long X_LO_VAL = Long.valueOf(new String(new char[EMouseEventFields.EVENT_CLIENT_X_LO.getFieldDefinition().getLength()]).replace("\0", "1"), 2);

    public int getClientY_Lo() {
        return getUnsignedNumberPart(getClientY(), Y_LO_VAL);
    }

    public int getClientY_Hi() {
        return getSignedNumberPart(getClientY(), EMouseEventFields.EVENT_CLIENT_Y_LO.getFieldDefinition().getLength());
    }

    public void setClientY_Hi(int clientY_Hi) {
        this.clientY = getNumber(getClientY_Lo(), clientY_Hi, EMouseEventFields.EVENT_CLIENT_Y_LO.getFieldDefinition().getLength());
    }

    public void setClientY_Lo(int clientY_Lo) {
        this.clientY = getNumber(clientY_Lo, getClientY_Hi());
    }

	protected void initPositions() {

		if (this.relatedTargetXpath != null && this.relatedTargetXpath.length() > 0) {

            Position position = elementXpathCache.getElementPosition(getElement(), true);

			this.relative = true;
			this.clientX = this.clientX - position.left;
			this.clientY = this.clientY - position.top;
		}
	}

    public int getRelativeInt() {
		return this.relative ? 1 : 0;
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
		result = prime * result + clientX;
		result = prime * result + clientY;
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
		if (clientX != other.clientX)
			return false;
        return clientY == other.clientY && relative == other.relative;
    }

	public int getButton()  {
		return button;
	}

	public void setButton(int button) {
		this.button = button;
	}

	private void recalculatePosition() {
		if (!relative) {
			return;
		}
		
		if (this.relatedTargetXpath != null && this.relatedTargetXpath.length() > 0) {
			relative = false;

            Position elementPosition = elementXpathCache.getElementPosition(getElement(), true);

            this.clientX = this.clientX + elementPosition.left;
			this.clientY = this.clientY + elementPosition.top;
		}
	}

    @Override
	public int getAbsoluteClientX() {
		recalculatePosition();
		return clientX;
	}

    @Override
	public int getAbsoluteClientY() {
		recalculatePosition();
		return clientY;
	}

	public NativeEvent createEvent(Element el) {
		//TODO screenX, screenY
		return Document.get().createMouseEvent(type, canBubble, cancelable, 0,
				0, 0, clientX, clientY, ctrlKey, altKey,
				shiftKey, metaKey, button, el);
	}
	
	public String toString(boolean pretty, boolean detailed) {
		
		if (!pretty) {
			if (!detailed) {
				if (relatedTargetXpath != null && relatedTargetXpath.length() > 0) {
					return type + " on " + relatedTargetXpath + " element";
				} else {
					return type + " to " + getClientX() + ", " + getClientY();
				}
			} else {
				String flags = "[CASM]" + (ctrlKey ? "true" : "false") + (altKey ? "true" : "false") +
						(shiftKey ? "true" : "false") + (metaKey ? "true" : "false");
				flags += ", button " + button + " pressed";
				
				if (relatedTargetXpath != null && relatedTargetXpath.length() > 0) {
					return type + " on " + relatedTargetXpath + " element" + ", " + flags;
				} else {
					return type + " to " + getClientX() + ", " + getClientY() + ", " + flags;
				}
			}
		} else {
			if (!detailed) {
				return "MouseEvent [x=" + getClientX()
				+ ", y=" + getClientY()
				+ ", xpath="
				+ relatedTargetXpath + ", type=" + type + "]";
			} else {
				return "MouseEvent [alt=" + altKey + ", button=" + button
					+ ", canBubble=" + canBubble + ", cancelable=" + cancelable
					+ ", ctrlKey=" + ctrlKey
					+ ", x=" + getClientX()
					+ ", y=" + getClientY()
					+ ", metaKey=" + metaKey + ", xpath="
					+ relatedTargetXpath + ", shiftKey=" + shiftKey + ", type=" + type + "]";
			}
		}
	}

	public int getTypeInt() {
		if (ClickEvent.getType().getName().equals(type)) {
			return MOUSE_CLICK_TYPE;
		} else if (DoubleClickEvent.getType().getName().equals(type)) {
			return MOUSE_DOUBLE_CLICK_TYPE;
		} else if (MouseDownEvent.getType().getName().equals(type)) {
			return MOUSE_DOWN_TYPE;
		} else if (MouseMoveEvent.getType().getName().equals(type)) {
			return MOUSE_MOVE_TYPE;
		} else if (MouseOutEvent.getType().getName().equals(type)) {
			return MOUSE_OUT_TYPE;
		} else if (MouseOverEvent.getType().getName().equals(type)) {
			return MOUSE_OVER_TYPE;
		} else if (MouseUpEvent.getType().getName().equals(type)) {
			return MOUSE_UP_TYPE;
		}

		return MOUSE_WHEEL_TYPE;
	}

	public static final int MOUSE_CLICK_TYPE = 0;
	public static final int MOUSE_DOUBLE_CLICK_TYPE = 1;
	public static final int MOUSE_DOWN_TYPE = 2;
	public static final int MOUSE_MOVE_TYPE = 3;
	public static final int MOUSE_OUT_TYPE = 4;
	public static final int MOUSE_OVER_TYPE = 5;
	public static final int MOUSE_UP_TYPE = 6;
	public static final int MOUSE_WHEEL_TYPE = 7;

	@Override
	public void setTypeInt(int type) {
		switch (type) {
		case MOUSE_CLICK_TYPE:
			this.type = ClickEvent.getType().getName();
			return;
		case MOUSE_DOUBLE_CLICK_TYPE:
			this.type = DoubleClickEvent.getType().getName();
			return;
		case MOUSE_DOWN_TYPE:
			this.type = MouseDownEvent.getType().getName();
			return;
		case MOUSE_MOVE_TYPE:
			this.type = MouseMoveEvent.getType().getName();
			return;
		case MOUSE_OUT_TYPE:
			this.type = MouseOutEvent.getType().getName();
			return;
		case MOUSE_OVER_TYPE:
			this.type = MouseOverEvent.getType().getName();
			return;
		case MOUSE_UP_TYPE:
			this.type = MouseUpEvent.getType().getName();
			return;
		case MOUSE_WHEEL_TYPE:
			this.type = MouseWheelEvent.getType().getName();
			return;
		}
		
		throw new IllegalArgumentException("Unknown event type: " + type);
	}
}