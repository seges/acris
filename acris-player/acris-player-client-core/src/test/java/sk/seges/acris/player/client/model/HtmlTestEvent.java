package sk.seges.acris.player.client.model;

import sk.seges.acris.recorder.client.event.HtmlEvent;

public class HtmlTestEvent extends HtmlEvent {

	public HtmlTestEvent(String type) {
		this.type = type;
		this.relatedTargetXpath = "#testId/table/div/input";
	}
}
