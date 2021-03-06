package sk.seges.acris.generator.server.processor.utils;

import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.shared.domain.GeneratorToken;

import java.io.File;

public class AnchorUtils {
	
	public static String getRelativePrefix(GeneratorToken generatorToken, boolean indexFile) {
		String niceUrl = generatorToken.getNiceUrl();
		if (File.separatorChar != '/') {
			niceUrl = niceUrl.replace(File.separatorChar, '/');
		}

		if (niceUrl.length() == 0 || indexFile) {
			return ""; //no special processing necessary 
		}

		//count number of directories in the path. If the niceurl/token is
		//en/project than there are 2 directories: en and project so we
		//have to add ../../ prefix into the path
		int count = niceUrl.split("/").length + 1;

		String pathPrefix = "";

		for (int i = 1; i < count; i++) {
			pathPrefix = pathPrefix + "../";
		}
		return pathPrefix;
	}
	
	public static String getAnchorTargetHref(String niceUrl, GeneratorEnvironment generatorEnvironment) {
		if (niceUrl == null) {
			return null;
		}
		
		if (niceUrl.startsWith("#")) {
			return niceUrl;
		}
		
		String url = generatorEnvironment.getWebSettings().getTopLevelDomain();
		
		if (url == null) {
			if (niceUrl.startsWith("/")) {
				return niceUrl;
			}
			return "/" + niceUrl;
		} 
				
		if (niceUrl.startsWith("/")) {
			if (url.endsWith("/")) {
				url = url.substring(0, url.length() - 1);
			}
		} else if (!url.endsWith("/")) {
			url += "/";
		}
		
		return url + niceUrl;
	}	
}