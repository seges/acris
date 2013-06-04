package sk.seges.acris.generator.server.rewriterules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LegacyURLGenerator {
	
	private final AbstractNiceURLGenerator niceURLGenerator;

	private final Boolean legacyRedirectSingleFile;

	private final String legacyRedirectFilePath;

	public LegacyURLGenerator(AbstractNiceURLGenerator niceURLGenerator, String legacyRedirectFilePath, Boolean legacyRedirectSingleFile) {
		this.niceURLGenerator = niceURLGenerator;
		this.legacyRedirectFilePath = legacyRedirectFilePath;
		this.legacyRedirectSingleFile = legacyRedirectSingleFile;
	}

	public String getLegacyURLs(String lang_country) {
		
		if (legacyRedirectSingleFile) {
			return getLangSpecificLegacyURLs(legacyRedirectFilePath);
		}
		
		String legacyURLs = "";
		
		if (lang_country == null || lang_country.length() == 0) {
			throw new RuntimeException("Do it beeter");
//			for (ELocale locale : ELocale.values()) {
//				legacyURLs += getLangSpecificLegacyURLs(legacyRedirectFilePath + "_" + locale.getLocale());
//			}
		} else {
			legacyURLs += getLangSpecificLegacyURLs(legacyRedirectFilePath + "_" + lang_country);
		}
		
		return legacyURLs;
	}
	
	private String getLangSpecificLegacyURLs(String fileName) {
		File file = new File(fileName);
		
		if (!file.exists()) {
			return "";
		}
		
		BufferedReader bufferedReader = null;
		
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		} catch (FileNotFoundException ioe) {
			return ""; //TODO add logging
 		}
		
		String line = "";
		
		String urlRewriteRules = "";

		Boolean redirectCondition = niceURLGenerator.getRedirectCondition();
		niceURLGenerator.setRedirectCondition(false); //Legacy URLs does not support redirect condition

		try {
			while ((line = bufferedReader.readLine()) != null) {
				String[] rules = line.trim().split("\\s");
				
				List<String> ruleParts = new ArrayList<String>();
				
				for (String rule : rules) {
					if (rule != null && rule.length() > 0) {
						ruleParts.add(rule);
					}
				}
				if (ruleParts.size() == 2) {
					String fromURL = ruleParts.get(0).trim();
					if (fromURL.startsWith("/")) {
						fromURL = fromURL.substring(1);
					}
					urlRewriteRules += niceURLGenerator.getPermanentRedirectRewriteRule(fromURL, ruleParts.get(1).trim());
				}
			}
		} catch (IOException e) {
			return ""; //TODO Logging
		} finally {
			niceURLGenerator.setRedirectCondition(redirectCondition);
		}
		
		return urlRewriteRules;
	}
}