package sk.seges.acris.security.server.core.user_management.context;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.codehaus.jettison.json.JSONObject;

import sk.seges.acris.security.server.core.user_management.context.api.UserProviderService;
import sk.seges.acris.security.shared.exception.ServerException;
import sk.seges.acris.security.shared.user_management.context.APIKeyUserContext;
import sk.seges.acris.security.shared.user_management.domain.api.UserContext;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.acris.security.shared.user_management.domain.dto.GenericUserDTO;

public class APIKeyUserService implements UserProviderService {

	private String apiKeyURL;
	
	public APIKeyUserService(String apiKeyURL) { 
		this.apiKeyURL = apiKeyURL;
	}
	
	@Override
	public UserData<?> getLoggedUser(UserContext userContext) {
		StringBuilder s = new StringBuilder();
		try{
			char[] buf = new char[2048];
			URLConnection connection = new URL(getUrl(userContext)).openConnection();
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
			e.printStackTrace();
		}
		return createUser(s.toString());
	}
	
	public UserData<?> createUser(String result) {
		Boolean allowed = false;
		try {
			allowed = (Boolean) new JSONObject(result).get("allowed");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (allowed) {
			UserData<?> adminUser = (UserData<?>) new GenericUserDTO();
			adminUser.setEnabled(true);
			adminUser.setUserAuthorities(new ArrayList<String>());
			return adminUser;
		}
		
		return null;
	}

	@Override
	public String getLoggedUserName(UserContext userContext) throws ServerException {
		return ((APIKeyUserContext)userContext).getWebId();
	}
	
	private String getUrl(UserContext userContext) {
		String url = apiKeyURL;
		url += "&" + ((APIKeyUserContext) userContext).getWebId();
		url += "&" + ((APIKeyUserContext) userContext).getApiKey();
		return url;
	}
}
