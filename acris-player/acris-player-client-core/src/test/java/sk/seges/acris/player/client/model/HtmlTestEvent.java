package sk.seges.acris.player.client.model;

import sk.seges.acris.recorder.client.event.HtmlEvent;
import sk.seges.acris.recorder.client.tools.ElementXpathCache;

public class HtmlTestEvent extends HtmlEvent {

	public HtmlTestEvent(String type) {
        super(new ElementXpathCache(30));
        this.type = type;
	}
}
