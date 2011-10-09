package sk.seges.acris.generator.client.collector;

import java.util.HashSet;
import java.util.Set;

import sk.seges.acris.generator.client.collector.api.NodeCollector;
import sk.seges.acris.generator.client.context.api.GeneratorClientEnvironment;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;

public class AnchorNodeCollector implements NodeCollector {
		
	@Override
	public void collect(Element rootPanel, GeneratorClientEnvironment generatorEnvironment) {
		NodeList<Element> anchors = rootPanel.getElementsByTagName("a");

		Set<String> tokens = new HashSet<String>();

		if (anchors != null) {
			for (int i = 0; i < anchors.getLength(); i++) {
				Element anchor = anchors.getItem(i);

				String href = anchor.getAttribute("href");
				
				if (isLocalHref(href, generatorEnvironment)) {
					tokens.add(stripToken(href, generatorEnvironment));
				}
			}
		}

		generatorEnvironment.getTokensCache().addTokens(tokens);
	}


	protected String stripToken(String niceUrl, GeneratorClientEnvironment generatorEnvironment) {

		if (generatorEnvironment.getTopLevelDomain() != null) {
			String url = generatorEnvironment.getTopLevelDomain();
			if (!url.endsWith("/")) {
				url += "/";
			}
			
			if (niceUrl.startsWith(url)) {
				niceUrl = niceUrl.replaceAll(url, "");
			}
		}

		if (niceUrl.startsWith("/")) {
			if (niceUrl.length() == 1) {
				return "";
			}
			niceUrl = niceUrl.substring(1);
		}

		if (niceUrl.startsWith("#")) {
			if (niceUrl.length() == 1) {
				return "";
			}
			niceUrl = niceUrl.substring(1);
		}
		return niceUrl;
	}

	protected boolean isLocalHref(String niceUrl, GeneratorClientEnvironment generatorEnvironment) {
		if (niceUrl.startsWith("/") || niceUrl.startsWith("#")) {
			return true;
		}
		
		if (generatorEnvironment.getTopLevelDomain() != null) {
			String url = generatorEnvironment.getTopLevelDomain();
			if (!url.endsWith("/")) {
				url += "/";
			}

			return niceUrl.startsWith(url) || niceUrl.startsWith(url + "#");
		} 

		return false;
	}
}