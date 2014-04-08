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
import sk.seges.acris.recorder.client.tools.CacheMap;

@BeanWrapper
public class KeyboardEvent extends AbstractGenericTargetableEventWithFlags {

	public static final String KEY_CODE_ATTRIBUTE = "keyCode";
	public static final String CHAR_CODE_ATTRIBUTE = "charCode";
	
	protected int keyCode;
	protected int charCode;

	public KeyboardEvent(CacheMap cacheMap) {
        super(cacheMap);
	}
	
	public KeyboardEvent(CacheMap cacheMap, Event event) {
		super(cacheMap, event);
		
		keyCode = DOM.eventGetKeyCode(event);
		charCode = getCharCode(event);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + charCode;
		result = prime * result + keyCode;
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
		if (charCode != other.charCode)
			return false;
		if (keyCode != other.keyCode)
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
	
	private native char getCharCode(NativeEvent e)/*-{
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
		if (pretty) {
			if (!detailed) {
				return type + " with keyCode= "+ keyCode + " and charCode=" + charCode;
			} else {
				String flags = "[CASM]" + (ctrlKey ? "true" : "false") + (altKey ? "true" : "false") +
						(shiftKey ? "true" : "false") + (metaKey ? "true" : "false");
				flags += ", ";
				
				return type + " , " + flags + " with keyCode= "+ keyCode + " and charCode=" + charCode;
			}
		} else {
			if (!detailed) {
				return "KeyboardEvent [type=" + type 
					+ ", keyCode= "+ keyCode + " and charCode=" + charCode + "]";
			} else {
				return "KeyboardEvent [altKey=" + altKey
					+ ", canBubble=" + canBubble + ", cancelable=" + cancelable
					+ ", metaKey=" + metaKey + ", relatedTargetXpath="
					+ ", keyCode= "+ keyCode + ", charCode=" + charCode
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

	public void setCharCode(int charCode) {
		this.charCode = charCode;
	}

	public int getCharCode() {
		return charCode;
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