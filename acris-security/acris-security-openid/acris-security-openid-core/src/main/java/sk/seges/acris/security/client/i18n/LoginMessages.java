package sk.seges.acris.security.client.i18n;

import com.google.gwt.i18n.client.Messages;

public interface LoginMessages extends Messages {
    public String usernamePrompt();
    public String passwordPrompt();
    public String languagePrompt();
    public String rememberMePrompt();
    public String loginButton();
    public String logoutButton();
    public String loginTitle();
    public String invalidCredentials(String username);
    public String loginFailedTitle();
    public String loginSuccessfulTitle();
    public String loginProgress();
    public String loggedUserMsg();
    public String cancelLogin();
    public String cancelTitle();
}