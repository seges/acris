package sk.seges.acris.player.client.model;

import sk.seges.acris.recorder.client.event.KeyboardEvent;
import sk.seges.acris.recorder.client.tools.ElementXpathCache;

public class KeyboardTestEvent extends KeyboardEvent {

	public KeyboardTestEvent(String type) {
		this(type, 45);
	}

	public KeyboardTestEvent(String type, int keyCode) {
        super(new ElementXpathCache(30));
		this.keyCode = keyCode;
		this.ctrlKey = false;
		this.altKey = false;
		this.shiftKey = false;
		this.metaKey = false;
		this.type = type;
	}
}