package sk.seges.acris.common.util;

public class URLUtils {
	private static final String PROTOCOL_SEPARATOR = "://";
	private static final String URL_SEPARATOR = "/";
	private static final String QUERY_LOCALE = "?locale=";

	public static String getLocalizedServerHost(String originalServerHost, String domainExtension) {
		int lastIndex = originalServerHost.lastIndexOf(".");
		
		if (lastIndex == -1) {
			return originalServerHost;
		}
		
		String rightPart = originalServerHost.substring(lastIndex);
		
		int portSeparatorIndex = rightPart.indexOf(":");
		int slashSeparatorIndex = rightPart.indexOf(URL_SEPARATOR);
		
		int separatorIndex = portSeparatorIndex;
		
		if (portSeparatorIndex == -1) {
			separatorIndex = slashSeparatorIndex; 
		}
		
		if (separatorIndex == -1) {
			return originalServerHost.substring(0, lastIndex + 1) + domainExtension;
		}
				
		return originalServerHost.substring(0, lastIndex + 1) + domainExtension + originalServerHost.substring(separatorIndex + lastIndex);
	}

	public static String createHRefWithNiceURL(String url, String niceURL) {
		int index = url.indexOf(PROTOCOL_SEPARATOR);
		index = url.indexOf(URL_SEPARATOR, index + 3);
		// url.replaceAll(GWT.getModuleName() + "/",
		// "").replaceAll(ModulesDefinition.APP_NAME + "/", "") +
		// content.getNiceUrl();
		String domainPart = url.substring(0, index);
		return (domainPart.endsWith(URL_SEPARATOR) ? domainPart : domainPart
				+ URL_SEPARATOR)
				+ niceURL;
	}
	
	/**
	 * 
	 * @param locationHRef URL to transform locale in. Usually Location.getHref().
	 * @param hostName Host name part of the URL. Usually Location.getHostName().
	 * @param domainName
	 * @param currentLocale Current locale on the page. Usually as a parameter in URL. Can be fetched using Location.getParameter("locale")
	 * @param targetLanguage 
	 * @return
	 */
	public static String transformURLToRequiredLocale(String locationHRef, String hostName, String domainName, String currentLocale, String targetLanguage) {
		String href;
		if(domainName != null && domainName.length() > 0) {
			href = replaceFirstLevelDomainName(locationHRef, hostName, domainName);
		} else {
			href = locationHRef;
		}
		
		int index = href.indexOf(QUERY_LOCALE);
		
		if(index == -1) {
			return appendLocale(href, targetLanguage);
		} 

		return replaceLocale(href, index, currentLocale, targetLanguage);
	}
	
	/**
	 * 
	 * @param href URL to be domain replaced in.
	 * @param hostName Host name part of href URL.
	 * @param requestedFirstLevelDomainName First level domain name to replace to.
	 * @return
	 */
	public static String replaceFirstLevelDomainName(String href, String hostName, String requestedFirstLevelDomainName) {
		int add = href.indexOf(hostName);
		int domainIndex = hostName.lastIndexOf(".");
		
		if(domainIndex == -1) {
			//there is no domain information (ie. localhost)
			return href;
		}
		
		String domain = hostName.substring(domainIndex + 1);
		try {
			Integer.parseInt(domain);
			// it is IP, we won't change it
			return href;
		} catch(NumberFormatException e) {
			// naaasty
			return href.substring(0, domainIndex + add + 1) + requestedFirstLevelDomainName + href.substring(domainIndex + add + 1 + domain.length());
		}			
	}
	
	private static String appendLocale(String href, String targetLanguage) {
		int index = href.indexOf("#");
		if(index == -1) {
			return href + QUERY_LOCALE + targetLanguage;
		}
		
		return href.substring(0, index) + QUERY_LOCALE + targetLanguage + href.substring(index, href.length());
	}
	
	private static String replaceLocale(String href, int localeIndex, String currentLocale, String targetLanguage) {
		return href.substring(0, localeIndex + QUERY_LOCALE.length()) + targetLanguage + href.substring(localeIndex + QUERY_LOCALE.length() + targetLanguage.length()); 
	}
	
	public static void main(String[] args) {
        String url = "middleman.seges.sk/#homie";
        String newUrl = transformURLToRequiredLocale(url, "middleman.seges.sk", "sk", "sk", "en");
        System.out.println(newUrl);
    }
}
