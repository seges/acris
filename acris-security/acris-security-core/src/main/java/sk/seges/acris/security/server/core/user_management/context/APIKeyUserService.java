package sk.seges.acris.security.server.core.user_management.context;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

import sk.seges.acris.security.server.core.session.ServerSessionProvider;
import sk.seges.acris.security.server.core.user_management.context.api.UserProviderService;
import sk.seges.acris.security.shared.exception.ServerException;
import sk.seges.acris.security.shared.user_management.context.APIKeyUserContext;
import sk.seges.acris.security.shared.user_management.domain.api.UserContext;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.acris.security.shared.user_management.domain.dto.GenericUserDTO;
import sk.seges.acris.security.shared.util.LoginConstants;

public class APIKeyUserService implements UserProviderService {

	public static final String APIKEY_PARAMETER = "apiKey";
	public static final String WEBID_PARAMETER = "webId";
	
	private static final Logger logger = Logger.getLogger(APIKeyUserService.class);
	
	private String apiKeyURL;
	private ServerSessionProvider sessionProvider;
	
	public APIKeyUserService(String apiKeyURL, ServerSessionProvider sessionProvider) { 
		this.apiKeyURL = apiKeyURL;
		this.sessionProvider = sessionProvider;
	}
	
	@Override
	public UserData<?> getLoggedUser(UserContext userContext) {
		StringBuilder s = new StringBuilder();
		try{
			char[] buf = new char[2048];

			String url = getUrl(userContext);
			logger.info("Veryfing username against " + url);
			
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
			logger.error("Comunication with APIKey service failed", e);
		}

		Boolean allowed = getParsedResult(s.toString());
		if (allowed) {
			sessionProvider.getSession().setAttribute(LoginConstants.ACRIS_API_KEY_STRING, ((APIKeyUserContext) userContext).getApiKey());
			return createUser();
		}

		return null;
	}
	
	protected Boolean getParsedResult(String result) {
		logger.info("Response =" + result);

		Boolean allowed = false;
		if (result != null && !result.isEmpty()) {
			try {
				allowed = (Boolean.valueOf((String)new JSONObject(result).get("allowed")));
			} catch (Exception e) {
				logger.error("APIKey service do not return correct result = " + result, e);
			}
		}
		
		return allowed;
	}
	
	public UserData<?> createUser() {
		UserData<?> adminUser = (UserData<?>) new GenericUserDTO();
		adminUser.setEnabled(true);
		adminUser.setUserAuthorities(new ArrayList<String>());
		return adminUser;
	}

	@Override
	public String getLoggedUserName(UserContext userContext) throws ServerException {
		return ((APIKeyUserContext)userContext).getWebId();
	}
	
	private String getUrl(UserContext userContext) {
		String url = apiKeyURL;
		url += apiKeyURL.contains("?") ? "&" : "?" + WEBID_PARAMETER + "=" + ((APIKeyUserContext) userContext).getWebId();
		url += "&" + APIKEY_PARAMETER + "=" + ((APIKeyUserContext) userContext).getApiKey();
		return url;
	}
}
