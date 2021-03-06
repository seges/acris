package sk.seges.acris.security.shared.util;

import sk.seges.acris.security.server.util.LoginConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window.Location;

public class LoginUtils {

    public static String getModuleBaseUrl() {
		String baseUrl = GWT.getModuleBaseURL();

		int indexOf = baseUrl.indexOf(GWT.getModuleName());
		if (indexOf > -1) {
			baseUrl = baseUrl.substring(0, indexOf);
		}

		return baseUrl;
	}

	public static String getCommonQueryString() {
		return getCommonQueryString(null);
	}

	public static String getCommonQueryString(String sessionId) {
		String theme = Location.getParameter(LoginConstants.ACRIS_THEME_STRING);
		String locale = Location.getParameter(LoginConstants.ACRIS_LOCALE_STRING);
		String codesvr = Location.getParameter(LoginConstants.ACRIS_CODESVR_STRING);

		String query = "";
		query += theme != null && !theme.isEmpty() ? "&" + LoginConstants.ACRIS_THEME_STRING + "=" + theme : "";
		query += locale != null && !locale.isEmpty() ? "&" + LoginConstants.ACRIS_LOCALE_STRING + "=" + locale : "";
		query += codesvr != null && !codesvr.isEmpty() ? "&" + LoginConstants.ACRIS_CODESVR_STRING + "=" + codesvr : "";
		query += sessionId != null && !sessionId.isEmpty() ? "&" + LoginConstants.ACRIS_SESSION_ID_STRING + "="
				+ sessionId : "";

		if (!query.isEmpty()) {
			query = query.replaceFirst("&", "?");
		}

		return query;
	}
}
