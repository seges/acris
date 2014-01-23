package sk.seges.acris.security.server.core.user_management.context;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

import sk.seges.acris.security.server.core.session.ServerSessionProvider;
import sk.seges.acris.security.server.core.user_management.context.api.UserProviderService;
import sk.seges.acris.security.server.util.LoginConstants;
import sk.seges.acris.security.shared.exception.ServerException;
import sk.seges.acris.security.shared.session.ClientSession;
import sk.seges.acris.security.shared.user_management.context.APIKeyUserContext;
import sk.seges.acris.security.shared.user_management.domain.api.ApiKeyUserProvider;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;
import sk.seges.acris.security.shared.user_management.domain.api.UserContext;

public class APIKeyUserService implements UserProviderService {

	public static final String APIKEY_PARAMETER = "apiKey";
	public static final String WEBID_PARAMETER = "webId";
	public static final String RESULT_PARAMETER = "allowed";
	
	private static final Logger log = Logger.getLogger(APIKeyUserService.class);
	
	private String apiKeyURL;
	protected ServerSessionProvider sessionProvider;
	private ApiKeyUserProvider apiKeyUserProvider;
	
	public APIKeyUserService(String apiKeyURL, ServerSessionProvider sessionProvider, ApiKeyUserProvider apiKeyUserProvider) { 
		this.apiKeyURL = apiKeyURL;
		this.sessionProvider = sessionProvider;
		this.apiKeyUserProvider = apiKeyUserProvider;
	}
	
	@Override
	public ClientSession getLoggedSession(UserContext userContext) {
		if (isValid(userContext)) {
			HttpSession session = sessionProvider.getSession();
			session.setAttribute(LoginConstants.ACRIS_API_KEY_STRING, ((APIKeyUserContext) userContext).getApiKey());
			session.setAttribute(LoginConstants.LOGIN_TOKEN_NAME, createLoginToken(userContext.getWebId()));
			ClientSession clientSession = new ClientSession();
			clientSession.setUser(apiKeyUserProvider.createUser(((APIKeyUserContext) userContext).getApiKey()));
			
			return clientSession;
		}
		
		return null;
	}
	
	private LoginToken createLoginToken(final String webId) {
		return new LoginToken() {
			private static final long serialVersionUID = 6945107263245752855L;
			@Override
			public String getWebId() {
				return webId;
			}
			
			@Override
			public boolean isAdmin() {
				return true;
			}
		};
	}
	
	/**
	 * Validate apiKey with webId from request
	 * against third party service
	 * 
	 * @return - true if apiKey is valid for webId
	 * otherwise return false
	 */
	public Boolean isValid(UserContext userContext) {
		StringBuilder s = new StringBuilder();
		try{
			char[] buf = new char[2048];

			String url = getUrl(userContext);
			log.info("Veryfing user against " + url);
			
			URLConnection connection = new URL(url).openConnection();
			connection.connect();
			InputStream is = connection.getInputStream();
			Reader r = new InputStreamReader(is, "UTF-8");
			  while (true) {
			    int n = r.read(buf);
			    if (n < 0)
			      break;
			    s.append(buf, 0, n);
			  }
			is.close();	
		} catch (Exception e) {
			log.error("Comunication with APIKey service failed", e);
		}

		return getParsedResult(s.toString());
	}
	
	/**
	 * Parse result from third party service
	 * to boolean representation
	 * Expected result:
	 * {"allowed" : boolean}
	 */
	protected Boolean getParsedResult(String result) {
		log.info("Response =" + result);

		Boolean allowed = false;
		if (result != null && !result.isEmpty()) {
			try {
				allowed = (Boolean.valueOf((String)new JSONObject(result).get(RESULT_PARAMETER)));
			} catch (Exception e) {
				log.error("APIKey service do not return correct result = " + result, e);
			}
		}
		return allowed;
	}
	
	@Override	
	public String getLoggedUserName(UserContext userContext) throws ServerException {
		return userContext.getWebId();
	}
	
	private String getUrl(UserContext userContext) {
		String url = apiKeyURL;
		url += apiKeyURL.contains("?") ? "&" : "?" + WEBID_PARAMETER + "=" + userContext.getWebId();
		url += "&" + APIKEY_PARAMETER + "=" + ((APIKeyUserContext) userContext).getApiKey();
		return url;
	}
}
