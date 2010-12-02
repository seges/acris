package sk.seges.acris.security.client.view;

import java.util.EventListener;

import sk.seges.acris.security.client.event.LoginEvent;
import sk.seges.acris.security.client.handler.LoginHandler;
import sk.seges.acris.security.client.i18n.LoginMessages;
import sk.seges.acris.security.client.presenter.LoginPresenter.LoginDisplay;
import sk.seges.acris.security.shared.util.LoginConstants;
import sk.seges.acris.util.Pair;
import sk.seges.acris.util.URLUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.LocaleInfo;
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
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LoginView extends Composite implements LoginDisplay {

	private final DockPanel dock = new DockPanel();

	private LoginMessages loginMessages = (LoginMessages) GWT.create(LoginMessages.class);

	private final Label message = new Label();
	private final Label errorMessage = new Label();
	private Grid grid = null;

	protected final TextBox username = GWT.create(TextBox.class);
	private final Label usernameLabel = GWT.create(Label.class);

	protected final PasswordTextBox password = GWT.create(PasswordTextBox.class);
	private final Label passwordLabel = GWT.create(Label.class);

	private final SimplePanel waitPanel = new SimplePanel();
	private final HorizontalPanel loginPanel = new HorizontalPanel();
	private final Button loginButton;

	private Pair<String, String>[] enabledLanguages;
	private String selectedLanguage;
	private ListBox languageBox;
	private Label languageLabel;

	private Boolean rememberMeAware;
	private CheckBox rememberMeCheckbox;

	private boolean initialized = false;

	/**
	 * The current LoginValidator. Defaults to a validator that simply checks if
	 * the username and password are not empty.
	 */
	private LoginValidator validator = new LoginValidator() {
		public boolean validateUsername(final String username) {
			return username.trim().length() > 0;
		}

		public boolean validatePassword(final String password) {
			return password.trim().length() > 0;
		}
	};

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
		this.loginButton = getLoginButton();
		initWidget(dock);
	}

	protected VerticalPanel getContainer() {
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
	public void setSelectedLanguage(String selectedLanguage) {
		this.selectedLanguage = selectedLanguage;
	}

	@Override
	public void setRememberMeMode(boolean rememberMeAware) {
		if (this.rememberMeAware != null && grid != null) {
			throw new IllegalArgumentException(
					"Remember me component is already set. Unable to set this component twice!");
		}
		this.rememberMeAware = rememberMeAware;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if (!initialized) {
			initComponents();
			initialized = true;
		}
	}

	protected void initComponents() {
		String language = Cookies.getCookie(LoginConstants.LANGUAGE_COOKIE_NAME);
		String localeName = LocaleInfo.getCurrentLocale().getLocaleName();
		if (language != null && localeName != null && !language.equals(localeName)) {
			String url = URLUtils.transformURLToRequiredLocale(Location.getHref(), Location.getHostName(), null,
					localeName, language);
			if (!Location.getHref().equals(url)) {
				Location.assign(url);
			}
		}

		setVisible(false);
		loginButton.setText(loginMessages.loginButton());

		VerticalPanel vp = getContainer();
		vp.setTitle(loginMessages.loginTitle());
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
		Widget vpParent = vp.getParent() != null ? vp.getParent() : vp;
		while (vpParent.getParent() != null) {
			vpParent = vpParent.getParent();
		}

		vpWrapper.add(vpParent);
		dock.add(vpWrapper, DockPanel.CENTER);

		vp.add(message);
		vp.add(errorMessage);
		vp.add(grid);

		int rowCounter = 0;

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
		buttonWrapper.setWidget(loginButton);
		grouper.add(buttonWrapper);

		loginPanel.add(grouper);

		grid.setWidget(rowCounter, 1, loginPanel);

		final HTMLTable.CellFormatter cellFormatter = grid.getCellFormatter();
		cellFormatter.addStyleName(rowCounter, 1, "acris-LoginPanel-formatter-row");
		cellFormatter.setHorizontalAlignment(rowCounter, 1, HasAlignment.ALIGN_RIGHT);
		cellFormatter.addStyleName(0, 1, "acris-LoginPanel-formatter-row-first");

		usernameLabel.addClickHandler(new ClickHandler() {
			public void onClick(final ClickEvent event) {
				username.setFocus(true);
			}
		});

		passwordLabel.addClickHandler(new ClickHandler() {

			public void onClick(final ClickEvent event) {
				password.setFocus(true);
			}
		});

		readCookies();

		final LoginKeyboardListener keyboardListener = new LoginKeyboardListener();
		username.addKeyUpHandler(keyboardListener);
		password.addKeyUpHandler(keyboardListener);

		final LoginChangeListener loginChangeListener = new LoginChangeListener();
		username.addChangeHandler(loginChangeListener);
		password.addChangeHandler(loginChangeListener);

		loginButton.addClickHandler(new ClickHandler() {
			public void onClick(final ClickEvent event) {
				login();
			}
		});

		loginButton.setEnabled(false);
		loginButton.getParent().addStyleName("login-Button-disabled");

		setStyleNames();
	}

	private void readCookies() {
		String language = Cookies.getCookie(LoginConstants.LANGUAGE_COOKIE_NAME);
		String loginName = Cookies.getCookie(LoginConstants.LOGINNAME_COOKIE_NAME);
		String loginPassword = Cookies.getCookie(LoginConstants.LOGINPASSWORD_COOKIE_NAME);
		if (language != null && !language.isEmpty() && enabledLanguages != null) {
			int languageCount = enabledLanguages.length;
			for (int i = 0; i < languageCount; ++i) {
				if (language.equals(languageBox.getValue(i))) {
					languageBox.setSelectedIndex(i);
					break;
				}
			}
		}
		if (null != loginName && !loginName.isEmpty() && loginPassword != null && !loginPassword.isEmpty()) {
			username.setText(loginName);
			password.setText(loginPassword);
			if (rememberMeAware) {
				rememberMeCheckbox.setValue(true);
			}
			login();
		} else {
			setVisible(true);
		}
	}

	private int initLanguageBox(Pair<String, String>[] enabledLanguages, int rowCounter) {
		languageBox = new ListBox();
		languageLabel = new Label(loginMessages.languagePrompt());
		int selectedIndex = 0;
		int length = enabledLanguages.length;
		for (int i = 0; i < length; ++i) {
			Pair<String, String> enabledLanguage = enabledLanguages[i];
			languageBox.addItem(enabledLanguage.getSecond(), enabledLanguage.getFirst());
			if (enabledLanguage.getFirst() != null && enabledLanguage.getFirst().equals(selectedLanguage)) {
				selectedIndex = i;
				break;
			}
		}
		languageBox.setSelectedIndex(selectedIndex);
		grid.setWidget(rowCounter, 0, languageLabel);
		grid.setWidget(rowCounter, 1, languageBox);

		languageBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				selectedLanguage = getSelectedLanguage().getFirst();
				Cookies.setCookie(LoginConstants.LANGUAGE_COOKIE_NAME, selectedLanguage);
				Location.assign(URLUtils.transformURLToRequiredLocale(Location.getHref(), Location.getHostName(), null,
						LocaleInfo.getCurrentLocale().getLocaleName(), selectedLanguage));
			}
		});

		return rowCounter + 1;
	}

	private int initRememberMeBox(int rowCounter) {
		rememberMeCheckbox = new CheckBox(loginMessages.rememberMePrompt());
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
		loginButton.addStyleName("login-Button");
		loginPanel.addStyleName("acris-login-LoginPanel");
	}

	/**
	 * Grab the focus. This will put the cursor in the text field for the user.
	 */
	public void focus() {
		if (username.getText().trim().length() > 0) {
			password.setFocus(true);
		} else {
			username.setFocus(true);
		}
	}

	/**
	 * Check the username and password fields and enable/disable the login
	 * button.
	 */
	public void updateLoginEnabled() {
		loginButton.setEnabled(validator.validateUsername(username.getText())
				&& validator.validatePassword(password.getText()));
		if (loginButton.isEnabled()) {
			loginButton.getParent().removeStyleName("login-Button-disabled");
		} else {
			loginButton.getParent().addStyleName("login-Button-disabled");
		}
	}

	/**
	 * Trigger a login event. This is the equivalent of the user clicking the
	 * Login button.
	 */
	public void login() {
		if (Boolean.TRUE.equals(rememberMeAware)) {
			if (Boolean.TRUE.equals(rememberMeCheckbox.getValue())) {
				Cookies.setCookie(LoginConstants.LOGINNAME_COOKIE_NAME, username.getText());
				Cookies.setCookie(LoginConstants.LOGINPASSWORD_COOKIE_NAME, username.getText());
				try {
					Cookies.setCookie(LoginConstants.LANGUAGE_COOKIE_NAME, getSelectedLanguage().getFirst());
				} catch (RuntimeException ignored) {
				}
			}
		}
		loginButton.setEnabled(false);
		this.fireEvent(new LoginEvent(getUsername(), getPassword(),
				(null != languageBox ? getSelectedLanguage() : null)));
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
	public String getUsername() {
		return username.getText();
	}

	public void setUsername(String name) {
		username.setText(name);
	}

	/**
	 * Get the entered password.
	 * 
	 * @return the password currently entered.
	 */
	public String getPassword() {
		return password.getText();
	}

	public void setPassword(String pass) {
		password.setText(pass);
	}

	public Pair<String, String> getSelectedLanguage() {
		if (null != languageBox) {
			return enabledLanguages[languageBox.getSelectedIndex()];
		}
		throw new RuntimeException("Language selection support disabled - enabled languages not specified.");
	}

	public boolean getRememberMeValue() {
		if (rememberMeAware) {
			return rememberMeCheckbox.getValue();
		}
		throw new RuntimeException("Remember me support disabled.");
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

	/**
	 * Set the callback to validate the username and password fields. If these
	 * fields fail validation the Login button will be disabled.
	 * 
	 * @param validator
	 *            callaback to check the username and password.
	 */
	public void setLoginValidator(final LoginValidator validator) {
		if (validator == null) {
			throw new IllegalArgumentException("The LoginValidator cannot be set to null.");
		}
		this.validator = validator;
	}

	@Override
	public void onLoginFailed() {
		username.setText("");
		password.setText("");
		if (Boolean.TRUE.equals(rememberMeAware)) {
			rememberMeCheckbox.setValue(false);
		}
		clearLoginCookies();
		setVisible(true);
	}

	private void clearLoginCookies() {
		Cookies.removeCookie(LoginConstants.LOGINNAME_COOKIE_NAME);
		Cookies.removeCookie(LoginConstants.LOGINPASSWORD_COOKIE_NAME);
		Cookies.removeCookie(LoginConstants.LANGUAGE_COOKIE_NAME);
	}

	/**
	 * Callback for a login attempt.
	 */
	public static interface LoginListener extends EventListener {
		/**
		 * Fired when the user tries to login. This happens either when the user
		 * presses the submit button or presses the enter key.
		 * 
		 * @param loginPanel
		 *            the origin of the event.
		 */
		public void onSubmit(LoginEvent loginEvent);
	}

	/**
	 * Callback to check the acceptablity of the the username and password
	 * fields. Use this to check minimum required length or required case of the
	 * user's crenditials.
	 */
	public static interface LoginValidator {
		/**
		 * Check the username for validity.
		 * 
		 * @param username
		 *            the currently entered username.
		 * @return true if this is a possibly valid username, else false.
		 */
		public boolean validateUsername(String username);

		/**
		 * Check the password for validity.
		 * 
		 * @param password
		 *            the currently entered password.
		 * @return true if this is a possibly valid password, else false.
		 */
		public boolean validatePassword(String password);
	}

	/**
	 * Listen for key presses and update the login button.
	 */
	private class LoginKeyboardListener implements KeyUpHandler {

		@Override
		public void onKeyUp(KeyUpEvent event) {
			updateLoginEnabled();
			if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
				if (loginButton.isEnabled()) {
					login();
				}
			}
		}
	}

	/**
	 * Listen for changes and update the login button.
	 */
	private class LoginChangeListener implements ChangeHandler {

		@Override
		public void onChange(ChangeEvent event) {
			updateLoginEnabled();
		}
	}

	@Override
	public void addLoginHandler(LoginHandler handler) {
		addHandler(handler, LoginEvent.getType());
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public void showMessage(String message) {
		Window.alert(message);
	}
}
