package sk.seges.acris.security.client.view;



import sk.seges.acris.security.client.presenter.LogoutDisplay;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LogoutView extends Composite implements LogoutDisplay {

	
	private Boolean initialized = false;
	private final DockPanel dock = new DockPanel();
	private VerticalPanel logoutPanel = new VerticalPanel();
	private Label loginUserLabel;
	private Button logoutButton;
	
	public LogoutView() {
		initWidget(dock);
	}
	
	@Override
	protected void onLoad() {
		if (!initialized) {
			initComponents();
			initialized = true;
		}
	}
	
	protected void initComponents() {
		loginUserLabel = new Label();
		SimplePanel loginUserPanel = new SimplePanel();
		loginUserPanel.add(loginUserPanel);
		
		logoutPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
		logoutPanel.add(loginUserPanel);
		
		SimplePanel simplePanel = new SimplePanel();
		simplePanel.addStyleDependentName("logout-Button-wrapper");
		simplePanel.setWidget(ensureLogoutButton());
		logoutPanel.add(simplePanel);
		
		dock.add(logoutPanel, DockPanel.CENTER);
	}
	
	private Button ensureLogoutButton() {
		if (this.logoutButton == null) {
			this.logoutButton = GWT.create(Button.class);
		}
		return this.logoutButton;
	}

	@Override
	public void displayMessage(String message) {
		RootPanel.get().clear();
		RootPanel.get().add(new Label(message));
	}

	@Override
	public void showMessage(String message) {
		Window.alert(message);
	}

	@Override
	public HandlerRegistration addLogoutButtonHandler(ClickHandler handler) {
		return ensureLogoutButton().addClickHandler(handler);
	}

	@Override
	public void setLoggedUser(String userName) {
		loginUserLabel.setText(userName);
	}
}
