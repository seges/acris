package sk.seges.acris.security.client.presenter;

/**
 * Callback to check the acceptability of the the username and password fields.
 * Use this to check minimum required length or required case of the user's
 * credentials.
 */
public interface LoginValidator {

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
