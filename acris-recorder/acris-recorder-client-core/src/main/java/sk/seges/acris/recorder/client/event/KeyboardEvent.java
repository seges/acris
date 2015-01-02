package sk.seges.acris.recorder.client.event;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import sk.seges.acris.core.client.annotation.BeanWrapper;
import sk.seges.acris.recorder.client.event.generic.AbstractGenericTargetableEventWithFlags;
import sk.seges.acris.recorder.client.tools.ElementXpathCache;

@BeanWrapper
public class KeyboardEvent extends AbstractGenericTargetableEventWithFlags {

	public static final String KEY_CODE_ATTRIBUTE = "keyCode";
    public static final String SCROLL_OFFSET = "scrollOffset";
    public static final String SCROLL_TYPE = "scrollTypeInt";

    public enum ScrollType {
        X, Y
    }

    protected int keyCode;
    protected ScrollType scrollType = ScrollType.Y;
    protected int scrollOffset = 0;

	public KeyboardEvent(ElementXpathCache elementXpathCache) {
        super(elementXpathCache);
	}
	
	public KeyboardEvent(ElementXpathCache elementXpathCache, Event event) {
		super(elementXpathCache, event);

        keyCode = getKeyCode(event);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
        result = prime * result + keyCode;
        result = prime * result + (scrollType != null ? scrollType.hashCode() : 0);
        result = prime * result + scrollOffset;
        return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof KeyboardEvent))
			return false;
		KeyboardEvent other = (KeyboardEvent) obj;

        if (keyCode != other.keyCode)
            return false;
        if (scrollOffset != other.scrollOffset)
            return false;
        if (scrollType != other.scrollType)
            return false;

		return true;
    }

    public static boolean isCorrectEvent(Event event) {
		int type = DOM.eventGetType(event);
		return isCorrectEvent(type);
	}

	private static boolean isCorrectEvent(int type) {
		switch (type) {
			case Event.ONKEYDOWN:
			case Event.ONKEYPRESS:
			case Event.ONKEYUP:
				return true;
		}
		
		return false;
	}
	
	private native char getKeyCode(NativeEvent e)/*-{
        e = e || $wnd.event;
        return e.which || e.keyCode;
	}-*/;

	protected NativeEvent createEvent(Element el) {
		return Document.get().createKeyCodeEvent(type,
				ctrlKey, altKey, shiftKey, metaKey, keyCode);
	}

	public int getTypeInt() {
		if (KeyDownEvent.getType().getName().equals(type)) {
			return 0;
		} else if (KeyPressEvent.getType().getName().equals(type)) {
			return 1;
		}

		return 2;
	}

	public String toString(boolean pretty, boolean detailed) {
		if (!pretty) {
			if (!detailed) {
				return type + " with keyCode= "+ keyCode;
			} else {
				String flags = "[CASM]" + (ctrlKey ? "true" : "false") + (altKey ? "true" : "false") +
						(shiftKey ? "true" : "false") + (metaKey ? "true" : "false");
				flags += ", ";
				
				return type + " , " + flags + " with keyCode= "+ keyCode;
			}
		} else {
			if (!detailed) {
				return "KeyboardEvent [type=" + type 
					+ ", keyCode= "+ keyCode + ", relatedTargetXpath=" + getRelatedTargetXpath() + "]";
			} else {
				return "KeyboardEvent [altKey=" + altKey
					+ ", canBubble=" + canBubble + ", cancelable=" + cancelable
					+ ", metaKey=" + metaKey + ", relatedTargetXpath=" + getRelatedTargetXpath()
					+ ", keyCode= "+ keyCode + "(" + (char) getKeyCode() + ")"
					+ ", shiftKey=" + shiftKey + ", type=" + type + "]";
			}
		}
	}

	public int getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(int keyCode) {
		this.keyCode = keyCode;
	}

    public ScrollType getScrollType() {
        return scrollType;
    }

    public void setScrollType(ScrollType scrollType) {
        this.scrollType = scrollType;
    }

    public int getScrollTypeInt() {
        return getScrollType().ordinal();
    }

    public void setScrollTypeInt(int scrollTypeInt) {
        for (ScrollType scrollType: ScrollType.values()) {
            if (scrollType.ordinal() == scrollTypeInt) {
                setScrollType(scrollType);
                return;
            }
        }

        throw new RuntimeException("Unsupported value for scroll type: " + scrollTypeInt);
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public void setScrollOffset(int scrollOffset) {
        this.scrollOffset = scrollOffset;
    }

    public void setTypeInt(int type) {
		switch (type) {
		case 0:
			this.type = KeyDownEvent.getType().getName();
			return;
		case 1:
			this.type = KeyPressEvent.getType().getName();
			return;
		case 2:
			this.type = KeyUpEvent.getType().getName();
			return;
		}
		
		throw new IllegalArgumentException("Unknown event type: " + type);
	}
}