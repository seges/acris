package sk.seges.acris.security.showcase.client;

import java.util.ArrayList;
import java.util.List;

import sk.seges.acris.security.client.mediator.RuntimeSecurityMediator;
import sk.seges.acris.security.client.mediator.SecurityMediator;
import sk.seges.acris.security.client.session.SessionServiceDefTarget;
import sk.seges.acris.security.rpc.callback.SecuredAsyncCallback;
import sk.seges.acris.security.rpc.exception.SecurityException;
import sk.seges.acris.security.rpc.session.ClientSession;
import sk.seges.acris.security.rpc.user_management.service.IUserService;
import sk.seges.acris.security.rpc.user_management.service.IUserServiceAsync;
import sk.seges.acris.security.showcase.shared.CustomerService;
import sk.seges.acris.security.showcase.shared.CustomerServiceAsync;
import sk.seges.acris.security.showcase.shared.Grants;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author ladislav.gazo
 */
public class Site implements EntryPoint {

	public void onModuleLoad() {
		RootPanel root = RootPanel.get();
		final FlowPanel container = new FlowPanel();
		root.add(container);

		// application-wide client session filled with logged in user
		final ClientSession clientSession = new ClientSession();

		// standard user service
		final IUserServiceAsync userService = GWT.create(IUserService.class);
		SessionServiceDefTarget endpoint = (SessionServiceDefTarget) userService;
		endpoint.setServiceEntryPoint("showcase-service/userService");
		endpoint.setSession(clientSession);

		final CustomerServiceAsync customerService = GWT.create(CustomerService.class);
		endpoint = (SessionServiceDefTarget) customerService;
		endpoint.setServiceEntryPoint("showcase-service/customerService");
		endpoint.setSession(clientSession);

		final List<Widget> checkables = new ArrayList<Widget>();
		
		// simple login panel with a command responsible for showing secured
		// panel
		LoginPanel login = new LoginPanel(userService, clientSession, new Command() {
			@Override
			public void execute() {
				CustomerPanel customerPanel = GWT.create(CustomerPanel.class);
				customerPanel.setClientSession(clientSession);
				container.add(customerPanel);

				MenuItem item = GWT.create(MenuItem.class);
				item.setClientSession(clientSession);
				RuntimeSecurityMediator.setGrant(Grants.SECURITY_MANAGEMENT, item);
				container.add(item);
				
				checkables.add(customerPanel);
				checkables.add(item);
			}
		});

		container.add(login);

		Button customerButton = new Button("Get customer if permitted", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				customerService.getCustomerName(new SecuredAsyncCallback<String>() {

					@Override
					public void onSuccessCallback(String arg0) {
						DialogBox db = new DialogBox(true);
						db.setText(arg0);
						db.center();
					}
					
					@Override
					public void onSecurityException(SecurityException exception) {
						Window.alert("Error while executing service call, " + exception.getMessage());
						GWT.log("Error", exception);
					}
				});
			}
		});

		container.add(customerButton);
		
		Button recheckButton = new Button("Check security again", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				for(Widget checkable : checkables) {
					SecurityMediator.checkSecuredWidget(checkable);
				}
			}
		});

		container.add(recheckButton);
	}
}
