package sk.seges.acris.recorder.client.model;

import sk.seges.acris.recorder.client.event.HtmlEvent;
import sk.seges.acris.recorder.client.tools.ElementXpathCache;

public class HtmlTestEvent extends HtmlEvent {

	public HtmlTestEvent(String type) {
        super(new ElementXpathCache(40));
		this.type = type;
		this.relatedTargetXpath = "#testId/table/div/input";
	}
}
