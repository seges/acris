/**
 * 
 */
package sk.seges.acris.security.showcase.client;

import sk.seges.acris.security.rpc.session.ClientSession;
import sk.seges.acris.security.rpc.user_management.domain.UserPasswordLoginToken;
import sk.seges.acris.security.rpc.user_management.service.IUserServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author ladislav.gazo
 */
public class LoginPanel extends Composite {
	private TextBox username;
	private TextBox password;
	private Button submit;

	public LoginPanel(final IUserServiceAsync userService, final ClientSession clientSession,
			final Command loggedInCmd) {
		username = new TextBox();
		password = new PasswordTextBox();
		submit = new Button("Log in");

		VerticalPanel container = new VerticalPanel();
		container.add(username);
		container.add(password);
		container.add(submit);

		initWidget(container);

		submit.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				userService.login(new UserPasswordLoginToken(username.getText(), password.getText(), null),
						new AsyncCallback<ClientSession>() {
							@Override
							public void onSuccess(ClientSession result) {
								// copy the information about user and session
								// so secured components can rely on it
								clientSession.setSessionId(result.getSessionId());
								clientSession.setUser(result.getUser());
								loggedInCmd.execute();
							}

							@Override
							public void onFailure(Throwable caught) {
								GWT.log("Login failure", caught);
							}
						});
			}
		});
	}

}
