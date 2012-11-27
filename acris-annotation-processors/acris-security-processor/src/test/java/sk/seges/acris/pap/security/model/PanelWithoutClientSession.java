package sk.seges.acris.pap.security.model;

import sk.seges.acris.security.client.annotations.Secured;

import com.google.gwt.user.client.ui.Label;

@Secured
public class PanelWithoutClientSession extends AbstractPanel {

	@Secured("TEXT")
	Label label1;

}