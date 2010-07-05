package sk.seges.acris.security.showcase.client;

import sk.seges.acris.security.client.session.SessionServiceDefTarget;
import sk.seges.acris.security.rpc.session.ClientSession;
import sk.seges.acris.security.rpc.user_management.service.IUserService;
import sk.seges.acris.security.rpc.user_management.service.IUserServiceAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;

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
    	
		// simple login panel with a command responsible for showing secured panel
		LoginPanel login = new LoginPanel(userService, clientSession, new Command() {
			@Override
			public void execute() {
		    	CustomerPanel customerPanel = GWT.create(CustomerPanel.class);
		    	customerPanel.setClientSession(clientSession);
		    	container.add(customerPanel);		
			}
		});
		
		container.add(login);
    }
}
