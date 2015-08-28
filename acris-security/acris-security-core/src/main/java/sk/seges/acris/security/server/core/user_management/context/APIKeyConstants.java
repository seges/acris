package sk.seges.acris.security.server.core.user_management.context;

/**
 * @author psloboda
 */
public interface APIKeyConstants {
	public static final String APIKEY_PARAMETER = "apiKey";
	public static final String WEBID_PARAMETER = "webId";
	public static final String RESULT_PARAMETER = "allowed";
	public static final String RESULT_PARAMETER_FTP_USER = "user";
	public static final String RESULT_PARAMETER_FTP_PASS = "password";
	public static final String SESSION_PARAMETER_FTP_USER = "ftp." + RESULT_PARAMETER_FTP_USER;
	public static final String SESSION_PARAMETER_FTP_PASS = "ftp." + RESULT_PARAMETER_FTP_PASS;
}
