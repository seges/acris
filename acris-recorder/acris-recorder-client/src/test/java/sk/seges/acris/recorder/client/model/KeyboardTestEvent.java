package sk.seges.acris.recorder.client.model;

import sk.seges.acris.recorder.client.event.KeyboardEvent;
import sk.seges.acris.recorder.client.tools.CacheMap;

public class KeyboardTestEvent extends KeyboardEvent {

	public KeyboardTestEvent(String type) {
		this(type, 45, 'A');
	}

	public KeyboardTestEvent(String type, int keyCode, char charCode) {
        super(new CacheMap(40));
		this.keyCode = keyCode;
		this.charCode = charCode;
		this.ctrlKey = false;
		this.altKey = false;
		this.shiftKey = false;
		this.metaKey = false;
		this.relatedTargetXpath = "#testId/table/div/input";
		this.type = type;
	}
}