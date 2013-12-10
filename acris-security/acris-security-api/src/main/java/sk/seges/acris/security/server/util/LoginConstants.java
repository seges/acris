package sk.seges.acris.security.server.util;

public class LoginConstants {

	public static final String LOGGED_ROLE_NAME = "logged_user_role";

	// Login parameters
	public static final String ACRIS_THEME_STRING = "acristheme";
	public static final String ACRIS_LOCALE_STRING = "locale";
	public static final String ACRIS_CODESVR_STRING = "gwt.codesvr";
	public static final String ACRIS_SESSION_ID_STRING = "sessionid";
	public static final String ACRIS_API_KEY_STRING = "apikey";
	public static final String ACRIS_WEB_ID_STRING = "webid";
	public static final String USER_NAME = "userName";
	
	//OpenID login integration parameters
	public static final String DOMAIN_ATTRIBUTE = "domain";
	public static final String FROM_URL_ATTRIBUTE = "url";
	public static final String OPENID_PROVIDER_ATTRIBUTE = "provider";

	public static final String GOOGLE_APPS_ATTRIBUTE_VALUE = "google";
	
	public static final String LOGIN_RUN_AS = "login.runas";
	public static final String LOGIN_TOKEN_NAME = "login.token.name";
	public static final String CLIENT_SESSION_NAME = "client.session";
	
	// Cookies
	public static final String JSESSIONID_COOKIE_NAME = "JSESSIONID";
	public static final String LANGUAGE_COOKIE_NAME = "acris-languageCookie";
	public static final String LOGINNAME_COOKIE_NAME = "acris-loginNameCookie";
	public static final String LOGINPASSWORD_COOKIE_NAME = "acris-loginPasswordCookie";
	public static final String OPENID_COOKIE_NAME = "acris-openIdCookie";

	// OpenID Identifiers
//  MOVED TO enum OpenIDProvider
//	public static final String GOOGLE_IDENTIFIER = "https://www.google.com/accounts/o8/id";
//	public static final String YAHOO_IDENTIFIER = "http://me.yahoo.com";
//	public static final String AOL_IDENTIFIER = "https://www.aol.com";
//	public static final String SEZNAM_IDENTIFIER = "http://www.seznam.cz";
//	public static final String BLOGGER_IDENTIFIER = "http://username.wordpress.com";
//	public static final String MYOPENID_IDENTIFIER = "https://www.myopenid.com";
}
