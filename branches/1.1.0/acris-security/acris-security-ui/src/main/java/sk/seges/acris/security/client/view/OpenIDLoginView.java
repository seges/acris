package sk.seges.acris.security.client.view;

import sk.seges.acris.security.client.event.OpenIDLoginEvent;
import sk.seges.acris.security.client.handler.OpenIDLoginHandler;
import sk.seges.acris.security.client.presenter.OpenIDLoginPresenter.OpenIDLoginDisplay;
import sk.seges.acris.security.shared.util.LoginConstants;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;

public class OpenIDLoginView extends LoginView implements OpenIDLoginDisplay {

	public OpenIDLoginView() {
		super();
	}

	@Override
	protected void initComponents() {
		super.initComponents();

		FlowPanel wrapper = new FlowPanel();
		wrapper.addStyleName("acris-login-button-wrapper");

		wrapper.add(createButton("acris-login-google-button", LoginConstants.GOOGLE_IDENTIFIER));
		wrapper.add(createButton("acris-login-yahoo-button", LoginConstants.YAHOO_IDENTIFIER));
		wrapper.add(createButton("acris-login-aol-button", LoginConstants.AOL_IDENTIFIER));
		wrapper.add(createButton("acris-login-blogger-button", LoginConstants.BLOGGER_IDENTIFIER));
		wrapper.add(createButton("acris-login-seznam-button", LoginConstants.SEZNAM_IDENTIFIER));
		wrapper.add(createButton("acris-login-myopenid-button", LoginConstants.MYOPENID_IDENTIFIER));

		getDock().add(wrapper, DockPanel.EAST);
	}

	private Button createButton(final String style, final String identifier) {
		Button button = new Button(" ");
		button.addStyleName("acris-login-button");
		button.addStyleName(style);
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				OpenIDLoginView.this.fireEvent(new OpenIDLoginEvent(identifier));
			}
		});
		return button;
	}

	@Override
	public void addOpenIDLoginHandler(OpenIDLoginHandler handler) {
		addHandler(handler, OpenIDLoginEvent.getType());
	}
}
