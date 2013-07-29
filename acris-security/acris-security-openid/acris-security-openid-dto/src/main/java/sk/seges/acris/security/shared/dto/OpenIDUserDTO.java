package sk.seges.acris.security.shared.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class OpenIDUserDTO implements Serializable {

	public static String OPENID_IDENTIFIER = "openID_identifier";
	public static String EMAIL_FROM_FETCH = "emailFromFetch";
	public static String EMAIL_FROM_SREG = "emailFromSReg";
	public static String SESSION_ID = "sessionID";
	public static String ENDPOINT_URL = "endpointURL";

	private static final long serialVersionUID = 1435482511152337161L;

	private Map<String, String> params = new HashMap<String, String>();
	private String locale;
	
	public OpenIDUserDTO() {
	}

	public Map<String, String> getParams() {
		return params;
	}

	public String getOpenIDIdentifier() {
		return params.get(OPENID_IDENTIFIER);
	}

	public String getEmail() {
		String emailFromFetch = params.get(EMAIL_FROM_FETCH);
		String emailFromSReg = params.get(EMAIL_FROM_SREG);

		if (emailFromFetch != null && !emailFromFetch.isEmpty()) {
			return emailFromFetch;
		} else {
			return emailFromSReg;
		}
	}

	public String getSessionId() {
		return params.get(SESSION_ID);
	}

	public String getEndpointUrl() {
		return params.get(ENDPOINT_URL);
	}
	
	public String getLocale() {
		return locale;
	}
	
	public void setLocale(String locale)  {
		this.locale = locale;
	}
}
