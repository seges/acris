package sk.seges.acris.generator.client.collector.panel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class AnchorPanel extends Widget  {

	interface AnchorPanelUiBinder extends UiBinder<DivElement, AnchorPanel> {}

	private static final AnchorPanelUiBinder uiBinder = GWT.create(AnchorPanelUiBinder.class);

	public AnchorPanel() {
		setElement(uiBinder.createAndBindUi(this));
	}
}