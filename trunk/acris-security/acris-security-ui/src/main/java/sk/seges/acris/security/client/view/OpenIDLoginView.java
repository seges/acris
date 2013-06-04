package sk.seges.acris.security.client.view;

import sk.seges.acris.security.client.presenter.OpenIDLoginPresenter.OpenIDLoginDisplay;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;

public class OpenIDLoginView extends LoginView implements OpenIDLoginDisplay {

	private FlowPanel openIDButtonPanel = new FlowPanel();

	public OpenIDLoginView() {
		super();
	}

	@Override
	public HandlerRegistration addOpenIDButtonHandler(ClickHandler handler, String style) {
		Button button = new Button(" ");
		button.addStyleName("acris-login-button");
		button.addStyleName(style);

		openIDButtonPanel.add(button);

		return button.addClickHandler(handler);
	}

	@Override
	protected void initComponents() {
		super.initComponents();

		openIDButtonPanel.addStyleName("acris-login-button-wrapper");

		getDock().add(openIDButtonPanel, DockPanel.EAST);
	}
}
