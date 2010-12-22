package sk.seges.acris.security.shared.data;

import java.io.Serializable;
import java.util.Map;

public class OpenIDUser implements Serializable {

	private static final long serialVersionUID = 1435482511152337161L;

	private String redirectUrl;
	private Map<?, ?> params;

	public OpenIDUser() {	
	}
	
	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public Map<?, ?> getParams() {
		return params;
	}

	public void setParams(Map<?, ?> params) {
		this.params = params;
	}
}
