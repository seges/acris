package sk.seges.acris.generator.server.rewriterules;

import java.util.ArrayList;
import java.util.List;

public class BaseURLGenerator {
	private final List<String> clientDirectoryStructure = new ArrayList<String>();
	
	private final AbstractNiceURLGenerator niceURLGenerator;

	public BaseURLGenerator(AbstractNiceURLGenerator niceURLGenerator) {
		this.niceURLGenerator = niceURLGenerator;
		clientDirectoryStructure.add("styles");
		clientDirectoryStructure.add("images");
		clientDirectoryStructure.add("js");
	}
	
	public String getBaseURLs(String webId) {
		Boolean redirectCondition = niceURLGenerator.getRedirectCondition();
		niceURLGenerator.setRedirectCondition(false); //Legacy URLs does not support redirect condition

		String urlRewriteRules = "";
		
		for (String clientDirectory : clientDirectoryStructure) {
			urlRewriteRules += niceURLGenerator.getForwardRewriteRule(clientDirectory + "/(.*)", 
					"http://" + webId + "/" + clientDirectory + "/$1");
		}

		niceURLGenerator.setRedirectCondition(redirectCondition);
		
		return urlRewriteRules;
	}
}
