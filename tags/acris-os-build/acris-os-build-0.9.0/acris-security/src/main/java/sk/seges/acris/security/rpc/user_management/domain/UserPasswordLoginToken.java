/**
 * 
 */
package sk.seges.acris.security.rpc.user_management.domain;

/**
 * Basic {@link LoginToken} implementation with username, password and language
 * information.
 * 
 * @author ladislav.gazo
 */
public class UserPasswordLoginToken implements LoginToken {
	private static final long serialVersionUID = 1338554746369372705L;

	private String username;
	private String password;
	private String language;

	public UserPasswordLoginToken() {}

	public UserPasswordLoginToken(String username, String password, String language) {
		super();
		this.username = username;
		this.password = password;
		this.language = language;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
}
