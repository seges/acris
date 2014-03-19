package sk.seges.acris.recorder.client.model;

import sk.seges.acris.recorder.client.event.HtmlEvent;

public class HtmlTestEvent extends HtmlEvent {

	public HtmlTestEvent(String type) {
		this.type = type;
		this.relatedTargetId = "#testId/table/div/input";
	}
}
