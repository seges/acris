package sk.seges.acris.player.client.model;

import sk.seges.acris.recorder.client.event.MouseEvent;
import sk.seges.acris.recorder.client.tools.ElementXpathCache;

public class MouseTestEvent extends MouseEvent {

	public MouseTestEvent(String type) {
		this(type, 100, 300);
	}

	public MouseTestEvent(String type, int clientX, int clientY) {
        super(new ElementXpathCache(30));
		this.clientX = clientX;
		this.clientY = clientY;
		this.button = 1;
		this.ctrlKey = false;
		this.altKey = false;
		this.shiftKey = false;
		this.metaKey = false;
		this.type = type;
	}
}