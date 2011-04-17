package sk.seges.acris.generator.server.processor.utils;

import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;

public class AnchorUtils {

	public static String getAnchorTargetHref(String niceUrl, GeneratorEnvironment generatorEnvironment) {
		if (generatorEnvironment.getWebSettings().getTopLevelDomain() == null) {
			return "/" + niceUrl;
		} 
		
		String url = generatorEnvironment.getWebSettings().getTopLevelDomain();
		if (!url.endsWith("/")) {
			url += "/";
		}

		return url + niceUrl;
	}
	
}
