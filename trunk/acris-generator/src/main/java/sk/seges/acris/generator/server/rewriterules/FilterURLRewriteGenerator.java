package sk.seges.acris.generator.server.rewriterules;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sk.seges.acris.util.URLUtils;

@Component
@Scope("prototype")
@Qualifier(value = "filter-url-rewrite")
public class FilterURLRewriteGenerator extends AbstractNiceURLGenerator {

	private String serverHost = null;
	
	protected String getDefaultRewriteRule() {

		if (serverHost == null) {
			String virtualServer = virtualServerName;
			
			if (virtualServerPort != null && virtualServerPort != 80) {
				virtualServer = virtualServer + ":" + virtualServerPort;
			}

			throw new RuntimeException("Do it!!!");

//			serverHost = URLUtils.getLocalizedServerHost(virtualServer, ELocale.getLocaleByLanguage(lang_country).getDomain());
		}
		
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + NEW_LINE +
				NEW_LINE +
				"<!DOCTYPE urlrewrite" + NEW_LINE +
				"	PUBLIC \"-//tuckey.org//DTD UrlRewrite 3.0//EN\"" + NEW_LINE +
				"	\"http://tuckey.org/res/dtds/urlrewrite3.0.dtd\">" + NEW_LINE +
				NEW_LINE +
				"<urlrewrite>" + NEW_LINE + "";
//				getRewriteRule("", "/" +  ModulesDefinition.APP_NAME + "/" + ModulesDefinition.MODULE_NAME + "/" + "__acris_token_themes.html");
				//aj tu
	}

	protected String getPermanentRedirectRewriteRule(String fromURL, String toURL) {
		String rewriteURL = "	<rule>" + NEW_LINE;
		
//		if (redirectCondition) {
//			rewriteURL += "		<condition name=\"host\" operator=\"equal\">" + serverHost + "</condition>" + NEW_LINE;
//		}

		rewriteURL += "		<from>^/" + fromURL + "$</from>" + NEW_LINE +
					"		<to type=\"permanent-redirect\">" + toURL + "</to>" + NEW_LINE +
					"	</rule>" + NEW_LINE;
		
//		if (redirectCondition) {
//			rewriteURL += "	<rule>" + NEW_LINE +
//						  "		<condition name=\"host\" operator=\"equal\">" + serverHost.replaceAll("www\\.", "") + "</condition>" + NEW_LINE +
//						  "		<from>^/" + fromURL + "$</from>" + NEW_LINE +
//						  "		<to type=\"permanent-redirect\">" + toURL + "</to>" + NEW_LINE +
//						  "	</rule>" + NEW_LINE;
//		}
		
		return rewriteURL;
	}

	protected String getRewriteRule(String fromURL, String toURL) {
		String rewriteURL = "	<rule>" + NEW_LINE;
		
//		if (redirectCondition) {
//			rewriteURL += "		<condition name=\"host\" operator=\"equal\">" + serverHost + "</condition>" + NEW_LINE;
//		}

		rewriteURL +=
		            "     <condition name=\"host\" operator=\"equal\">www.goodwill.eu.sk</condition>" + NEW_LINE +
		            "		<from>^/" + fromURL + "$</from>" + NEW_LINE +
					"		<to type=\"forward\">" + toURL + "</to>" + NEW_LINE +
					"	</rule>" + NEW_LINE;
		
//		if (redirectCondition) {
//			rewriteURL += "	<rule>" + NEW_LINE +
//						  "		<condition name=\"host\" operator=\"equal\">" + serverHost.replaceAll("www\\.", "") + "</condition>" + NEW_LINE +
//						  "		<from>^/" + fromURL + "$</from>" + NEW_LINE +
//						  "		<to type=\"forward\">" + toURL + "</to>" + NEW_LINE +
//						  "	</rule>" + NEW_LINE;
//		}
		
		return rewriteURL;
	}

	protected String getFinalRewriteRule() {
		return "</urlrewrite>";
	}
}
