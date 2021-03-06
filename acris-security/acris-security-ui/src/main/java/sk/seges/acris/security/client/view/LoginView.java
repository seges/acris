package sk.seges.acris.security.client.view;

import sk.seges.acris.common.util.Pair;
import sk.seges.acris.security.client.i18n.LoginMessages;
import sk.seges.acris.security.client.presenter.LoginPresenter.LoginDisplay;
import sk.seges.acris.security.server.util.LoginConstants;
import sk.seges.acris.security.shared.user_management.model.dto.GenericUserDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LoginView extends Composite implements LoginDisplay {

	private LoginMessages loginMessages = (LoginMessages) GWT.create(LoginMessages.class);

	private final DockPanel dock = new DockPanel();

	private final Label message = new Label();
	private final Label errorMessage = new Label();
	private Grid grid = null;

	protected final TextBox username = GWT.create(TextBox.class);
	private final Label usernameLabel = GWT.create(Label.class);

	protected final PasswordTextBox password = GWT.create(PasswordTextBox.class);
	private final Label passwordLabel = GWT.create(Label.class);

	private final SimplePanel waitPanel = new SimplePanel();
	private final HorizontalPanel loginPanel = new HorizontalPanel();
	private Button loginButton;
	
	private final VerticalPanel logoutPanel = new VerticalPanel();
	private Label loggedUserName;
	private Button logoutButton;

	private Pair<String, String>[] enabledLanguages;
	private String selectedLanguage;
	private ListBox languageBox = GWT.create(ListBox.class);
	private Label languageLabel;

	private Boolean rememberMeAware;
	private CheckBox rememberMeCheckbox = GWT.create(CheckBox.class);

	private boolean initialized = false;

	/**
	 * Creates a new Login Panel.
	 */
	public LoginView() {
		this(null, null);
	}

	public LoginView(boolean rememberMeAware) {
		this(null, null, rememberMeAware);
	}

	public LoginView(Pair<String, String>[] enabledLanguages, String selectedLanguage) {
		this(enabledLanguages, selectedLanguage, false);
	}

	public LoginView(Pair<String, String>[] enabledLanguages, String selectedLanguage, boolean rememberMeAware) {
		this.enabledLanguages = enabledLanguages;
		this.selectedLanguage = selectedLanguage != null ? selectedLanguage
				: ((enabledLanguages != null && enabledLanguages.length > 0) ? enabledLanguages[0].getFirst() : null);
		this.rememberMeAware = rememberMeAware;
		initWidget(dock);
	}

	protected Panel getContainer() {
		return new VerticalPanel();
	}

	protected DockPanel getDock() {
		return dock;
	}

	protected Button getLoginButton() {
		return GWT.create(Button.class);
	}

	@Override
	public void setEnabledLanguages(Pair<String, String>[] languages) {
		if (this.enabledLanguages != null) {
			throw new IllegalArgumentException("Languages are already set. Unable to set different language set!");
		}
		this.enabledLanguages = languages;
	}

	@Override
	public Pair<String, String> getSelectedLanguage() {
		if (languageBox != null) {
			return enabledLanguages[languageBox.getSelectedIndex()];
		}
		throw new RuntimeException("Language selection support disabled - enabled languages not specified.");
	}

	@Override
	public void setSelectedLanguage(String language) {
		this.selectedLanguage = language;

		if (languageBox != null) {
			if (language != null && !language.isEmpty() && enabledLanguages != null) {
				int languageCount = enabledLanguages.length;
				for (int i = 0; i < languageCount; ++i) {
					if (language.equals(languageBox.getValue(i))) {
						languageBox.setSelectedIndex(i);
						break;
					}
				}
			}
		}
	}

	@Override
	public void setRememberMeEnabled(boolean enabled) {
		if (this.rememberMeAware != null && grid != null) {
			throw new IllegalArgumentException(
					"Remember me component is already set. Unable to set this component twice!");
		}
		this.rememberMeAware = enabled;
	}

	@Override
	protected void onLoad() {
		if (!initialized) {
			initComponents();
			initialized = true;
		}
	}

	protected void initComponents() {
		ensureLoginButton().setText(loginMessages.loginButton());
		ensureLoginButton().setTitle(loginMessages.loginTitle());

		Panel container = getContainer();
		usernameLabel.setText(loginMessages.usernamePrompt());
		passwordLabel.setText(loginMessages.passwordPrompt());

		int rowCount = 3;
		if (enabledLanguages != null) {
			rowCount++;
		}
		if (rememberMeAware) {
			rowCount++;
		}

		grid = new Grid(rowCount, 2);
		grid.setWidth("100%");

		dock.setHeight("100%");

		SimplePanel vpWrapper = new SimplePanel();
		Widget vpParent = container.getParent() != null ? container.getParent() : container;
		while (vpParent.getParent() != null) {
			vpParent = vpParent.getParent();
		}

		vpWrapper.add(vpParent);
		dock.add(vpWrapper, DockPanel.CENTER);

		container.add(message);
		container.add(errorMessage);
		container.add(grid);
		container.add(logoutPanel);

		int rowCounter = 0;

		if (Location.getQueryString().contains(LoginConstants.USER_NAME)) {
			String userName = Location.getParameter(LoginConstants.USER_NAME);
			username.setValue(userName);
		}
		grid.setCellSpacing(10);
		grid.setWidget(rowCounter, 0, usernameLabel);
		grid.setWidget(rowCounter++, 1, username);
		

		grid.setWidget(rowCounter, 0, passwordLabel);
		grid.setWidget(rowCounter++, 1, password);

		if (enabledLanguages != null) {
			rowCounter = initLanguageBox(enabledLanguages, rowCounter);
		}
		if (rememberMeAware) {
			rowCounter = initRememberMeBox(rowCounter);
		}

		loginPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
		final HorizontalPanel grouper = new HorizontalPanel();
		grouper.setStyleName("acris-LoginPanel-grouper");
		grouper.add(waitPanel);

		SimplePanel buttonWrapper = new SimplePanel();
		buttonWrapper.addStyleName("login-Button-wrapper");
		buttonWrapper.setWidget(ensureLoginButton());
		grouper.add(buttonWrapper);

		if (ensureCancelButton() != null) {
			SimplePanel cancelWrapper = new SimplePanel();
			cancelWrapper.addStyleName("login-Button-wrapper");
			cancelWrapper.setWidget(ensureCancelButton());
			ensureCancelButton().addStyleName("login-Button cancel-Button");
			ensureCancelButton().setText(loginMessages.cancelLogin());
			ensureCancelButton().setTitle(loginMessages.cancelTitle());
			grouper.add(cancelWrapper);
		}
		
		loginPanel.add(grouper);

		grid.setWidget(rowCounter, 1, loginPanel);
		
		final HTMLTable.CellFormatter cellFormatter = grid.getCellFormatter();
		cellFormatter.addStyleName(rowCounter, 1, "acris-LoginPanel-formatter-row");
		cellFormatter.setHorizontalAlignment(rowCounter, 1, HasAlignment.ALIGN_RIGHT);
		cellFormatter.addStyleName(0, 1, "acris-LoginPanel-formatter-row-first");

		ensureLoginButton().setEnabled(false);
		ensureLoginButton().getParent().addStyleName("login-Button-disabled");

		loggedUserName = new Label();
		SimplePanel loggedUserPanel = new SimplePanel();
		loggedUserPanel.addStyleName("logged-username-label");
		loggedUserPanel.setWidget(loggedUserName);
		
		SimplePanel logoutBtnWrapper = new SimplePanel();
		logoutBtnWrapper.addStyleName("logout-button-wrapper");
		ensureLogoutButton().setText(loginMessages.logoutButton());
		ensureLogoutButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				doLogout();
			}
		});
		logoutBtnWrapper.setWidget(ensureLogoutButton());
		logoutPanel.add(loggedUserPanel);
		logoutPanel.add(logoutBtnWrapper);
		logoutPanel.setVisible(false);
		
		setStyleNames();
	}

	private int initLanguageBox(Pair<String, String>[] enabledLanguages, int rowCounter) {
		languageLabel = new Label(loginMessages.languagePrompt());
		int selectedIndex = 0;
		int length = enabledLanguages.length;
		for (int i = 0; i < length; ++i) {
			Pair<String, String> enabledLanguage = enabledLanguages[i];
			languageBox.addItem(enabledLanguage.getSecond(), enabledLanguage.getFirst());
			if (enabledLanguage.getFirst() != null && enabledLanguage.getFirst().equals(selectedLanguage)) {
				selectedIndex = i;
			}
		}
		languageBox.setSelectedIndex(selectedIndex);
		grid.setWidget(rowCounter, 0, languageLabel);
		grid.setWidget(rowCounter, 1, languageBox);

		return rowCounter + 1;
	}

	private int initRememberMeBox(int rowCounter) {
		rememberMeCheckbox.setText(loginMessages.rememberMePrompt());
		grid.setWidget(rowCounter, 1, rememberMeCheckbox);
		return rowCounter + 1;
	}

	protected void setStyleNames() {
		dock.setStyleName("login-LoginPanel");
		message.addStyleName("login-Message");
		errorMessage.addStyleName("login-ErrorMessage");
		username.addStyleName("login-Username");
		usernameLabel.addStyleName("login-UsernameLabel");
		password.addStyleName("login-Password");
		passwordLabel.addStyleName("login-PasswordLabel");
		if (languageLabel != null) {
			languageLabel.addStyleName("login-LanguageLabel");
		}
		if (languageBox != null) {
			languageBox.addStyleName("login-LanguageBox");
		}
		if (rememberMeCheckbox != null) {
			rememberMeCheckbox.addStyleName("login-RememberMeBox");
		}
		ensureLoginButton().addStyleName("login-Button");
		ensureLogoutButton().addStyleName("login-Button");
		loginPanel.addStyleName("acris-login-LoginPanel");
	}

	/**
	 * Grab the focus. This will put the cursor in the text field for the user.
	 */
	@Override
	public void focus() {
		if (username.getText().trim().length() > 0) {
			password.setFocus(true);
		} else {
			username.setFocus(true);
		}
	}

	/**
	 * Checks the username and password fields and enables/disables the login
	 * button.
	 */
	@Override
	public void updateLoginEnabled() {
		if (ensureLoginButton() == null || ensureLoginButton().getParent() == null) {
			return;
		}
		if (ensureLoginButton().isEnabled()) {
			ensureLoginButton().getParent().removeStyleName("login-Button-disabled");
		} else {
			ensureLoginButton().getParent().addStyleName("login-Button-disabled");
		}
	}

	/**
	 * Reset the widget to the initial state.
	 */
	public void reset() {
		setMessage(null);
		setErrorMessage(null);
		username.setText("");
		password.setText("");
		updateLoginEnabled();
		waitPanel.clear();
	}

	/**
	 * Allow the widget to accept another login attempt.
	 */
	public void reenable() {
		waitPanel.clear();
		updateLoginEnabled();
	}

	/**
	 * Get the entered username.
	 * 
	 * @return the username currently entered.
	 */
	@Override
	public String getUsername() {
		return username.getText().trim();
	}

	@Override
	public void setUsername(String name) {
		username.setText(name);
	}

	/**
	 * Get the entered password.
	 * 
	 * @return the password currently entered.
	 */
	@Override
	public String getPassword() {
		return password.getText().trim();
	}

	@Override
	public void setPassword(String pass) {
		password.setText(pass);
	}

	/**
	 * Set the message to display to the user.
	 * 
	 * @param message
	 *            the message to display to the user.
	 */
	public void setMessage(final String message) {
		this.message.setText(message != null ? message : "");
	}

	/**
	 * Set the error message to display to the user.
	 * 
	 * @param message
	 *            the error message to display to the user.
	 */
	public void setErrorMessage(final String message) {
		this.errorMessage.setText(message != null ? message : "");
	}

	@Override
	public Widget asWidget() {
		ensureLoginButton();
		return this;
	}

	private Button ensureLoginButton() {
		if (this.loginButton == null) {
			this.loginButton = getLoginButton();
		}
		return this.loginButton;
	}
	
	protected Button ensureCancelButton() {
		return null;
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
	public HandlerRegistration addLoginButtonHandler(ClickHandler handler) {
		return ensureLoginButton().addClickHandler(handler);
	}
	
	@Override
	public HandlerRegistration addLogoutButtonHandler(ClickHandler handler) {
		return ensureLogoutButton().addClickHandler(handler);
	}

	@Override
	public HandlerRegistration addUsernameKeyHandler(KeyUpHandler handler) {
		return username.addKeyUpHandler(handler);
	}

	@Override
	public HandlerRegistration addPasswordKeyHandler(KeyUpHandler handler) {
		return password.addKeyUpHandler(handler);
	}

	@Override
	public HandlerRegistration addUsernameChangeHandler(ChangeHandler handler) {
		return username.addChangeHandler(handler);
	}

	@Override
	public HandlerRegistration addPasswordChangeHandler(ChangeHandler handler) {
		return password.addChangeHandler(handler);
	}

	@Override
	public HandlerRegistration addLanguageHandler(ChangeHandler handler) {
		return languageBox.addChangeHandler(handler);
	}

	@Override
	public void onLoginFailed() {
		username.setText("");
		password.setText("");
		if (Boolean.TRUE.equals(rememberMeAware)) {
			rememberMeCheckbox.setValue(false);
		}
	}

	@Override
	public boolean getRememberMe() {
		if (rememberMeAware) {
			return rememberMeCheckbox.getValue();
		}
		throw new RuntimeException("Remember me support disabled.");
	}

	@Override
	public void setRemeberMe(boolean rememberMe) {
		rememberMeCheckbox.setValue(rememberMe);
	}

	@Override
	public boolean isLoginEnabled() {
		return ensureLoginButton().isEnabled();
	}

	@Override
	public void setLoginEnabled(boolean enabled) {
		ensureLoginButton().setEnabled(enabled);
	}

	@Override
	public void changeState(Boolean loggedIn) {
		if (loggedIn) {
			changeLoginState(!loggedIn);
			logoutPanel.setVisible(true);
		} else {
			changeLoginState(!loggedIn);
			loggedUserName.setText("");
			logoutPanel.setVisible(false);
		}
	}
	
	private void changeLoginState(Boolean visible) {
		username.setVisible(visible);
		usernameLabel.setVisible(visible);
		password.setVisible(visible);
		passwordLabel.setVisible(visible);
		loginButton.setVisible(visible);
		loginPanel.setVisible(visible);
	}
	
	@Override
	public void setLoggedUser(GenericUserDTO user) {
		String loggedUserMsg = loginMessages.loggedUserMsg();
		if (user != null) {
			loggedUserMsg += user.getName() + " " + user.getSurname();
		}
		loggedUserName.setText(loggedUserMsg);
	}
	
	protected void clearLoginCookies() {
		Cookies.removeCookie(LoginConstants.LOGINNAME_COOKIE_NAME);
		Cookies.removeCookie(LoginConstants.LOGINPASSWORD_COOKIE_NAME);
	}

	@Override
	public void doLogout() {
		//TODO server side logout is not called
		clearLoginCookies();
		changeState(false);
	}

	@Override
	public HandlerRegistration addCancelHandler(ClickHandler handler) {
		if (ensureCancelButton() == null) {
			return null; 
		}
		return ensureCancelButton().addClickHandler(handler);
	}
}
