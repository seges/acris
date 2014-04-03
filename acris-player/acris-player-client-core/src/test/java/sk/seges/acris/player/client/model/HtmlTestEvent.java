package sk.seges.acris.player.client.model;

import sk.seges.acris.recorder.client.event.HtmlEvent;
import sk.seges.acris.recorder.client.tools.CacheMap;

public class HtmlTestEvent extends HtmlEvent {

	public HtmlTestEvent(String type) {
        super(new CacheMap(30));
        this.type = type;
	}
}
