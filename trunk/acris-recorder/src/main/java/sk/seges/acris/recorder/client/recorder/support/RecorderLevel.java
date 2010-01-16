package sk.seges.acris.recorder.client.recorder.support;

import com.google.gwt.user.client.Event;

public enum RecorderLevel {
	MOUSE(Event.ONCLICK | Event.ONDBLCLICK | Event.ONMOUSEDOWN
			| Event.ONMOUSEMOVE | Event.ONMOUSEOUT | Event.ONMOUSEOVER
			| Event.ONMOUSEUP | Event.ONMOUSEWHEEL), 
	KEYBOARD(Event.ONKEYDOWN | Event.ONKEYPRESS | Event.ONKEYUP), 
	HTML(Event.ONBLUR | Event.ONCHANGE | Event.ONCONTEXTMENU 
			| Event.ONERROR | Event.ONFOCUS | Event.ONLOAD | Event.ONSCROLL),

	ALL(MOUSE.getValue() | KEYBOARD.getValue() | HTML.getValue()),
	
	TRACE(Event.ONCLICK | Event.ONDBLCLICK | Event.ONMOUSEDOWN
			| Event.ONMOUSEOUT | Event.ONMOUSEOVER
			| Event.ONMOUSEUP | Event.ONMOUSEWHEEL | KEYBOARD.getValue() | HTML.getValue()),
	
	DETAILED(Event.ONCLICK | Event.ONDBLCLICK | Event.ONMOUSEOUT | Event.ONMOUSEOVER
			| KEYBOARD.getValue() | Event.ONBLUR | Event.ONCONTEXTMENU 
			| Event.ONFOCUS | Event.ONSCROLL),
	
	INFO(Event.ONCLICK | Event.ONDBLCLICK | Event.ONMOUSEOUT | Event.ONMOUSEOVER
			| KEYBOARD.getValue() | Event.ONSCROLL),

	NONE(0);
	
	private int value;

	private RecorderLevel(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
	public boolean isRecordable(int eventType) {
		return ((value | eventType) == value);
	}
}